package com.devgary.contentlinkapi.handlers.gfycat.api.model

import com.squareup.moshi.Json

data class GfycatItem (
    @Json(name = "gfyId") var gfyId: String,
    @Json(name = "gfyName") var gfyName: String,
    @Json(name = "gfyNumber") var gfyNumber: String?,
    @Json(name = "task") var task: String?,
    @Json(name = "webmUrl") var webmUrl: String?,
    @Json(name = "gifUrl") var gifUrl: String?,
    @Json(name = "mobileUrl") var mobileUrl: String?,
    @Json(name = "mobilePosterUrl") var mobilePosterUrl: String?,
    @Json(name = "miniUrl") var miniUrl: String?,
    @Json(name = "miniPosterUrl") var miniPosterUrl: String?,
    @Json(name = "posterUrl") var posterUrl: String?,
    @Json(name = "thumb360Url") var thumb360Url: String?,
    @Json(name = "thumb360PosterUrl") var thumb360PosterUrl: String?,
    @Json(name = "thumb100PosterUrl") var thumb100PosterUrl: String?,
    @Json(name = "max5mbGif") var max5mbGif: String?,
    @Json(name = "max2mbGif") var max2mbGif: String?,
    @Json(name = "max1mbGif") var max1mbGif: String?,
    @Json(name = "gif100px") var gif100px: String?,
    @Json(name = "mjpgUrl") var mjpgUrl: String?,
    @Json(name = "width") var width: Int?,
    @Json(name = "height") var height: Int?,
    @Json(name = "avgColor") var avgColor: String?,
    @Json(name = "frameRate") var frameRate: Double?,
    @Json(name = "numFrames") var numFrames: Int?,
    @Json(name = "mp4Size") var mp4Size: Int?,
    @Json(name = "webmSize") var webmSize: Int?,
    @Json(name = "gifSize") var gifSize: Int?,
    @Json(name = "source") var source: Int?,
    @Json(name = "createDate") var createDate: Int?,
    @Json(name = "nsfw") var nsfw: String?,
    @Json(name = "mp4Url") var mp4Url: String?,
    @Json(name = "likes") var likes: String?,
    @Json(name = "published") var published: Int?,
    @Json(name = "dislikes") var dislikes: String?,
    @Json(name = "extraLemmas") var extraLemmas: String?,
    @Json(name = "md5") var md5: String?,
    @Json(name = "views") var views: Int?,
    @Json(name = "tags") var tags: List<String>?,
    @Json(name = "userName") var userName: String?,
    @Json(name = "title") var title: String?,
    @Json(name = "description") var description: String?,
    @Json(name = "languageText") var languageText: String?,
    @Json(name = "languageCategories") var languageCategories: Any?,
    @Json(name = "subreddit") var subreddit: String?,
    @Json(name = "redditId") var redditId: String?,
    @Json(name = "redditIdText") var redditIdText: String?,
    @Json(name = "domainWhitelist") var domainWhitelist: List<Any>?,
)