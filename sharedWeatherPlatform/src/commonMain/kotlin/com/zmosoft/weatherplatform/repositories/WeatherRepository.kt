package com.zmosoft.weatherplatform.repositories

import com.zmosoft.weatherplatform.api.APIResponse
import com.zmosoft.weatherplatform.repositories.data.WeatherData
import com.zmosoft.weatherplatform.utils.BackgroundDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.withContext

class WeatherRepository: RepositoryBase() {
    val data = MutableStateFlow(WeatherData())

    suspend fun searchWeather(
        query: String = "",
        latitude: Double? = null,
        longitude: Double? = null
    ) {
        withContext (BackgroundDispatcher) {
            data.emit(
                data.value.copy(
                    loading = true
                )
            )
            val response = openWeatherService.getCurrentWeatherDataByLocation(
                query = query,
                latitude = latitude,
                longitude = longitude
            )

            data.emit(
                data.value.copy(
                    data = response.data,
                    loading = false
                )
            )
            error.emit(response.error)
        }
    }
}