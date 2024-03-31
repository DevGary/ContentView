package com.devgary.contentlinkapi.content

open class ContentLinkException(message: String) : Exception(message)

class ResponseDidNotContainContentException(message: String? = null) :
    ContentLinkException(message = message ?: "Response did not contain expected content")