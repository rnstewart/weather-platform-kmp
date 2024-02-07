package com.zmosoft.weatherplatform.state

import com.zmosoft.weatherplatform.api.models.response.geo.AutocompletePlacesData
import com.zmosoft.weatherplatform.api.models.response.weather.WeatherDataResponse
import com.zmosoft.weatherplatform.data.SharedRepositories
import com.zmosoft.weatherplatform.repositories.interfaces.GoogleMapsInterface
import com.zmosoft.weatherplatform.repositories.interfaces.WeatherInterface
import com.zmosoft.weatherplatform.utils.BackgroundDispatcher
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class MainScreenStateMachine(
    scope: CoroutineScope?,
    private val sharedRepositories: SharedRepositories
): GoogleMapsInterface, WeatherInterface {
    val coroutineScope = scope ?: CoroutineScope(BackgroundDispatcher + Job())

    private val _state: MutableStateFlow<State> = MutableStateFlow(State.Empty)
    val state: StateFlow<State> = _state

    sealed interface State {
        data object Empty: State
        data object Loading: State
        data class Error(
            val error: String
        ): State
        data class AutocompleteLoaded(
            val places: List<AutocompletePlacesData.Prediction>
        ): State
        data class WeatherLoaded(
            val data: WeatherDataResponse?
        ): State
    }

    sealed interface Intent {
        data class SearchLocation(
            val query: String
        ): Intent
        data class SelectLocation(
            val location: AutocompletePlacesData.Prediction
        ): Intent
        data class SearchWeatherByName(
            val query: String
        ): Intent
        data class SearchWeatherByLocation(
            val latitude: Double?,
            val longitude: Double?
        ): Intent
    }

    fun process(intent: Intent) {
        when(intent) {
            is Intent.SearchLocation -> {
                onLocationSearch(intent.query)
            }
            is Intent.SelectLocation -> {
                onLocationSelected(intent.location)
            }

            is Intent.SearchWeatherByLocation -> {
                searchWeatherByLocation(
                    intent.latitude,
                    intent.longitude
                )
            }
            is Intent.SearchWeatherByName -> {
                searchWeatherByName(intent.query)
            }
        }
    }

    override fun onLocationSearch(input: String) {
        coroutineScope.launch {
            _state.emit(State.Loading)
            val result = sharedRepositories.googleMapsRepository.searchLocation(
                input = input
            )
            _state.emit(
                State.AutocompleteLoaded(
                    result
                )
            )
        }
    }

    override fun onLocationSelected(location: AutocompletePlacesData.Prediction) {
        coroutineScope.launch {
            _state.emit(State.Loading)
            sharedRepositories.googleMapsRepository.autocompleteResultSelected(
                location = location
            )?.let { (latitude, longitude) ->
                val weatherData = sharedRepositories.weatherRepository.searchWeatherByLocation(latitude, longitude)
                _state.emit(
                    State.WeatherLoaded(
                        data = weatherData
                    )
                )
            } ?: run {
                _state.emit(
                    State.WeatherLoaded(
                        data = null
                    )
                )
            }
        }
    }

    override fun searchWeatherByName(query: String) {
        coroutineScope.launch {
            _state.emit(State.Loading)
            val response = sharedRepositories.weatherRepository.searchWeatherByName(
                query = query
            )
            _state.emit(
                State.WeatherLoaded(
                    data = response
                )
            )
        }
    }

    override fun searchWeatherByLocation(latitude: Double?, longitude: Double?) {
        coroutineScope.launch {
            _state.emit(State.Loading)
            val response = sharedRepositories.weatherRepository.searchWeatherByLocation(
                latitude = latitude,
                longitude = longitude
            )
            _state.emit(
                State.WeatherLoaded(
                    data = response
                )
            )
        }
    }
}