package com.zmosoft.weatherplatform.state

import com.zmosoft.weatherplatform.api.models.response.geo.AutocompletePlacesData
import com.zmosoft.weatherplatform.api.models.response.weather.WeatherDataResponse
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

    private val currentData: WeatherDataResponse?
        get() = state.value.let {
            (it as? MainScreenState.WeatherData)?.data ?: (it as? MainScreenState.WeatherDataLoading)?.data
        }

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
            _state.emit(
                MainScreenState.WeatherDataLoading(
                    data = currentData
                )
            )
            val result = sharedRepositories.googleMapsRepository.searchLocation(
                input = input
            )
            _state.emit(
                if (result.isSuccess) {
                    MainScreenState.AutocompleteLoaded(
                        result.getOrNull() ?: listOf()
                    )
                } else {
                    MainScreenState.Error(
                        result.exceptionOrNull()?.message ?: ""
                    )
                }
            )
        }
    }

    private fun onLocationSelected(location: AutocompletePlacesData.Prediction) {
        coroutineScope.launch {
            _state.emit(
                MainScreenState.WeatherDataLoading(
                    data = currentData
                )
            )
            val result = sharedRepositories.googleMapsRepository.autocompleteResultSelected(
                location = location
            )
            val error = result.exceptionOrNull()?.message
            if (error?.isNotEmpty() == true) {
                MainScreenState.Error(error)
            } else {
                result.getOrNull()?.let { (latitude, longitude) ->
                    val weatherData = sharedRepositories.weatherRepository.searchWeatherByLocation(
                        latitude,
                        longitude
                    )
                    val weatherDataError = weatherData.exceptionOrNull()?.message
                    _state.emit(
                        if (weatherDataError?.isNotEmpty() == true) {
                            MainScreenState.Error(weatherDataError)
                        } else {
                            MainScreenState.WeatherData(
                                data = weatherData.getOrNull()
                            )
                        }
                    )
                } ?: run {
                    _state.emit(
                        MainScreenState.WeatherData(
                            data = null
                        )
                    )
                }
            }
        }
    }

    private fun searchWeatherByName(query: String) {
        coroutineScope.launch {
            _state.emit(
                MainScreenState.WeatherDataLoading(
                    data = currentData
                )
            )
            val response = sharedRepositories.weatherRepository.searchWeatherByName(
                query = query
            )
            val error = response.exceptionOrNull()?.message
            _state.emit(
                if (error?.isNotEmpty() == true) {
                    MainScreenState.Error(error)
                } else {
                    MainScreenState.WeatherData(
                        data = response.getOrNull()
                    )
                }
            )
        }
    }

    private fun searchWeatherByLocation(latitude: Double?, longitude: Double?) {
        coroutineScope.launch {
            _state.emit(
                MainScreenState.WeatherDataLoading(
                    data = currentData
                )
            )
            val response = sharedRepositories.weatherRepository.searchWeatherByLocation(
                latitude = latitude,
                longitude = longitude
            )
            val error = response.exceptionOrNull()?.message
            _state.emit(
                if (error?.isNotEmpty() == true) {
                    MainScreenState.Error(error)
                } else {
                    MainScreenState.WeatherData(
                        data = response.getOrNull()
                    )
                }
            )
        }
    }
}