package com.zmosoft.weatherplatform.api.models.request

import io.ktor.http.HttpMethod

class PlaceDetailsRequest(
    placeId: String,
    fields: String? = "address_component,name,geometry",
    apiKey: String
): GoogleMapsRequestBase(
    path = "place/details/json",
    method = HttpMethod.Get,
    queryParams = mapOf(
        "place_id" to placeId,
        "fields" to fields,
        "key" to apiKey
    ),
    requireAuth = false
)