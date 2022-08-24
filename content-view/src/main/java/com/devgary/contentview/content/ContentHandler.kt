package com.devgary.contentview.content

import android.view.View
import com.devgary.contentcore.model.content.Content

interface ContentHandler {
    fun getView(): View
    fun setViewVisibility(visibility: Int)
    fun showContent(content: Content)
    fun canShowContent(content: Content): Boolean
}