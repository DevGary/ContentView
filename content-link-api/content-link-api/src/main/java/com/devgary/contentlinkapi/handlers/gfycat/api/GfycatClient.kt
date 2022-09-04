package com.devgary.contentlinkapi.handlers.gfycat.api

import android.util.Log
import androidx.collection.LruCache
import com.devgary.contentcore.util.TAG
import com.devgary.contentcore.util.name
import com.devgary.contentcore.util.runIfLazyInitialized
import com.devgary.contentcore.util.secondsToMillis
import com.devgary.contentlinkapi.Constants
import com.devgary.contentlinkapi.handlers.gfycat.api.model.GfycatAuthenticationRequest
import com.devgary.contentlinkapi.handlers.gfycat.api.model.GfycatAuthenticationResponse
import com.devgary.contentlinkapi.handlers.gfycat.api.model.GfycatItem

class GfycatClient(
    private val clientId: String,
    private val clientSecret: String,
    private val gfycatEndpoint: GfycatEndpoint,
) {
    private var cachedGfycatAuthResponse: GfycatAuthenticationResponse? = null
    private val cachedGfycatItems: LruCache<String, GfycatItem>
            by lazy { LruCache(/* maxSize = */ Constants.LRU_CACHE_DEFAULT_SIZE) }
    
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
            cachedGfycatItems.put(gfycatName, it)
            Log.i(TAG, "Returning network ${name<GfycatItem>()} for gfycatName = $gfycatName")
            return it
        }
    }

    fun clearMemory() {
        this::cachedGfycatItems.runIfLazyInitialized {
            Log.i(TAG,"Cleared ${cachedGfycatItems.size()} ${name<GfycatItem>()} from memory cache")
            cachedGfycatItems.evictAll()
        }
    }
}