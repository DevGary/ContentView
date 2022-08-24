package com.devgary.contentlinkapi.components.imgur

import com.devgary.contentcore.model.content.components.ContentSource
import com.devgary.contentcore.model.content.components.ContentType
import com.devgary.contentcore.model.content.CollectionContent
import com.devgary.contentcore.model.content.Content
import com.devgary.contentcore.util.containsIgnoreCase
import com.devgary.contentcore.util.isNotNullOrBlank
import com.devgary.contentlinkapi.components.imgur.api.ImgurClient
import com.devgary.contentlinkapi.components.imgur.api.ImgurEndpoint
import com.devgary.contentlinkapi.components.imgur.api.model.ImgurImage
import com.devgary.contentlinkapi.content.ContentLinkException
import com.devgary.contentlinkapi.content.ContentLinkHandler
import com.devgary.contentlinkapi.util.LinkUtils

class ImgurContentLinkHandler(
    private val authorizationHeader: String,
    private val mashapeKey: String,
) : ContentLinkHandler {
    private val imgurApi: ImgurClient by lazy {
        ImgurClient(ImgurEndpoint.create(
            authorizationHeader = authorizationHeader,
            mashapeKey = mashapeKey
        ))
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

            return CollectionContent(ContentSource.Url(url), collection)
        }
    }

    private fun mapImgurImageToContent(imgurImage: ImgurImage): Content {
        return if (imgurImage.mp4Url.isNotNullOrBlank()) {
            Content(ContentSource.Url(imgurImage.mp4Url!!), ContentType.VIDEO)
        } else {
            Content(ContentSource.Url(imgurImage.url), ContentType.IMAGE)
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