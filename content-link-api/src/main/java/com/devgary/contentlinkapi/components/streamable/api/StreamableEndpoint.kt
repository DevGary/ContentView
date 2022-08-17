package com.devgary.contentlinkapi.components.streamable.api

import com.devgary.contentlinkapi.components.streamable.api.model.StreamableVideoResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface StreamableEndpoint {
    @GET("/videos/{shortcode}")
    suspend fun getVideo(@Path("shortcode") shortCode: String): StreamableVideoResponse
}