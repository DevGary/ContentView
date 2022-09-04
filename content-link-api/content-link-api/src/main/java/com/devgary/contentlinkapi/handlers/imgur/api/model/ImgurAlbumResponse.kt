package com.devgary.contentlinkapi.handlers.imgur.api.model

import com.squareup.moshi.Json

data class ImgurAlbumResponse(
    @Json(name = "data")
    val album: ImgurAlbum,
    @Json(name = "status")
    val status: Int,
    @Json(name = "success")
    val success: Boolean
)