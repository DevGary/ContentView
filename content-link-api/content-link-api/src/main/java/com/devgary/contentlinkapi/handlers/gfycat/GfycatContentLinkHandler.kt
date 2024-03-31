package com.devgary.contentlinkapi.handlers.gfycat

import com.devgary.contentcore.model.content.Content
import com.devgary.contentcore.model.content.DefaultContentCreator
import com.devgary.contentcore.util.runIfLazyInitialized
import com.devgary.contentlinkapi.handlers.gfycat.api.GfycatClient
import com.devgary.contentlinkapi.handlers.gfycat.api.GfycatEndpoint
import com.devgary.contentlinkapi.handlers.interfaces.ClearableMemory
import com.devgary.contentlinkapi.content.ContentLinkException
import com.devgary.contentlinkapi.content.ContentLinkHandler
import com.devgary.contentlinkapi.util.LinkUtils

class GfycatContentLinkHandler(
    private val clientId: String,
    private val clientSecret: String,
) : ContentLinkHandler, ClearableMemory {
    private val gfycatClient: GfycatClient by lazy { 
        GfycatClient(
            clientId = clientId, 
            clientSecret = clientSecret,
            gfycatEndpoint = GfycatEndpoint.create()
        ) 
    }

    override fun canHandleLink(url: String): Boolean {
        return parseGfycatNameFromUrl(url).isNullOrEmpty().not()
    }

    override suspend fun getContent(url: String): Content {
        val gfycatName = parseGfycatNameFromUrl(url)
        
        if (gfycatName.isNullOrEmpty()) {
            throw ContentLinkException("Could not parse GfycatName from url $url")
        }
        
        val response = gfycatClient.getGfycat(gfycatName)
        response.let {
            response.mp4Url?.let {
                return DefaultContentCreator.createVideoContent(videoUrl = it, thumbnailUrl = response.posterUrl)
            }
        }

        throw ContentLinkException("Could not parse content from url $url")
    }

    private fun parseGfycatNameFromUrl(url: String): String? =
        LinkUtils.parseAlphabeticalIdFromUrl(url, "gfycat.com/")

    override fun clearMemory() {
        this::gfycatClient.runIfLazyInitialized {
            gfycatClient.clearMemory()
        }
    }
}