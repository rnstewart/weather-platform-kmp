package com.zmosoft.weatherplatform.repositories

import com.zmosoft.weatherplatform.interfaces.SharedGoogleMapsInterface
import com.zmosoft.weatherplatform.interfaces.SharedWeatherInterface
import com.zmosoft.weatherplatform.repositories.data.GoogleMapsData
import com.zmosoft.weatherplatform.repositories.data.WeatherData

data class RepositoryInterfaceContainer(
    val googleMapsInterface: SharedGoogleMapsInterface,
    val weatherInterface: SharedWeatherInterface
)

data class RepositoryDataContainer(
    val googleMapsData: GoogleMapsData = GoogleMapsData(),
    val weatherData: WeatherData = WeatherData()
)