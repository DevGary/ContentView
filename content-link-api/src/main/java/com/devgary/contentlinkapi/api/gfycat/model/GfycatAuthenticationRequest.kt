package com.devgary.contentlinkapi.api.gfycat.model

import com.squareup.moshi.Json

data class GfycatAuthenticationRequest(
    @Json(name = "client_id") private val clientId: String,
    @Json(name = "client_secret") private val clientSecret: String,
    @Json(name = "grant_type") private var grantType: String = "client_credentials",
)