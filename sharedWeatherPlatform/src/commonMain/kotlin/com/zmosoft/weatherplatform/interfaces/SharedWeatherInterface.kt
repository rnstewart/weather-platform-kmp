package com.zmosoft.weatherplatform.interfaces

import com.zmosoft.weatherplatform.data.SharedRepositories
import com.zmosoft.weatherplatform.repositories.interfaces.WeatherInterface
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class SharedWeatherInterface(
    scope: CoroutineScope?,
    private val sharedRepositories: SharedRepositories
): SharedInterfaceBase(scope = scope), WeatherInterface {
    override fun isLoading(loading: Boolean) {
        coroutineScope.launch {
            sharedRepositories.weatherRepository.isLoading(loading)
        }
    }

    override fun searchWeather(query: String, latitude: Double?, longitude: Double?) {
        coroutineScope.launch {
            sharedRepositories.weatherRepository.searchWeather(
                query = query,
                latitude = latitude,
                longitude = longitude
            )
        }
    }
}