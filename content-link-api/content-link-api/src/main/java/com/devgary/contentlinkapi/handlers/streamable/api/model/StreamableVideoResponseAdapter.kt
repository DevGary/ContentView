package com.devgary.contentlinkapi.handlers.streamable.api.model

import com.devgary.contentlinkapi.handlers.ApiException
import com.devgary.contentlinkapi.util.LinkUtils.prependHttpsIfMissing
import com.squareup.moshi.*

class StreamableVideoResponseAdapter {
    @FromJson
    fun fromJson(reader: JsonReader): StreamableVideoResponse {
        val moshi: Moshi = Moshi.Builder().build()

        val responseKeys: JsonReader.Options = JsonReader.Options.of("files")
        val fileKeys: JsonReader.Options = JsonReader.Options.of("mp4", "mp4-mobile")
        
        val responseAdapter: JsonAdapter<StreamableVideoResponse> = moshi.adapter(StreamableVideoResponse::class.java)
        val videoAdapter: JsonAdapter<StreamableVideo> = moshi.adapter(StreamableVideo::class.java)

        val streamableVideoEndpointResponse = responseAdapter.fromJson(reader.peekJson())
        
        streamableVideoEndpointResponse?.apply {
            thumbnailUrl = thumbnailUrl?.prependHttpsIfMissing()
            var mp4Video: StreamableVideo? = null
            var mp4MobileVideo: StreamableVideo? = null

            reader.beginObject()
            while (reader.hasNext()) {
                when (reader.selectName(responseKeys)) {
                    // reads "files" json object
                    0 -> { 
                        reader.beginObject()
                        while (reader.hasNext()) {
                            // reads various StreamableVideo objects in "files" json object
                            when (reader.selectName(fileKeys)) { 
                                0 -> mp4Video = videoAdapter.fromJsonValue(reader.readJsonValue())
                                1 -> mp4MobileVideo = videoAdapter.fromJsonValue(reader.readJsonValue())
                                -1 -> {
                                    // Unknown, skip
                                    reader.skipValue()
                                }
                            }
                        }
                        reader.endObject()
                    }
                    -1 -> {
                        // Unknown, skip
                        reader.skipName()
                        reader.skipValue()
                    }
                }
            }
            reader.endObject()

            mp4Video?.let {
                videos.add(mp4Video)
            }

            mp4MobileVideo?.let {
                videos.add(mp4MobileVideo)
            }

            return this
        }

        throw ApiException("Error parsing response from Streamable Video Endpoint")
    }
}
