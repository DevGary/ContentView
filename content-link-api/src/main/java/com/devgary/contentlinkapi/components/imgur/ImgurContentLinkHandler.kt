package com.devgary.contentlinkapi.components.imgur

import com.devgary.contentcore.model.CollectionContent
import com.devgary.contentcore.model.Content
import com.devgary.contentcore.model.ContentType
import com.devgary.contentcore.util.containsIgnoreCase
import com.devgary.contentcore.util.isNotNullOrBlank
import com.devgary.contentlinkapi.components.imgur.api.ImgurClient
import com.devgary.contentlinkapi.components.imgur.api.model.ImgurImage
import com.devgary.contentlinkapi.content.ContentLinkException
import com.devgary.contentlinkapi.content.ContentLinkHandler
import com.devgary.contentlinkapi.util.LinkUtils

class ImgurContentLinkHandler(
    private val authorizationHeader: String,
    private val mashapeKey: String,
) : ContentLinkHandler {
    private val imgurApi: ImgurClient by lazy {
        ImgurClient(authorizationHeader = authorizationHeader, mashapeKey = mashapeKey)
    }

    override fun canHandleLink(url: String): Boolean {
        return parseImgurAlbumIdFromUrl(url).isNotNullOrBlank()
    }

    override suspend fun getContent(url: String): Content {
        val imgurAlbumId = parseImgurAlbumIdFromUrl(url)

        if (imgurAlbumId.isNullOrEmpty()) {
            throw ContentLinkException("Could not parse imgurAlbumId from url $url")
        }

        val response = imgurApi.getImgur(imgurAlbumId)
        response.let {
            val collection = it.images.map { imgurImage ->
                mapImgurImageToContent(imgurImage)
            }

            return CollectionContent(url, collection)
        }
    }

    private fun mapImgurImageToContent(imgurImage: ImgurImage): Content {
        return if (imgurImage.mp4Url.isNotNullOrBlank()) {
            Content(imgurImage.url, ContentType.VIDEO)
        } else {
            Content(imgurImage.url, ContentType.IMAGE)
        }
    }

    private fun parseImgurAlbumIdFromUrl(imgurAlbumUrl: String): String? {
        val suffix = if (imgurAlbumUrl.containsIgnoreCase("imgur.com/a/")) {
            "imgur.com/a/"
        } else if (imgurAlbumUrl.containsIgnoreCase("imgur.com/gallery/")) {
            "imgur.com/gallery/"
        } else if (imgurAlbumUrl.containsIgnoreCase("imgur.com/g/")) {
            "imgur.com/g/"
        } else {
            return null
        }

        return LinkUtils.parseAlphanumericIdFromUrl(url = imgurAlbumUrl, startFromOccurrence = suffix)
    }
}