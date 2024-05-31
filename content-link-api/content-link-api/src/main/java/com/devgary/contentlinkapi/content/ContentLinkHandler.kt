package com.devgary.contentlinkapi.content

import com.devgary.contentcore.model.content.Content

interface ContentLinkHandler {
    /**
     * Whether this [ContentLinkHandler] has the ability to convert [url] into a [Content] item. 
     * 
     * @see getContent
     */
    fun canHandleLink(url: String): Boolean

    /**
     * Converts [url] into [Content] item, most likely by making some HTTP Request.
     * 
     * @return [Content] null if url was unable to be converted
     */
    suspend fun getContent(url: String): ContentResult
}