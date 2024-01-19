package com.zmosoft.weatherplatform.repositories.interfaces

interface WeatherInterface {
    fun isLoading(loading: Boolean)
    fun searchWeather(
        query: String = "",
        latitude: Double? = null,
        longitude: Double? = null
    )

}