package com.devgary.contentlinkapi.handlers.streamable.api.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class StreamableVideoResponse(
    var url: String,
    @Json(name = "thumbnail_url")
    var thumbnailUrl: String? = null,
    var title: String? = null,
    var videos: MutableList<StreamableVideo> = mutableListOf()
)