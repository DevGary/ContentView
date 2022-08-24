package com.devgary.contentlinkapi.content

import android.util.Log
import com.devgary.contentcore.model.content.Content
import com.devgary.contentcore.util.TAG
import com.devgary.contentcore.util.name

abstract class AbstractContentLinkApi : ContentLinkHandler {
    private val contentHandlers = mutableListOf<ContentLinkHandler>()

    init {
        registerContentHandlers()
    }

    private fun registerContentHandlers() {
        for (providedContentHandler in provideContentHandlers()) {
            contentHandlers.add(providedContentHandler)
        }
    }

    abstract fun provideContentHandlers(): List<ContentLinkHandler>

    override fun canHandleLink(url: String): Boolean {
        return contentHandlers.any { handler -> handler.canHandleLink(url) }
    }

    override suspend fun getContent(url: String): Content? {
        val firstContentLinkHandlerForUrl = contentHandlers.firstOrNull { handler ->
            handler.canHandleLink(url)
        }

        firstContentLinkHandlerForUrl?.let { handler ->
            return handler.getContent(url)
        } ?: run {
            Log.e(TAG, "No ${name<ContentLinkHandler>()} found for url = $url")
        }
        
        return null
    }
}