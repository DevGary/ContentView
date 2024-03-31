package com.devgary.contentlinkapi.handlers

import com.devgary.contentcore.model.content.Content
import com.devgary.contentcore.model.content.components.ContentSource
import com.devgary.contentcore.model.content.components.ContentType
import com.devgary.contentlinkapi.content.ContentLinkException
import com.devgary.contentlinkapi.content.ContentLinkHandler
import com.devgary.contentlinkapi.content.ContentResult

/**
 * [ContentLinkHandler] that tries to create [Content] from url by parsing the url.
 * Intended for when no other [ContentLinkHandler] could handle url.
 *
 * @param defaultContentType [ContentType] to use if one could not be guessed from url.
 * The default value is null which will result in [canHandleLink] returning false and
 * [getContent] returning null if [Content] could not be guessed.
 */
class FallthroughContentLinkHandler(private val defaultContentType: ContentType? = null) : ContentLinkHandler {
    override fun canHandleLink(url: String): Boolean {
        if (url.endsWith(".mp4")) return true
        else if (url.endsWith(".gif")) return true
        else if (url.endsWith(".png")) return true
        else if (url.endsWith(".jpg")) return true
        else if (defaultContentType != null) return true
        return false
    }

    override suspend fun getContent(url: String): ContentResult {
        val content = if (url.endsWith(".mp4")) {
            Content(ContentSource.Url(url), ContentType.VIDEO)
        } else if (url.endsWith(".gif")) {
            Content(ContentSource.Url(url), ContentType.GIF)
        } else if (url.endsWith(".jpg") || url.endsWith(".png")) {
            Content(ContentSource.Url(url), ContentType.IMAGE)
        } else if (defaultContentType != null) {
            Content(ContentSource.Url(url), defaultContentType)
        } else {
            null
        }
        
        return content?.let { 
            ContentResult.Success(it)
        } ?: run { 
            ContentResult.Failure(ContentLinkException("Could not handle content: $url"))
        }
    }
}