package com.zmosoft.weatherplatform.state

import com.zmosoft.weatherplatform.api.models.response.geo.AutocompletePlacesData

sealed interface MainScreenIntent {
    data class SearchLocation(
        val query: String
    ): MainScreenIntent
    data class SelectLocation(
        val location: AutocompletePlacesData.Prediction
    ): MainScreenIntent
    data class SearchWeatherByName(
        val query: String
    ): MainScreenIntent
    data class SearchWeatherByLocation(
        val latitude: Double?,
        val longitude: Double?
    ): MainScreenIntent
}