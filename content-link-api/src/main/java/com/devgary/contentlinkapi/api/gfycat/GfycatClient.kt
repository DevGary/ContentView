package com.devgary.contentlinkapi.api.gfycat

import android.util.Log
import com.devgary.contentcore.util.TAG
import com.devgary.contentcore.util.name
import com.devgary.contentcore.util.secondsToMillis
import com.devgary.contentlinkapi.api.gfycat.model.GfycatAuthenticationRequest
import com.devgary.contentlinkapi.api.gfycat.model.GfycatAuthenticationResponse
import com.devgary.contentlinkapi.api.gfycat.model.GfycatItem
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

class GfycatClient(
    private val clientId: String,
    private val clientSecret: String,
) {
    private val gfycatEndpoint: GfycatEndpoint
    private val BASE_URL = "https://api.gfycat.com/v1/"

    private var cachedGfycatAuthResponse: GfycatAuthenticationResponse? = null
    private val cachedGfycatItems: MutableMap<String, GfycatItem> by lazy { HashMap() }
    
    init {
        val moshi = Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()

        gfycatEndpoint = Retrofit.Builder()
            .client(OkHttpClient())
            .baseUrl(BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
            .create(GfycatEndpoint::class.java)
    }

    private suspend fun authenticate(): GfycatAuthenticationResponse {
        val response = gfycatEndpoint.authenticate(GfycatAuthenticationRequest(clientId, clientSecret))
        val expiryTimePadding = 10000
        response.expiryTimeUnix = System.currentTimeMillis() + response.expiresIn.secondsToMillis() - expiryTimePadding
        cachedGfycatAuthResponse = response
        
        return response
    }

    private suspend fun getAuthenticatedAccessToken(): String {
        cachedGfycatAuthResponse?.let {
            if (System.currentTimeMillis() < it.expiryTimeUnix) {
                return it.accessToken
            }
        }
    
        return authenticate().accessToken
    } 

    suspend fun getGfycat(gfycatName: String): GfycatItem {
        cachedGfycatItems[gfycatName]?.let {
            Log.i(TAG, "Returning cached ${name<GfycatItem>()} for gfycatName = $gfycatName")
            return it
        }

        gfycatEndpoint.getGfycatItem(getAuthenticatedAccessToken(), gfycatName).gfycatItem.also {
            cachedGfycatItems[gfycatName] = it
            Log.i(TAG, "Returning network ${name<GfycatItem>()} for gfycatName = $gfycatName")
            return it
        }
    }
}