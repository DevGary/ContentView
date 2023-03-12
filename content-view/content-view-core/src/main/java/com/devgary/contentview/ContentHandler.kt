package com.devgary.contentview

import android.view.View
import com.devgary.contentcore.model.content.Content
import com.devgary.contentview.model.ScaleType

interface ContentHandler {
    /**
     * Whether this [ContentHandler] has the ability to show [content]
     */
    fun canShowContent(content: Content): Boolean
    
    fun showContent(content: Content)

    /**
     * The [View] where shown [Content] will be displayed
     */
    fun getView(): View
    
    fun setViewVisibility(visibility: Int)
    
    fun setViewScaleType(scaleType: ScaleType)
}