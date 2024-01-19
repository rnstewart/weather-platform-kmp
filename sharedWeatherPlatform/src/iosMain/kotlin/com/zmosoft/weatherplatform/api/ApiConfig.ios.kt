package com.zmosoft.weatherplatform.api

import kotlinx.serialization.json.Json
import platform.Foundation.NSBundle
import platform.Foundation.NSString
import platform.Foundation.stringWithContentsOfURL

val format = Json {
    ignoreUnknownKeys = true
}

actual object ApiConfig {
    actual val apiKeys: APIKeys
        get() = NSBundle.mainBundle.URLForResource("apiKeys.json", null)?.let {
            NSString.stringWithContentsOfURL(it)?.let { jsonString ->
                format.decodeFromString(
                    deserializer = APIKeys.serializer(),
                    string = jsonString.toString()
                )
            }
        } ?: APIKeys()
}