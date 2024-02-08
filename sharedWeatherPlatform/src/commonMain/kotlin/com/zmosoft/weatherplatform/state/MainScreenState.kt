package com.zmosoft.weatherplatform.state

import com.zmosoft.weatherplatform.api.models.response.geo.AutocompletePlacesData
import com.zmosoft.weatherplatform.api.models.response.weather.WeatherDataResponse

sealed interface MainScreenState {
    data object Empty: MainScreenState
    data object Loading: MainScreenState
    data class Error(
        val error: String
    ): MainScreenState
    data class AutocompleteLoaded(
        val places: List<AutocompletePlacesData.Prediction>
    ): MainScreenState
    data class WeatherLoaded(
        val data: WeatherDataResponse?
    ): MainScreenState
}