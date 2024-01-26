package com.zmosoft.weatherplatform.repositories.interfaces

interface WeatherInterface {
    fun searchWeatherByName(
        query: String = ""
    )

    fun searchWeatherByLocation(
        latitude: Double? = null,
        longitude: Double? = null
    )
}