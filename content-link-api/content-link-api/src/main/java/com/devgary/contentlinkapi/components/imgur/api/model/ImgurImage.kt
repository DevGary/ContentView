package com.devgary.contentlinkapi.components.imgur.api.model

import com.squareup.moshi.Json

/**
 * The base model for an image from Imgur endpoint.
 *
 * Note: Despite the name, the content does not always have to be a still image.
 * It can be an animated gif or a video.
 */
data class ImgurImage(
    @Json(name = "id")
    val id: String,
    @Json(name = "link")
    val url: String,
    @Json(name = "width")
    val width: Int,
    @Json(name = "height")
    val height: Int,
    @Json(name = "type")
    val type: String,
    @Json(name = "animated")
    val animated: Boolean,
    @Json(name = "gifv")
    /**
     * Only available if the image is animated and type is 'image/gif'
     */
    val gifvUrl: String?,
    /**
     * Only available if the image is animated and type is 'image/gif'.
     */
    @Json(name = "mp4")
    val mp4Url: String?,
)