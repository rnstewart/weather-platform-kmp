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
