package com.zmosoft.weatherplatform.repositories

import com.zmosoft.weatherplatform.interfaces.GoogleMapsRepositoryState
import com.zmosoft.weatherplatform.interfaces.WeatherRepositoryState
import com.zmosoft.weatherplatform.repositories.data.GoogleMapsData
import com.zmosoft.weatherplatform.repositories.data.WeatherData

data class RepositoryStateContainer(
    val googleMapsState: GoogleMapsRepositoryState,
    val weatherState: WeatherRepositoryState
)

data class RepositoryDataContainer(
    val googleMapsData: GoogleMapsData = GoogleMapsData(),
    val weatherData: WeatherData = WeatherData()
)