package com.devgary.contentlinkapi.handlers.imgur

import com.devgary.contentcore.model.content.components.ContentSource
import com.devgary.contentcore.model.content.components.ContentType
import com.devgary.contentcore.model.content.CollectionContent
import com.devgary.contentcore.model.content.Content
import com.devgary.contentcore.util.containsIgnoreCase
import com.devgary.contentcore.util.isNotNullOrBlank
import com.devgary.contentcore.util.runIfLazyInitialized
import com.devgary.contentlinkapi.handlers.imgur.api.ImgurClient
import com.devgary.contentlinkapi.handlers.imgur.api.ImgurEndpoint
import com.devgary.contentlinkapi.handlers.imgur.api.model.ImgurImage
import com.devgary.contentlinkapi.handlers.interfaces.ClearableMemory
import com.devgary.contentlinkapi.content.ContentLinkHandler
import com.devgary.contentlinkapi.content.ContentResult
import com.devgary.contentlinkapi.content.ResponseDidNotContainContentException
import com.devgary.contentlinkapi.util.LinkUtils

class ImgurContentLinkHandler(
    private val authorizationHeader: String,
    private val mashapeKey: String,
) : ContentLinkHandler, ClearableMemory {
    private val imgurClient: ImgurClient by lazy {
        ImgurClient(ImgurEndpoint.create(
            authorizationHeader = authorizationHeader,
            mashapeKey = mashapeKey
        ))
    }

    override fun canHandleLink(url: String): Boolean {
        return parseImgurAlbumIdFromUrl(url).isNotNullOrBlank()
    }

    override suspend fun getContent(url: String): ContentResult {
        try {
            val imgurAlbumId = parseImgurAlbumIdFromUrl(url)

            if (imgurAlbumId.isNullOrEmpty()) {
                return ContentResult.Failure(ResponseDidNotContainContentException())
            }
            
            val response = imgurClient.getImgur(imgurAlbumId)
            val collection = response.images.map { imgurImage ->
                mapImgurImageToContent(imgurImage)
            }

            val content = CollectionContent(ContentSource.Url(url), collection)
            return ContentResult.Success(content)
        } catch (e: Exception) {
            return ContentResult.Failure(e)
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

    override fun clearMemory() {
        this::imgurClient.runIfLazyInitialized {
            imgurClient.clearMemory()
        }
    }
}