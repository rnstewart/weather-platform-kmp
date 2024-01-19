package com.zmosoft.weatherplatform.api.models.response.geo

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AutocompletePlacesData(
    val status: String? = null,
    val predictions: List<Prediction>? = null
) {
    @kotlinx.serialization.Serializable
    data class Prediction(
        val description: String? = null,
        @SerialName("distance_meters")
        val distanceMeters: Long? = null,
        val id: String? = null,
        @SerialName("matched_substrings")
        val matchedSubstrings: List<MatchedSubstring>? = null,
        @SerialName("place_id")
        val placeId: String? = null,
        val reference: String? = null,
        val terms: List<Term>? = null,
        val types: List<String>? = null
    ) {
        val name: String
            get() = description ?: ""

        @kotlinx.serialization.Serializable
        data class MatchedSubstring(
            val length: Int? = null,
            val offset: Int? = null
        )

        @kotlinx.serialization.Serializable
        data class Term(
            val offset: Int? = null,
            val value: String? = null
        )
    }
}
