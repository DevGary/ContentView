package com.devgary.contentlinkapi.components.gfycat

import com.devgary.contentcore.model.Content
import com.devgary.contentcore.model.ContentType
import com.devgary.contentlinkapi.components.gfycat.api.GfycatClient
import com.devgary.contentlinkapi.content.ContentLinkException
import com.devgary.contentlinkapi.content.ContentLinkHandler
import com.devgary.contentlinkapi.util.LinkUtils

class GfycatContentLinkHandler(
    private val clientId: String,
    private val clientSecret: String,
) : ContentLinkHandler {
    private val gfycatApi: GfycatClient by lazy { 
        GfycatClient(clientId = clientId, clientSecret = clientSecret) 
    }

    override fun canHandleLink(url: String): Boolean {
        return parseGfycatNameFromUrl(url).isNullOrEmpty().not()
    }

    override suspend fun getContent(url: String): Content {
        val gfycatName = parseGfycatNameFromUrl(url)
        
        if (gfycatName.isNullOrEmpty()) {
            throw ContentLinkException("Could not parse GfycatName from url $url")
        }
        
        val response = gfycatApi.getGfycat(gfycatName)
        response.let {
            response.mp4Url?.let {
                return Content(it, ContentType.VIDEO)
            }
        }

        throw ContentLinkException("Could not parse content from url $url")
    }

    private fun parseGfycatNameFromUrl(url: String): String? =
        LinkUtils.parseAlphabeticalIdFromUrl(url, "gfycat.com/")
}