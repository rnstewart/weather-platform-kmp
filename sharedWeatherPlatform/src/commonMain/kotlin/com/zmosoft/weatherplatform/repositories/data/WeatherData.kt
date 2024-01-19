package com.zmosoft.weatherplatform.repositories.data

import com.zmosoft.weatherplatform.api.models.response.weather.WeatherDataResponse

data class WeatherData(
    val data: WeatherDataResponse? = null,
    val loading: Boolean = false
)