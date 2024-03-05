# Weather Platform

### A Kotlin Multiplatform weather app for Android and iOS

This is a sample mobile app that uses the JetBrains [Kotlin Multiplatform](https://kotlinlang.org/docs/multiplatform.html) (KMP) framework to deploy builds for Android and iOS targets. The core of the app is a shared common framework built in Kotlin that compiles to Android and iOS targets, with a presentation layer on top for each platform written in their native languages (Kotlin and Swift) using fully native UI and APIs. KMP is a relatively new technology that has only recently entered full Release status, and work on it is ongoing. Because it is so new, many people (including me) are still trying to learn the most effective ways to use it. This is what I have come up with so far.

Because the core functions of the app (network calls and data manipulation) are handled in the common module, shared between the two mobile platforms, the challenge was to figure out a way to easily push data from this module out to the presentation layer to be displayed and rapidly updated in the UI using modern declarative frameworks (Jetpack Compose and SwiftUI). The method I have come up with to accomplish this is a variant on MVI (Model-View-Intent) architecture driven by a State Machine. It divides the data into individual Repositories which are segmented by function (in this case, one Repository for weather data, one for Google Maps location data). Each Repository contains the methods for sending, receiving, and mutating data. That data is pushed to a Kotlin `StateFlow`, which is observed by the presentation layer on both Android and iOS to provide updates to the UI state when the data changes. The data operations are exposed through State Machine classes, which contain the `StateFlow`s. These State Machines are easily used in both Android Kotlin classes and iOS Swift classes to provide the presentation layer with the functionality to perform necessary operations and reactive data flows to display the results.

Beyond the Repository and State Machine classes, each app is mostly a normal mobile app, using their respective native SDKs. Some concessions are made to the multiplatform nature of the codebase; for example, instead of Dagger for dependency injection (which is a JVM DI framework and will only work on Android), I use the Kotlin Native DI framework [Kodein](https://github.com/kosi-libs/Kodein). The goal is to do as much work as possible in the shared module, simplifying the native app codebases to mostly a thin layer that simply calls State Machine functions and responds to data.

## Structure

### Repositories

The heart of the architecture is the Repository, which should be a familiar concept to most developers. A Repository is a module of code that contains data and exposes functions for operating on that data. In this approach, the Repositories use `MutableStateFlow`s to push that data to the presentation layer. The use of Flows allows this to be handled in a cross-platform way thanks to Swift's `async` concurrency approach; I'll explain how that works later. But first let's look at an example of a Repository, specifically the `WeatherRepository` class in this codebase:

```
class WeatherRepository: RepositoryBase() {
    suspend fun searchWeatherByName(
        query: String = ""
    ): Result<WeatherDataResponse?> {
        return withContext (NetworkDispatcher) {
            val response = openWeatherService.getCurrentWeatherData(
                query = query
            )

            val error = response.error
            if (error == null) {
                Result.success(response.data)
            } else {
                Result.failure(error.throwable)
            }
        }
    }

    suspend fun searchWeatherByLocation(
        latitude: Double? = null,
        longitude: Double? = null
    ): Result<WeatherDataResponse?> {
        return withContext (NetworkDispatcher) {
            val response = openWeatherService.getCurrentWeatherData(
                latitude = latitude,
                longitude = longitude
            )

            val error = response.error
            if (error == null) {
                Result.success(response.data)
            } else {
                Result.failure(error.throwable)
            }
        }
    }
}
```

