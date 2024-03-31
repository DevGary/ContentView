package com.devgary.contentlinkapi.content

import android.util.Log
import com.devgary.contentcore.model.content.Content
import com.devgary.contentcore.util.logExecutedFunction
import com.devgary.contentcore.util.TAG
import com.devgary.contentcore.util.name
import com.devgary.contentlinkapi.handlers.interfaces.ClearableMemory

abstract class AbstractCompositeContentLinkHandler : CompositeContentLinkHandler {
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

    override suspend fun getContent(url: String): ContentResult {
        val firstContentLinkHandlerForUrl = contentHandlers.firstOrNull { handler ->
            handler.canHandleLink(url)
        }

        return firstContentLinkHandlerForUrl?.let { handler ->
            handler.getContent(url)
        } ?: run {
            val errorString = "No ${name<ContentLinkHandler>()} found for url = $url"
            Log.e(TAG, errorString)
            
            return ContentResult.Failure(ContentLinkException(errorString))
        }
    }

    override fun clearMemory() {
        logExecutedFunction()
        
        contentHandlers.forEach {
            (it as? ClearableMemory)?.clearMemory()
        }
    }
}