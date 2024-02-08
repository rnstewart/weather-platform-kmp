package com.zmosoft.weatherplatform.state

import com.zmosoft.weatherplatform.api.models.response.geo.AutocompletePlacesData
import com.zmosoft.weatherplatform.data.SharedRepositories
import com.zmosoft.weatherplatform.utils.BackgroundDispatcher
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class MainScreenStateMachine(
    scope: CoroutineScope?,
    private val sharedRepositories: SharedRepositories
) {
    private val coroutineScope = scope ?: CoroutineScope(BackgroundDispatcher + Job())

    private val _state: MutableStateFlow<MainScreenState> = MutableStateFlow(MainScreenState.Empty)
    val state: StateFlow<MainScreenState> = _state

    fun process(intent: MainScreenIntent) {
        when(intent) {
            is MainScreenIntent.SearchLocation -> {
                onLocationSearch(intent.query)
            }
            is MainScreenIntent.SelectLocation -> {
                onLocationSelected(intent.location)
            }

            is MainScreenIntent.SearchWeatherByLocation -> {
                searchWeatherByLocation(
                    intent.latitude,
                    intent.longitude
                )
            }
            is MainScreenIntent.SearchWeatherByName -> {
                searchWeatherByName(intent.query)
            }
        }
    }

    private fun onLocationSearch(input: String) {
        coroutineScope.launch {
            _state.emit(MainScreenState.Loading)
            val result = sharedRepositories.googleMapsRepository.searchLocation(
                input = input
            )
            _state.emit(
                MainScreenState.AutocompleteLoaded(
                    result
                )
            )
        }
    }

    private fun onLocationSelected(location: AutocompletePlacesData.Prediction) {
        coroutineScope.launch {
            _state.emit(MainScreenState.Loading)
            sharedRepositories.googleMapsRepository.autocompleteResultSelected(
                location = location
            )?.let { (latitude, longitude) ->
                val weatherData = sharedRepositories.weatherRepository.searchWeatherByLocation(latitude, longitude)
                _state.emit(
                    MainScreenState.WeatherLoaded(
                        data = weatherData
                    )
                )
            } ?: run {
                _state.emit(
                    MainScreenState.WeatherLoaded(
                        data = null
                    )
                )
            }
        }
    }

    private fun searchWeatherByName(query: String) {
        coroutineScope.launch {
            _state.emit(MainScreenState.Loading)
            val response = sharedRepositories.weatherRepository.searchWeatherByName(
                query = query
            )
            _state.emit(
                MainScreenState.WeatherLoaded(
                    data = response
                )
            )
        }
    }

    private fun searchWeatherByLocation(latitude: Double?, longitude: Double?) {
        coroutineScope.launch {
            _state.emit(MainScreenState.Loading)
            val response = sharedRepositories.weatherRepository.searchWeatherByLocation(
                latitude = latitude,
                longitude = longitude
            )
            _state.emit(
                MainScreenState.WeatherLoaded(
                    data = response
                )
            )
        }
    }
}