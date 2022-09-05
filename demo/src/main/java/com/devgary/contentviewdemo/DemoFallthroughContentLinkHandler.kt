package com.devgary.contentviewdemo

import com.devgary.contentcore.model.content.Content
import com.devgary.contentcore.model.content.components.ContentSource
import com.devgary.contentcore.model.content.components.ContentType
import com.devgary.contentcore.util.containsIgnoreCase
import com.devgary.contentlinkapi.handlers.FallthroughContentLinkHandler
import com.devgary.contentlinkapi.content.ContentLinkHandler

/**
 * Example of creating your own [ContentLinkHandler] via composition
 * 
 * Intended for when no other [ContentLinkHandler] could handle url. Handles some extra
 * urls but mostly fallbacks on using [FallthroughContentLinkHandler]
 */
class DemoFallthroughContentLinkHandler : ContentLinkHandler {
    private val fallthroughContentLinkHandler by lazy { FallthroughContentLinkHandler() }
    
    private val matchToContentType = mapOf(
        "images.unsplash.com" to ContentType.IMAGE
    )
    
    override fun canHandleLink(url: String): Boolean {
        if (matchToContentType.keys.any { match -> url.containsIgnoreCase(match) }) return true
        return fallthroughContentLinkHandler.canHandleLink(url)
    }

    override suspend fun getContent(url: String): Content? {
        matchToContentType.keys.firstOrNull { match -> url.containsIgnoreCase(match) }?.let { key ->
            return Content(ContentSource.Url(url), matchToContentType[key]!!)
        }
        return fallthroughContentLinkHandler.getContent(url)
    }
}