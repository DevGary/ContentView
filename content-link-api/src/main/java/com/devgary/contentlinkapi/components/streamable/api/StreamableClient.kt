package com.devgary.contentlinkapi.components.streamable.api

import android.util.Log
import com.devgary.contentcore.util.TAG
import com.devgary.contentcore.util.name
import com.devgary.contentlinkapi.components.streamable.api.model.StreamableVideoResponse

class StreamableClient(private val streamableEndpoint: StreamableEndpoint) {
    private val cachedStreamableVideoResponses: MutableMap<String, StreamableVideoResponse> = HashMap()

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