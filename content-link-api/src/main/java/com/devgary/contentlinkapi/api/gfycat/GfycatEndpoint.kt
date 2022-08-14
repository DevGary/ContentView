package com.devgary.contentlinkapi.api.gfycat

import com.devgary.contentlinkapi.api.gfycat.model.*
import retrofit2.http.*

interface GfycatEndpoint {
    @POST("oauth/token")
    suspend fun authenticate(@Body gfycatAuthenticationRequest: GfycatAuthenticationRequest): GfycatAuthenticationResponse

    @GET("gfycats/{gfyid}")
    suspend fun getGfycatItem(@Header("Authorization") authorization: String, @Path("gfyid") gfycatName: String): GfycatItemResponse
}