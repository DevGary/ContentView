package com.devgary.contentlinkapi.handlers.gfycat.api

import com.devgary.contentlinkapi.handlers.gfycat.api.model.GfycatAuthenticationRequest
import com.devgary.contentlinkapi.handlers.gfycat.api.model.GfycatAuthenticationResponse
import com.devgary.contentlinkapi.handlers.gfycat.api.model.GfycatItemResponse
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.*

interface GfycatEndpoint {
    @POST("oauth/token")
    suspend fun authenticate(@Body gfycatAuthenticationRequest: GfycatAuthenticationRequest): GfycatAuthenticationResponse

    @GET("gfycats/{gfyid}")
    suspend fun getGfycatItem(@Header("Authorization") authorization: String, @Path("gfyid") gfycatName: String): GfycatItemResponse

    companion object {
        fun create(baseUrl: String = "https://api.gfycat.com/v1/"): GfycatEndpoint {
            val moshi = Moshi.Builder()
                .add(KotlinJsonAdapterFactory())
                .build()

            return Retrofit.Builder()
                .client(OkHttpClient())
                .baseUrl(baseUrl)
                .addConverterFactory(MoshiConverterFactory.create(moshi))
                .build()
                .create(GfycatEndpoint::class.java)
        }
    }
}