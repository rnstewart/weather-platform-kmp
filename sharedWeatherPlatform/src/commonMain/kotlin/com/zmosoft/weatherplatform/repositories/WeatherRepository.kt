package com.zmosoft.weatherplatform.repositories

import com.zmosoft.weatherplatform.repositories.data.WeatherData
import com.zmosoft.weatherplatform.utils.BackgroundDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.withContext

class WeatherRepository: RepositoryBase() {
    private val _data = MutableStateFlow(WeatherData())
    val data: StateFlow<WeatherData> = _data

    suspend fun searchWeatherByName(
        query: String = ""
    ) {
        withContext (BackgroundDispatcher) {
            val response = openWeatherService.getCurrentWeatherData(
                query = query
            )

            _data.emit(
                _data.value.copy(
                    data = response.data
                )
            )
            setError(response.error?.error ?: "")
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

            _data.emit(
                _data.value.copy(
                    data = response.data
                )
            )
            setError(response.error?.error ?: "")
        }
    }
}