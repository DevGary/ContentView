package com.devgary.contentlinkapi.components.gfycat.api

import android.util.Log
import com.devgary.contentcore.util.TAG
import com.devgary.contentcore.util.name
import com.devgary.contentcore.util.secondsToMillis
import com.devgary.contentlinkapi.components.gfycat.api.model.GfycatAuthenticationRequest
import com.devgary.contentlinkapi.components.gfycat.api.model.GfycatAuthenticationResponse
import com.devgary.contentlinkapi.components.gfycat.api.model.GfycatItem

class GfycatClient(
    private val clientId: String,
    private val clientSecret: String,
    private val gfycatEndpoint: GfycatEndpoint,
) {

    private var cachedGfycatAuthResponse: GfycatAuthenticationResponse? = null
    private val cachedGfycatItems: MutableMap<String, GfycatItem> by lazy { HashMap() }
    
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