package com.zmosoft.weatherplatform.api

import com.zmosoft.weatherplatform.api.models.request.WeatherDataRequest
import com.zmosoft.weatherplatform.api.models.response.weather.WeatherDataResponse

class OpenWeatherService(
    private val apiKeys: APIKeys
) : Api(
    baseUrl = apiKeys.openWeatherMap.apiHost
) {
    suspend fun getCurrentWeatherDataByLocation(query: String = "", latitude: Double? = null, longitude: Double? = null): APIResponse<WeatherDataResponse> {
        return apiCall(
            WeatherDataRequest(
                query = query,
                latitude = latitude,
                longitude = longitude,
                apiKeys = apiKeys
            )
        )
    }
}