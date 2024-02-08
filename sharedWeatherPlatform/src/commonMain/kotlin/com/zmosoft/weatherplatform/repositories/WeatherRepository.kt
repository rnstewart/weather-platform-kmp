package com.zmosoft.weatherplatform.repositories

import com.zmosoft.weatherplatform.api.models.response.weather.WeatherDataResponse
import com.zmosoft.weatherplatform.utils.NetworkDispatcher
import kotlinx.coroutines.withContext

class WeatherRepository: RepositoryBase() {
    suspend fun searchWeatherByName(
        query: String = ""
    ): Result<WeatherDataResponse?> {
        return withContext (NetworkDispatcher) {
            val response = openWeatherService.getCurrentWeatherData(
                query = query
            )

            val error = response.error
            if (error == null) {
                Result.success(response.data)
            } else {
                Result.failure(error.throwable)
            }
        }
    }

    suspend fun searchWeatherByLocation(
        latitude: Double? = null,
        longitude: Double? = null
    ): Result<WeatherDataResponse?> {
        return withContext (NetworkDispatcher) {
            val response = openWeatherService.getCurrentWeatherData(
                latitude = latitude,
                longitude = longitude
            )

            val error = response.error
            if (error == null) {
                Result.success(response.data)
            } else {
                Result.failure(error.throwable)
            }
        }
    }
}