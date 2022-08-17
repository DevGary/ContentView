package com.devgary.contentlinkapi.components.gfycat.api

import com.devgary.contentlinkapi.components.gfycat.api.model.GfycatAuthenticationRequest
import com.devgary.contentlinkapi.components.gfycat.api.model.GfycatAuthenticationResponse
import com.devgary.contentlinkapi.components.gfycat.api.model.GfycatItemResponse
import retrofit2.http.*

interface GfycatEndpoint {
    @POST("oauth/token")
    suspend fun authenticate(@Body gfycatAuthenticationRequest: GfycatAuthenticationRequest): GfycatAuthenticationResponse

    @GET("gfycats/{gfyid}")
    suspend fun getGfycatItem(@Header("Authorization") authorization: String, @Path("gfyid") gfycatName: String): GfycatItemResponse
}