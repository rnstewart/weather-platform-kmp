package com.zmosoft.weatherplatform.api.models.request

import io.ktor.http.*

open class GoogleMapsRequestBase(
    override val method: HttpMethod,
    override val path: String,
    override val contentType: ContentType = ContentType.Application.Json,
    override val pathParams: List<Any> = listOf(),
    override val queryParams: Map<String, Any?> = mapOf(),
    override val bodyData: Any? = null,
    override val expectSuccess: Boolean = true,
    override val requireAuth: Boolean = true,
): RequestBase(
    method = method,
    path = path,
    pathBase = "maps/api"
)