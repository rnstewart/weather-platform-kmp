package com.zmosoft.weatherplatform.repositories

import com.zmosoft.weatherplatform.repositories.data.WeatherData
import com.zmosoft.weatherplatform.utils.BackgroundDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.withContext

class WeatherRepository: RepositoryBase() {
    val data = MutableStateFlow(WeatherData())

    suspend fun searchWeatherByName(
        query: String = ""
    ) {
        withContext (BackgroundDispatcher) {
            val response = openWeatherService.getCurrentWeatherData(
                query = query
            )

            data.emit(
                data.value.copy(
                    data = response.data
                )
            )
            error.emit(response.error?.error ?: "")
        }
    }

    suspend fun searchWeatherByLocation(
        latitude: Double? = null,
        longitude: Double? = null
    ) {
        withContext (BackgroundDispatcher) {
            val response = openWeatherService.getCurrentWeatherData(
                latitude = latitude,
                longitude = longitude
            )

            data.emit(
                data.value.copy(
                    data = response.data
                )
            )
            error.emit(response.error?.error ?: "")
        }
    }
}