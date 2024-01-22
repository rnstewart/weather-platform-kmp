# Weather Platform

### A Kotlin Multiplatform weather app for Android and iOS

This is a sample mobile app that uses the JetBrains [Kotlin Multiplatform](https://kotlinlang.org/docs/multiplatform.html) (KMP) framework to deploy builds for Android and iOS targets. The core of the app is a shared common framework built in Kotlin that compiles to Android and iOS targets, with a presentation layer on top for each platform written in their native languages (Kotlin and Swift) using fully native UI and APIs. KMP is a relatively new technology that has only recently entered full Release status, and work on it is ongoing. Because it is so new, many people (including me) are still trying to learn the most effective ways to use it. This is what I have come up with so far.

Because the core functions of the app (network calls and data manipulation) are handled in the common module, shared between the two mobile platforms, the challenge was to figure out a way to easily push data from this module out to the presentation layer to be displayed and rapidly updated in the UI using modern reactive frameworks (Jetpack Compose and SwiftUI). The method I have come up with to accomplish this is something I'm calling the Repository-Interface-Flow (RIF) architecture. It divides the data into individual Repositories which are segmented by function (in this case, one Repository for weather data, one for Google Maps location data). Each Repository contains a Kotlin `MutableStateFlow` which pushes the data as an immutable Kotlin `data class`, using the `copy()` method to mutate the data structure. This flow is then observed by the presentation layer on both Android and iOS to provide rapid updates to the UI state when the data changes. Finally, there is a shared Interface class for each Repository, which exposes functions to perform operations on the data. These Interfaces are easily used in both Android Kotlin classes and iOS Swift classes to provide the presentation layer with the functionality to perform necessary operations and reactive data flows to display the results.

Beyond the Repository and Interface classes, each app is mostly a normal mobile app, using their respective native SDKs. Some concessions are made to the multiplatform nature of the codebase; for example, instead of Dagger for dependency injection (which is a JVM DI framework and will only work on Android), I use the Kotlin Native DI framework [Kodein](https://github.com/kosi-libs/Kodein). The goal is to do as much work as possible in the shared module, simplifying the native app codebases to mostly a thin layer that simply calls Interface functions and responds to data.

## Structure

### Repositories

The heart of the RIF architecture is the Repository, which should be a familiar concept to most developers. A Repository is a module of code that contains data and exposes functions for mutating that data. In this approach, the Repositories use `MutableStateFlow`s to push that data to the presentation layer. The use of Flows allows this to be handled in a cross-platform way thanks to Swift's `async` concurrency approach; I'll explain how that works later. But first let's look at an example of a Repository, specifically the `WeatherRepository` class in this codebase:

```
class WeatherRepository: RepositoryBase() {
    val data = MutableStateFlow(WeatherData())

    suspend fun searchWeather(
        query: String = "",
        latitude: Double? = null,
        longitude: Double? = null
    ) {
        withContext (BackgroundDispatcher) {
            data.emit(
                data.value.copy(
                    loading = true
                )
            )
            val response = openWeatherService.getCurrentWeatherDataByLocation(
                query = query,
                latitude = latitude,
                longitude = longitude
            )

            data.emit(
                data.value.copy(
                    data = response.data,
                    loading = false
                )
            )
            error.emit(response.error)
        }
    }
}
```

The first thing you'll notice is that the Repository has a base class. Here's what that looks like:

```
open class RepositoryBase: DIAware {
    final override val di by DI.lazy {
        importAll(
            SharedModules.dataModule,
            SharedModules.repositoriesModule
        )
    }

    protected val googleMapsService: GoogleMapsService by di.instance()
    protected val openWeatherService: OpenWeatherService by di.instance()

    val error: MutableStateFlow<APIResponse.APIError?> = MutableStateFlow(null)
}
```

All Repositories inherit from this class, which gives them access to the Service classes for making API calls (I use [ktor](https://ktor.io/) for network services, which allows these calls to be made entirely in the common module). If the app had a local database, a reference to that would also be here (cross-platform SQL database access in Android and iOS can be accomplished using [SQLDelight](https://github.com/cashapp/sqldelight)). There's also a single shared Flow called `error`; this is used to capture any errors encountered during network calls and other operations.

Next is the `data` value, which is a `MutableStateFlow` containing the `WeatherData` class. Here's how that is defined:

```
data class WeatherData(
    val data: WeatherDataResponse? = null,
    val loading: Boolean = false
)
```

This data class represents the data values we will want to update and use for displaying values in the UI. The `searchWeather()` method in the Repository does a few things:

1. It updates the `loading` value and pushes that change. This is used in the UI to display a progress spinner while the data is loading.
2. It makes a network call to the weather API to get weather conditions for the String value `query`.
3. When it receives a response, it pulls the data from that response and pushes it to the Flow so it can be displayed in the UI (note that it also sets `loading` to false when it pushes the result of the API call).

### Interfaces

Each Repository has a corresponding interface to perform operations on it. Here is the interface for `WeatherRepository`:

```
interface WeatherInterface {
    fun searchWeather(
        query: String = "",
        latitude: Double? = null,
        longitude: Double? = null
    )
}
```

This interface is implemented in a *Shared Interface* class, like this:

```
class SharedWeatherInterface(
    scope: CoroutineScope?,
    private val sharedRepositories: SharedRepositories
): SharedInterfaceBase(scope = scope), WeatherInterface {
    override fun searchWeather(query: String, latitude: Double?, longitude: Double?) {
        coroutineScope.launch {
            sharedRepositories.weatherRepository.searchWeather(
                query = query,
                latitude = latitude,
                longitude = longitude
            )
        }
    }
}
```

The Shared Interface is what will be used across platforms to provide the same functionality. Note that it has a base class:

```
open class SharedInterfaceBase(
    scope: CoroutineScope?
) {
    val coroutineScope = scope ?: CoroutineScope(BackgroundDispatcher + Job())

    fun cancel() {
        coroutineScope.cancel()
    }
}
```

The main reason for this is to handle Coroutine Scopes. If the Shared Interface is being called from an Android app, we'll want to use a common Coroutine Scope depending on where it's used (like, for example, `viewModelScope`). But Swift in iOS doesn't have this concept, so I allow for it to be called without passing in a `scope`, which will resul in a default Scope being created.

You can see in `SharedWeatherInterface` that the `searchWeather()` method launches inside of the defined Scope and calls `WeatherRepository.searchWeather()`. This then handles the API call and pushing of the resulting data to the appropriate Flow.

## Bringing it all together

Now that we undestand how the Repository and its associated Interface are build, and how they allow us to make network calls, manipulate data, and perform all of the operations necessary in the shared cross-platform module, we want to see it in action and understand how these pieces are actually used in a working app. Let's start with Android.

### Android

The core component for integrating into the native app is a class called `SharedRepositories`:

```
class SharedRepositories: DIAware {
    override val di: DI by DI.lazy {
        importAll(
            SharedModules.dataModule,
            SharedModules.repositoriesModule
        )
    }

    val googleMapsRepository: GoogleMapsRepository by di.instance()
    val weatherRepository: WeatherRepository by di.instance()
}
```

As you can see, this is just a simple app that exposes the various Repositories. An instance of `SharedRepositories` is made available in our DI framework, allowing us to import it into the Android app's main ViewModel:

```
class MainActivityViewModel(
    activity: Activity
): ViewModel(), DIAware {
    override val di by DI.lazy {
        importAll(
            AndroidModules.vmModule,
            SharedModules.dataModule,
            SharedModules.repositoriesModule
        )
    }

    val sharedRepositories: SharedRepositories by di.instance()
}
```

We then create instances of the Shared Interfaces within the VM:

```
    private val googleMapsInterface = SharedGoogleMapsInterface(
        scope = viewModelScope,
        sharedRepositories = sharedRepositories
    )

    private val weatherInterface = SharedWeatherInterface(
        scope = viewModelScope,
        sharedRepositories = sharedRepositories
    )
```

And finally, to simplify integration into the UI, we have a single `RepositoryInterfaceContainer` class:

```
data class RepositoryInterfaceContainer(
    val googleMapsInterface: SharedGoogleMapsInterface,
    val weatherInterface: SharedWeatherInterface
)
```

Which is created in the VM:

```
    val interfaces = RepositoryInterfaceContainer(
        googleMapsInterface = googleMapsInterface,
        weatherInterface = weatherInterface
    )
```

There is also a corresponding container for data:

```
data class RepositoryDataContainer(
    val googleMapsData: GoogleMapsData = GoogleMapsData(),
    val weatherData: WeatherData = WeatherData()
)
```

Now we're ready to pull it all together in `MainActivity`:

```
class MainActivity : ComponentActivity(), DIAware {
    override val di by DI.lazy {
        importAll(
            AndroidModules.vmModule,
            SharedModules.dataModule,
            SharedModules.repositoriesModule
        )
    }

    ...

    private lateinit var viewModel: MainActivityViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
            ...
            val vm: MainActivityViewModel by di.instance(
                arg = this
            )
            viewModel = vm

            setContent {
                WeatherPlatformTheme {
                    CompositionLocalProvider(
                        LocalRepositoryContent provides RepositoryContent(
                            data = RepositoryDataContainer(
                                googleMapsData = viewModel.sharedRepositories.googleMapsRepository.data.collectAsState().value,
                                weatherData = viewModel.sharedRepositories.weatherRepository.data.collectAsState().value
                            ),
                            interfaces = viewModel.interfaces
                        )
                    ) {
                        MainScreen(
                            onLocationClicked = {
                                if (permissionsRequest.checkPermissions(this)) {
                                    withContext(Dispatchers.Main) {
                                        viewModel.updateLocation()
                                    }
                                }
                            }
                        )
                    }
                }
            }
    }
```

Two things to note here:

1. The data from the repositories uses `collectAsState()` to convert the Flows to a `State` class appropriate for use in Jetpack Compose. This is what allows the reactive UI framework to automatically update itself as new data is pushed to the Flows. It should be familiar to any Android developer who has used Flows with Compose.
2. I use a `CompositionLocalProvider` to send the data and interfaces to the Compose scope. This is a convenient tool that allows `RepositoryContent` to become part of the general environment in Compose, ready to pull out any time we need it, instead of having to pass a bunch of different objects into the Composable signatures.

Now the Interfaces and data objects are easily accessible to all Composables in our app. They can be retrieved like this:
```
fun WeatherSearchScreen(
    modifier: Modifier = Modifier,
    onLocationClicked: suspend () -> Unit
) {
    val content = LocalRepositoryContent.current
    val interfaces = content.interfaces
    val data = content.data
    val weatherData = data.weatherData.data
}
```

Operations on the Repositories can be accessed by the Interface functions from inside compose:
```
interfaces?.weatherInterface?.searchWeather(searchQuery)
```

And when those operations run, the updated data will be pushed to the Flows through the `CompositionLocalProvider` and into the Composables, triggering a recomposition and update of the UI. This is a fully reactive architecture taking advantage of the tools Kotlin gives us for such an approach.

### iOS

Now, this is all well and good for Android, and we would expect it to work there because these are Kotlin and Android tools. But how well does it work with Swift and iOS? This is where things get interesting.

I should qualify this by saying I am primarily an Android/Kotlin developer. I'm pretty new to Swift and iOS, so I know my skills there are still lacking. But I was pleasantly surprised with how easy it was to transition from Kotlin to Swift, and while the iOS APIs are dramtically different from the Android ones, the advantage of KMP is that the majority of code can be written in Kotlin, so this minimizes the amount of native Swift/iOS knowledge I needed to get the job done. What I have her works very well, but I'm sure there's plenty of room for improvement.

### Concurrency on iOS: `await` and Coroutines

The first and most important thing to tackle here is concurrency. All of the Kotlin concurrency code uses Coroutines of course, but Swift doesn't have an awareness of Kotlin Coroutines. Even if it did, the Kotlin code doesn't compile to Swift on iOS; it compiles to Objective-C (JetBrains is working on this, fortunately). So at first you might think that handling concurrency between Kotlin Native and Swift is going to be a big mess.

It turns out that's not the case, though. While Swift doesn't have Coroutines as Kotlin developers know it, it does have a [concurrency model](https://docs.swift.org/swift-book/documentation/the-swift-programming-language/concurrency/) that is very similar, with `Tasks` and the `await` keyword. The conceptual similarities are so strong that Kotlin Native Coroutines translate pretty directly to Swift async tasks. This makes our job a lot easier. There are some problems with job cancellation, but Touchlab has a nice library called [SKIE](https://skie.touchlab.co/) that alleviates these problems (along with a few others).

So in order to use a Kotlin `MutableStateFlow` in Swift, all we really need to do is declare a `Task` and use the `await` keyword to listen for the result. This, combined with the `ObservableObject` class and the `@Published` annotation in SwiftUI give us pretty much exactly the same result as `collectAsState()` in Jetpack Compose. When the underlying Repository runs a mutation function and emits new data, that data is pushed to the appropriate published object in Swift and it triggers an update in the reactive SwiftUI code.

In order to make this work, I have to add one more layer on top of the Shared Interface specifically for Swift: an *Observable* Interface. Here's an example:

```
class ObservableWeatherInterface: ObservableObject, WeatherInterface {
    private let repositories: Repositories = Repositories()
    
    private let sharedWeatherInterface: SharedWeatherInterface
    
    @Published
    private(set) var data: WeatherData

    init() {
        let sharedRepositories: SharedRepositories = repositories.sharedRepositories
        let repo = sharedRepositories.weatherRepository
        self.data = repo.data.value
        self.sharedWeatherInterface = SharedWeatherInterface(
            scope: nil,
            sharedRepositories: sharedRepositories
        )
    }

    func setup() {
        Task.detached {
            for await dataFlow in self.repositories.sharedRepositories.weatherRepository.data {
                DispatchQueue.main.async {
                    self.data = dataFlow
                }
            }
        }
    }

    func searchWeather(query: String, latitude: KotlinDouble?, longitude: KotlinDouble?) {
        sharedWeatherInterface.searchWeather(query: query, latitude: latitude, longitude: longitude)
    }
}
```

As you can see, this inherits from `ObservableObject`, which allows us to use it as a `StateObject` in a SwiftUi struct, and it implements the base `WeatherInterface`. This means it has all of the same functions as `SharedWeatherInterface`; they just act as pass-through functions to the Shared Interface functions which operate on the Repository itself.

There is one other new thing here: the `Repositories` class. This is just a simple helper class created in the Kotlin shared module specifically for iOS:

```
class Repositories: DIAware {
    override val di by DI.lazy {
        importAll(
            SharedModules.dataModule,
            SharedModules.repositoriesModule
        )
    }

    val sharedRepositories: SharedRepositories by di.instance()
}
```

Writing it this way makes it easy to declare in a Swift class, and you can see how I do that at the top of `ObservableWeatherInterface`. Once I have the `repositories` object, I can pull `sharedRepositories` out of it and use it in `init()` to create the `SharedWeatherInterface`. After that, I simply need to call `setup()`, which creates a Swift `Task` and sets up an asynchronous watcher on the `data` Flow. Any time new data is pushed to that Flow, the watcher responds and updates the Published `data` field in this class, which will trigger an update in the SwiftUI code. Here's how the class is used:

```
struct ContentView: View {
    @StateObject var weatherInterface: ObservableWeatherInterface = ObservableWeatherInterface()

    ...

        var body: some View {
            let weatherData = weatherInterface.data

            VStack {
                // UI code using weatherData
            }.onAppear {
                weatherInterface.setup()
            }
        }
}
```
