package com.devgary.contentlinkapi.content

import com.devgary.contentcore.model.content.Content

interface ContentLinkHandler {
    fun canHandleLink(url: String): Boolean
    suspend fun getContent(url: String): Content?
}