package com.zmosoft.weatherplatform.repositories

import com.zmosoft.weatherplatform.api.models.response.weather.WeatherDataResponse
import com.zmosoft.weatherplatform.utils.NetworkDispatcher
import kotlinx.coroutines.withContext

class WeatherRepository: RepositoryBase() {
    suspend fun searchWeatherByName(
        query: String = ""
    ): WeatherDataResponse? {
        return withContext (NetworkDispatcher) {
            val response = openWeatherService.getCurrentWeatherData(
                query = query
            )

            setError(response.error?.error ?: "")
            response.data
        }
    }

    suspend fun searchWeatherByLocation(
        latitude: Double? = null,
        longitude: Double? = null
    ): WeatherDataResponse? {
        return withContext (NetworkDispatcher) {
            val response = openWeatherService.getCurrentWeatherData(
                latitude = latitude,
                longitude = longitude
            )

            setError(response.error?.error ?: "")
            response.data
        }
    }
}