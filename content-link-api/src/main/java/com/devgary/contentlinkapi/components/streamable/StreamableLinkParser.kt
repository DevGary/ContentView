package com.devgary.contentlinkapi.components.streamable

import com.devgary.contentlinkapi.util.LinkUtils

class StreamableLinkParser {
    data class ParsedStreamableLink(val shortCode: String?)
    
    companion object {
        fun parse(url: String): ParsedStreamableLink {
            return ParsedStreamableLink(shortCode = LinkUtils.parseAlphanumericIdFromUrl(url, "streamable.com/"))
        }
    }
}