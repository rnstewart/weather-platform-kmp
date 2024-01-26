package com.zmosoft.weatherplatform.interfaces

import com.zmosoft.weatherplatform.api.models.response.geo.AutocompletePlacesData
import com.zmosoft.weatherplatform.data.SharedRepositories
import com.zmosoft.weatherplatform.repositories.interfaces.GoogleMapsInterface
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class GoogleMapsRepositoryState(
    scope: CoroutineScope?,
    private val sharedRepositories: SharedRepositories
): RepositoryStateBase(scope = scope), GoogleMapsInterface {
    override fun placesAutoComplete(input: String, latitude: Double?, longitude: Double?) {
        coroutineScope.launch {
            loading.emit(true)
            sharedRepositories.googleMapsRepository.placesAutoComplete(
                input = input,
                latitude = latitude,
                longitude = longitude
            )
            loading.emit(false)
        }
    }

    override fun autocompleteResultSelected(
        location: AutocompletePlacesData.Prediction
    ) {
        coroutineScope.launch {
            loading.emit(true)
            sharedRepositories.googleMapsRepository.autocompleteResultSelected(
                location = location
            )?.let { (latitude, longitude) ->
                sharedRepositories.weatherRepository.searchWeatherByLocation(latitude, longitude)
                loading.emit(false)
            } ?: run {
                loading.emit(false)
            }
        }
    }

    override fun placeDetails(placeId: String, fields: String?) {
        coroutineScope.launch {
            loading.emit(true)
            sharedRepositories.googleMapsRepository.placeDetails(
                placeId = placeId,
                fields = fields
            )
            loading.emit(false)
        }
    }
}