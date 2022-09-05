package com.devgary.contentlinkapi.handlers.streamable

import com.devgary.contentlinkapi.util.LinkUtils

class StreamableLinkParser {
    data class StreamableLinkInfo(val shortCode: String?)
    
    companion object {
        fun parse(url: String): StreamableLinkInfo {
            LinkUtils.parseAlphanumericIdFromUrl(
                url = url, 
                startFromOccurrence = "streamable.com/s/", 
                minLength = 3
            )?.let {
                return StreamableLinkInfo(shortCode = it)
            }

            LinkUtils.parseAlphanumericIdFromUrl(
                url = url,
                startFromOccurrence = "streamable.com/",
                minLength = 3
            )?.let {
                return StreamableLinkInfo(shortCode = it)
            }

            return StreamableLinkInfo(null)
        }
    }
}