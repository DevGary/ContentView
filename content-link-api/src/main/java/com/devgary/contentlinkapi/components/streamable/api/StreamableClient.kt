package com.devgary.contentlinkapi.components.streamable.api

import android.util.Log
import com.devgary.contentcore.util.TAG
import com.devgary.contentcore.util.name
import com.devgary.contentlinkapi.components.streamable.api.model.StreamableVideoResponse
import com.devgary.contentlinkapi.components.streamable.api.model.StreamableVideoResponseAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

class StreamableClient {
    private val BASE_URL = "https://api.streamable.com/"
    
    private val cachedStreamableVideoResponses: MutableMap<String, StreamableVideoResponse> = HashMap()
    private val streamableEndpoint: StreamableEndpoint

    init {
        val moshi = Moshi.Builder()
            .add(StreamableVideoResponseAdapter())
            .add(KotlinJsonAdapterFactory())
            .build()
        
        streamableEndpoint = Retrofit.Builder()
            .client(OkHttpClient())
            .baseUrl(BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
            .create(StreamableEndpoint::class.java)
    }

    suspend fun getVideo(shortCode: String): StreamableVideoResponse {
        cachedStreamableVideoResponses[shortCode]?.let {
            Log.i(TAG, "Returning cached ${name<StreamableVideoResponse>()} for shortCode = $shortCode")
            return it
        }

        val response = streamableEndpoint.getVideo(shortCode)
        Log.i(TAG,"Returning network ${name<StreamableVideoResponse>()} for shortCode = $shortCode")

        cachedStreamableVideoResponses[shortCode] = response
        return response
    }
}