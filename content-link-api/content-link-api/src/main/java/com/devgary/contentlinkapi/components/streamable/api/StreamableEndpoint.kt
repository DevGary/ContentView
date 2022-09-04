package com.devgary.contentlinkapi.components.streamable.api

import com.devgary.contentlinkapi.components.streamable.api.model.StreamableVideoResponse
import com.devgary.contentlinkapi.components.streamable.api.model.StreamableVideoResponseAdapter
import com.squareup.moshi.Moshi.*
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path

interface StreamableEndpoint {
    @GET("/videos/{shortcode}")
    suspend fun getVideo(@Path("shortcode") shortCode: String): StreamableVideoResponse
    
    companion object {
        fun create(baseUrl: String = "https://api.streamable.com/"): StreamableEndpoint {
            val moshi = Builder()
                .add(StreamableVideoResponseAdapter())
                .add(KotlinJsonAdapterFactory())
                .build()

            return Retrofit.Builder()
                .client(OkHttpClient())
                .baseUrl(baseUrl)
                .addConverterFactory(MoshiConverterFactory.create(moshi))
                .build()
                .create(StreamableEndpoint::class.java)
        }
    }
}