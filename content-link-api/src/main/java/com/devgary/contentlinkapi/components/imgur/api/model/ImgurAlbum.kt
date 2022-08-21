package com.devgary.contentlinkapi.components.imgur.api.model

import com.squareup.moshi.Json

data class ImgurAlbum(
    @Json(name = "id")
    val id: String,
    @Json(name = "cover")
    val coverImageId: String,
    @Json(name = "images_count")
    val albumSize: Int,
    @Json(name = "images")
    var images: List<ImgurImage>,
)