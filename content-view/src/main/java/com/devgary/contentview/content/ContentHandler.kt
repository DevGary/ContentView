package com.devgary.contentview.content

import com.devgary.contentcore.model.Content

interface ContentHandler {
    fun setViewVisibility(visibility: Int)
    fun showContent(content: Content)
}