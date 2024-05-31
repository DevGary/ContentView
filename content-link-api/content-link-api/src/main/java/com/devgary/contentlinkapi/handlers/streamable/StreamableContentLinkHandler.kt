package com.devgary.contentlinkapi.handlers.streamable

import com.devgary.contentcore.model.content.ActivatableContent
import com.devgary.contentcore.model.content.components.ContentSource
import com.devgary.contentcore.model.content.components.ContentType
import com.devgary.contentcore.model.content.Content
import com.devgary.contentcore.model.content.DefaultContentCreator
import com.devgary.contentcore.util.equalsIgnoreCase
import com.devgary.contentcore.util.runIfLazyInitialized
import com.devgary.contentlinkapi.handlers.interfaces.ClearableMemory
import com.devgary.contentlinkapi.handlers.streamable.api.StreamableClient
import com.devgary.contentlinkapi.handlers.streamable.api.StreamableEndpoint
import com.devgary.contentlinkapi.content.ContentLinkException
import com.devgary.contentlinkapi.content.ContentLinkHandler
import com.devgary.contentlinkapi.content.ContentResult
import com.devgary.contentlinkapi.content.ResponseDidNotContainContentException
import com.devgary.contentlinkapi.util.LinkUtils.parseDomainFromUrl

class StreamableContentLinkHandler : ContentLinkHandler, ClearableMemory {
    private val streamableClient: StreamableClient by lazy {
        StreamableClient(StreamableEndpoint.create())
    }

    override fun canHandleLink(url: String): Boolean {
        return url.parseDomainFromUrl().equalsIgnoreCase("streamable.com")
    }

    override suspend fun getContent(url: String): ContentResult {
        try {
            val shortCodeFromUrl = parseShortcodeFromUrl(url)

            if (!shortCodeFromUrl.isNullOrEmpty()) {
                val response = streamableClient.getVideo(shortCodeFromUrl)
                response.let {
                    val video = response.videos
                        .filter { v -> v.url.isNotEmpty() }
                        .minByOrNull { v -> v.bitrate }

                    video?.let {
                        val content = DefaultContentCreator.createVideoContent(
                            videoUrl = video.url,
                            thumbnailUrl = response.thumbnailUrl
                        )
                        return ContentResult.Success(content)
                    } ?: run {
                        return ContentResult.Failure(ResponseDidNotContainContentException())
                    }
                }
            } else {
                streamableClient.parseVideoUrlFromWebpage(url)?.let {
                    val videoContent = Content(ContentSource.Url(it), ContentType.VIDEO)
                    val thumbnailContent = Content(ContentSource.Empty, ContentType.IMAGE)
                    val activatableContent =
                        ActivatableContent(contentWhenNotActivated = thumbnailContent, contentWhenActivated = videoContent)
                    return ContentResult.Success(activatableContent)
                } ?: run {
                    return ContentResult.Failure(ContentLinkException("Response did not contain expected content"))
                }
            }
        } catch (e: Exception) {
            return ContentResult.Failure(e)
        }
    }

    private fun parseShortcodeFromUrl(url: String): String? =
        StreamableLinkParser.parse(url).shortCode

    override fun clearMemory() {
        this::streamableClient.runIfLazyInitialized { 
            streamableClient.clearMemory()
        }
    }
}