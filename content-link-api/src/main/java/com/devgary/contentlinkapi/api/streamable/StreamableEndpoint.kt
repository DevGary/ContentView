package com.devgary.contentlinkapi.api.streamable

import retrofit2.http.GET
import retrofit2.http.Path

interface StreamableEndpoint {
    @GET("/videos/{shortcode}")
    suspend fun getVideo(@Path("shortcode") shortCode: String): StreamableVideoResponse
}