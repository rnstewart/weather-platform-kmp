package com.zmosoft.weatherplatform.repositories

import com.zmosoft.weatherplatform.api.models.response.geo.AutocompletePlacesData
import com.zmosoft.weatherplatform.repositories.data.GoogleMapsData
import com.zmosoft.weatherplatform.utils.BackgroundDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.withContext

class GoogleMapsRepository: RepositoryBase() {
    val data = MutableStateFlow(GoogleMapsData())

    suspend fun isLoading(loading: Boolean) {
        data.emit(
            data.value.copy(
                loading = loading
            )
        )
        error.emit(null)
    }

    suspend fun clear() {
        data.emit(GoogleMapsData())
    }

    suspend fun placesAutoComplete(
        input: String,
        latitude: Double? = null,
        longitude: Double? = null
    ) {
        withContext (BackgroundDispatcher) {
            val response = googleMapsService.placesAutoComplete(
                input = input,
                latitude = latitude,
                longitude = longitude
            )

            data.emit(
                data.value.copy(
                    autocompletePredictions = response.data?.predictions ?: listOf(),
                    loading = false
                )
            )
            error.emit(response.error)
        }
    }

    suspend fun autocompleteResultSelected(
        location: AutocompletePlacesData.Prediction,
        weatherRepository: WeatherRepository
    ) {
        withContext (BackgroundDispatcher) {
            val placeId = location.placeId
            if (placeId?.isNotEmpty() == true) {
                val response = googleMapsService.placeDetails(placeId = placeId)
                val locationResult = response.data?.result?.geometry?.location
                if (locationResult != null) {
                    val latitude = locationResult.latitude
                    val longitude = locationResult.longitude
                    if (latitude != null && longitude != null) {
                        weatherRepository.searchWeather(
                            latitude = latitude,
                            longitude = longitude
                        )
                    }
                    data.emit(
                        data.value.copy(
                            autocompletePredictions = listOf(),
                            placeDetails = response.data.result
                        )
                    )
                } else {
                    error.emit(response.error)
                }
            }
        }
    }

    suspend fun placeDetails(
        placeId: String,
        fields: String? = "address_component,name,geometry"
    ) {
        withContext (BackgroundDispatcher) {
            val response = googleMapsService.placeDetails(placeId = placeId, fields = fields)

            data.emit(
                data.value.copy(
                    placeDetails = response.data?.result,
                    loading = false
                )
            )
            error.emit(response.error)
        }
    }
}
