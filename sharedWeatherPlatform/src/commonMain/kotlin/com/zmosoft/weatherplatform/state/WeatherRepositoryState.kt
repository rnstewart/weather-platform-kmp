package com.zmosoft.weatherplatform.state

import com.zmosoft.weatherplatform.data.SharedRepositories
import com.zmosoft.weatherplatform.repositories.interfaces.WeatherInterface
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class WeatherRepositoryState(
    scope: CoroutineScope?,
    private val sharedRepositories: SharedRepositories
): RepositoryStateBase(scope = scope), WeatherInterface {
    override fun searchWeatherByName(query: String) {
        coroutineScope.launch {
            loading.emit(true)
            sharedRepositories.weatherRepository.searchWeatherByName(
                query = query
            )
            loading.emit(false)
        }
    }

    override fun searchWeatherByLocation(latitude: Double?, longitude: Double?) {
        coroutineScope.launch {
            loading.emit(true)
            sharedRepositories.weatherRepository.searchWeatherByLocation(
                latitude = latitude,
                longitude = longitude
            )
            loading.emit(false)
        }
    }
}