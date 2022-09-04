package com.devgary.contentlinkapi.handlers.streamable.api.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class StreamableVideo(
    var url: String, 
    var bitrate: Long,
    var framerate: Int,
    var height: Int,
    var width: Int,
    @Json(name = "size")
    var sizeBytes: Long,
    @Json(name = "duration")
    var durationSeconds: Float,
)