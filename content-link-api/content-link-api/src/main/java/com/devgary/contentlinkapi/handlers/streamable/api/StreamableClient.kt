package com.devgary.contentlinkapi.handlers.streamable.api

import android.util.Log
import androidx.collection.LruCache
import com.devgary.contentcore.util.*
import com.devgary.contentlinkapi.Constants
import com.devgary.contentlinkapi.handlers.ApiException
import com.devgary.contentlinkapi.util.webpageparser.WebpageParser
import com.devgary.contentlinkapi.handlers.streamable.api.model.StreamableVideoResponse

class StreamableClient(private val streamableEndpoint: StreamableEndpoint) {
    private val cachedStreamableVideoResponses: LruCache<String, StreamableVideoResponse>
            by lazy { LruCache(/* maxSize = */ Constants.LRU_CACHE_DEFAULT_SIZE) }
    
    suspend fun getVideo(shortCode: String): StreamableVideoResponse {
        cachedStreamableVideoResponses[shortCode]?.let {
            Log.i(TAG, "Returning cached ${name<StreamableVideoResponse>()} for shortCode = $shortCode")
            return it
        }

        val response = try {
            streamableEndpoint.getVideo(shortCode)
        } catch (e: Exception) {
            throw ApiException(e.message.orEmpty())
        }
        
        Log.i(TAG,"Returning network ${name<StreamableVideoResponse>()} for shortCode = $shortCode")

        cachedStreamableVideoResponses.put(shortCode, response)
        return response
    }

    suspend fun parseVideoUrlFromWebpage(url: String): String? {
        val videoUrl = WebpageParser.parseVideoUrlFromWebpage(url)
        
        return videoUrl.nullIfBlank()
    }

    fun clearMemory() {
        this::cachedStreamableVideoResponses.runIfLazyInitialized {
            Log.i(TAG,"Cleared ${cachedStreamableVideoResponses.size()} ${name<StreamableVideoResponse>()} from memory cache")
            cachedStreamableVideoResponses.evictAll()
        }
    }
}