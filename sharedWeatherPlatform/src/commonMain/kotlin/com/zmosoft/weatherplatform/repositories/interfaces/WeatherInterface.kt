package com.zmosoft.weatherplatform.repositories.interfaces

interface WeatherInterface {
    fun searchWeather(
        query: String = "",
        latitude: Double? = null,
        longitude: Double? = null
    )

}