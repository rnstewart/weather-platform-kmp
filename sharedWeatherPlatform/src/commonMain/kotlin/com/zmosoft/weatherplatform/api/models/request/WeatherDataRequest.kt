package com.zmosoft.weatherplatform.api.models.request

import com.zmosoft.weatherplatform.api.APIKeys
import io.ktor.http.HttpMethod

class WeatherDataRequest(
    query: String = "",
    latitude: Double? = null,
    longitude: Double? = null,
    apiKeys: APIKeys
) : WeatherDataRequestBase(
    method = HttpMethod.Get,
    path = "weather",
    requireAuth = false,
    queryParams = if (query.isNotEmpty())
        mapOf(
            "q" to query,
            "appid" to apiKeys.openWeatherMap.apiKey
        )
    else if (latitude != null && longitude != null)
        mapOf(
            "lat" to latitude.toString(),
            "lon" to longitude.toString(),
            "appid" to apiKeys.openWeatherMap.apiKey
        )
    else
        mapOf()
)