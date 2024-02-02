package com.zmosoft.weatherplatform.repositories

import com.zmosoft.weatherplatform.api.models.response.geo.AutocompletePlacesData
import com.zmosoft.weatherplatform.repositories.data.GoogleMapsData
import com.zmosoft.weatherplatform.utils.BackgroundDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.withContext

class GoogleMapsRepository: RepositoryBase() {
    private val _data = MutableStateFlow(GoogleMapsData())
    val data: StateFlow<GoogleMapsData> = _data

    suspend fun clear() {
        _data.emit(GoogleMapsData())
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

            _data.emit(
                _data.value.copy(
                    autocompletePredictions = response.data?.predictions ?: listOf()
                )
            )
            setError(response.error?.error ?: "")
        }
    }

    suspend fun autocompleteResultSelected(
        location: AutocompletePlacesData.Prediction
    ): Pair<Double, Double>? {
        return withContext (BackgroundDispatcher) {
            val placeId = location.placeId
            if (placeId?.isNotEmpty() == true) {
                val response = googleMapsService.placeDetails(placeId = placeId)
                val locationResult = response.data?.result?.geometry?.location
                val latitude = locationResult?.latitude
                val longitude = locationResult?.longitude
                if (latitude != null && longitude != null) {
                    _data.emit(
                        _data.value.copy(
                            autocompletePredictions = listOf(),
                            placeDetails = response.data.result
                        )
                    )
                    Pair(
                        latitude,
                        longitude
                    )
                } else {
                    setError(response.error?.error ?: "")
                    null
                }
            } else {
                null
            }
        }
    }

    suspend fun placeDetails(
        placeId: String,
        fields: String? = "address_component,name,geometry"
    ) {
        withContext (BackgroundDispatcher) {
            val response = googleMapsService.placeDetails(placeId = placeId, fields = fields)

            _data.emit(
                _data.value.copy(
                    placeDetails = response.data?.result
                )
            )
            setError(response.error?.error ?: "")
        }
    }
}
