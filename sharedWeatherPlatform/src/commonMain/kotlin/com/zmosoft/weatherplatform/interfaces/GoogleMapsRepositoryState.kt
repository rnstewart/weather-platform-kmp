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
            sharedRepositories.googleMapsRepository.placesAutoComplete(
                input = input,
                latitude = latitude,
                longitude = longitude
            )
        }
    }

    override fun autocompleteResultSelected(
        location: AutocompletePlacesData.Prediction
    ) {
        coroutineScope.launch {
            sharedRepositories.googleMapsRepository.autocompleteResultSelected(
                location = location
            )?.let { (latitude, longitude) ->
                sharedRepositories.weatherRepository.searchWeatherByLocation(latitude, longitude)
            }
        }
    }

    override fun placeDetails(placeId: String, fields: String?) {
        coroutineScope.launch {
            sharedRepositories.googleMapsRepository.placeDetails(
                placeId = placeId,
                fields = fields
            )
        }
    }
}