package com.devgary.contentlinkapi.handlers.streamable

import com.devgary.contentcore.model.content.components.ContentSource
import com.devgary.contentcore.model.content.components.ContentType
import com.devgary.contentcore.model.content.Content
import com.devgary.contentcore.util.equalsIgnoreCase
import com.devgary.contentcore.util.runIfLazyInitialized
import com.devgary.contentlinkapi.handlers.interfaces.ClearableMemory
import com.devgary.contentlinkapi.handlers.streamable.api.StreamableClient
import com.devgary.contentlinkapi.handlers.streamable.api.StreamableEndpoint
import com.devgary.contentlinkapi.content.ContentLinkException
import com.devgary.contentlinkapi.content.ContentLinkHandler
import com.devgary.contentlinkapi.util.LinkUtils.parseDomainFromUrl

class StreamableContentLinkHandler : ContentLinkHandler, ClearableMemory {
    private val streamableClient: StreamableClient by lazy {
        StreamableClient(StreamableEndpoint.create())
    }

    override fun canHandleLink(url: String): Boolean {
        return url.parseDomainFromUrl().equalsIgnoreCase("streamable.com")
    }

    override suspend fun getContent(url: String): Content {
        val shortCodeFromUrl = parseShortcodeFromUrl(url)
        
        if (!shortCodeFromUrl.isNullOrEmpty()) {
            val response = streamableClient.getVideo(shortCodeFromUrl)
            response.let {
                val video = response.videos
                    .filter { v -> v.url.isNotEmpty() }
                    .minByOrNull { v -> v.bitrate }

                video?.let {
                    return Content(ContentSource.Url(video.url), ContentType.VIDEO)
                }
            }
        }
        else {
            streamableClient.parseVideoUrlFromWebpage(url)?.let { 
                return Content(ContentSource.Url(it), ContentType.VIDEO)
            }
        }

        throw ContentLinkException("Could not parse content from url $url")
    }

    private fun parseShortcodeFromUrl(url: String): String? =
        StreamableLinkParser.parse(url).shortCode

    override fun clearMemory() {
        this::streamableClient.runIfLazyInitialized { 
            streamableClient.clearMemory()
        }
    }
}