package com.devgary.contentlinkapi.api.gfycat

import com.devgary.contentcore.util.secondsToMillis
import com.devgary.contentlinkapi.api.ApiException
import com.devgary.contentlinkapi.api.gfycat.model.GfycatAuthenticationRequest
import com.devgary.contentlinkapi.api.gfycat.model.GfycatAuthenticationResponse
import com.devgary.contentlinkapi.api.gfycat.model.GfycatItem
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

class GfycatApi(
    private val clientId: String,
    private val clientSecret: String,
) {
    private val gfycatEndpoint: GfycatEndpoint
    private val BASE_URL = "https://api.gfycat.com/v1/"

    var lastSuccessfulGfycatAuthResponse: GfycatAuthenticationResponse? = null
    private val gfycatNameToGfycatItems: MutableMap<String, GfycatItem> by lazy { HashMap() }
    
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
        lastSuccessfulGfycatAuthResponse = response
        
        return response
    }

    private suspend fun getAuthenticatedAccessToken(): String {
        lastSuccessfulGfycatAuthResponse?.let {
            if (System.currentTimeMillis() < it.expiryTimeUnix) {
                return it.accessToken
            }
        }
    
        return authenticate().accessToken
    } 

    suspend fun getGfycat(url: String): GfycatItem {
        gfycatNameToGfycatItems[url]?.let { 
            return it
        }

        val parsedGfycatName: String? = GfycatUtils.parseGfycatNameFromUrl(url)

        if (parsedGfycatName.isNullOrEmpty().not()) {
            getGfycatItem(parsedGfycatName!!).also {
                gfycatNameToGfycatItems[parsedGfycatName] = it
                return it
            }
        }
        else {
            throw ApiException("Could not parse gfycat name from gfycat url $url")
        }
    }

    private suspend fun getGfycatItem(gfycatName: String): GfycatItem {
        return gfycatEndpoint.getGfycatItem(getAuthenticatedAccessToken(), gfycatName).gfycatItem
    }
}