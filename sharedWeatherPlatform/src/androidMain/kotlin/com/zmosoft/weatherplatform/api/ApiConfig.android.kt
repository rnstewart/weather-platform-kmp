package com.zmosoft.weatherplatform.api

import com.zmosoft.weatherplatform.storage.AndroidStorageProvider
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromStream

val format = Json {
    ignoreUnknownKeys = true
}

actual object ApiConfig {
    @OptIn(ExperimentalSerializationApi::class)
    actual val apiKeys: APIKeys
        get() = AndroidStorageProvider.appContext?.assets?.open("config/apiKeys.json")?.use { stream ->
            format.decodeFromStream<APIKeys>(stream)
        } ?: APIKeys()
}