package com.zmosoft.weatherplatform.state

import com.zmosoft.weatherplatform.api.models.response.geo.AutocompletePlacesData
import com.zmosoft.weatherplatform.api.models.response.weather.WeatherDataResponse

sealed interface MainScreenState {
    data object Empty: MainScreenState
    data class Error(
        val error: String
    ): MainScreenState
    data class AutocompleteLoaded(
        val places: List<AutocompletePlacesData.Prediction>
    ): MainScreenState
    data class WeatherData(
        val data: WeatherDataResponse?
    ): MainScreenState
    data class WeatherDataLoading(
        val data: WeatherDataResponse?
    ): MainScreenState
}