package com.devgary.contentlinkapi.api.gfycat.model

import com.squareup.moshi.Json

data class GfycatAuthenticationResponse(
    @Json(name = "token_type") private val tokenType: String? = null,
    @Json(name = "scope") private val scope: String? = null,
    @Json(name = "expires_in") val expiresIn: Int,
    @Json(name = "access_token") val accessToken: String,
    var expiryTimeUnix: Long = 0
)