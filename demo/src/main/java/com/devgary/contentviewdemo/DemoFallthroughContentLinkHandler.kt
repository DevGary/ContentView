package com.devgary.contentviewdemo

import com.devgary.contentcore.model.content.Content
import com.devgary.contentcore.model.content.components.ContentSource
import com.devgary.contentcore.model.content.components.ContentType
import com.devgary.contentcore.util.containsIgnoreCase
import com.devgary.contentlinkapi.handlers.FallthroughContentLinkHandler
import com.devgary.contentlinkapi.content.ContentLinkHandler
import com.devgary.contentlinkapi.content.ContentResult

/**
 * Example of creating your own [ContentLinkHandler] via composition
 * 
 * Intended for when no other [ContentLinkHandler] could handle url. Handles some extra
 * urls but mostly fallbacks on using [FallthroughContentLinkHandler]
 */
class DemoFallthroughContentLinkHandler : ContentLinkHandler {
    private val fallthroughContentLinkHandler by lazy { FallthroughContentLinkHandler() }
    
    private val urlContentType = mapOf(
        "images.unsplash.com" to ContentType.IMAGE
    )
    
    override fun canHandleLink(url: String): Boolean {
        if (urlContentType.keys.any { match -> url.containsIgnoreCase(match) }) return true
        return fallthroughContentLinkHandler.canHandleLink(url)
    }

    override suspend fun getContent(url: String): ContentResult {
        val urlContentType = urlContentType
            .firstNotNullOfOrNull { (url, contentType) ->
                contentType.takeIf { url.containsIgnoreCase(url) }
            }
        
        urlContentType?.let {
            val content = Content(source = ContentSource.Url(url), type = it)
            return ContentResult.Success(content)
        }

        return fallthroughContentLinkHandler.getContent(url)
    }
}