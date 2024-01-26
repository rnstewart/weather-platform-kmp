package com.zmosoft.weatherplatform.repositories

import com.zmosoft.weatherplatform.api.APIResponse
import com.zmosoft.weatherplatform.interfaces.GoogleMapsRepositoryState
import com.zmosoft.weatherplatform.interfaces.WeatherRepositoryState
import com.zmosoft.weatherplatform.repositories.data.GoogleMapsData
import com.zmosoft.weatherplatform.repositories.data.WeatherData

data class RepositoryStateContainer(
    val googleMapsState: GoogleMapsRepositoryState,
    val weatherState: WeatherRepositoryState
)

data class LoadingState(
    val googleMaps: Boolean = false,
    val weather: Boolean = false
)

data class ErrorState(
    val googleMaps: String? = null,
    val weather: String? = null
)

data class RepositoryDataContainer(
    val googleMapsData: GoogleMapsData = GoogleMapsData(),
    val weatherData: WeatherData = WeatherData()
)