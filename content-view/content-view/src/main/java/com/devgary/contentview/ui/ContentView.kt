package com.devgary.contentview.ui

import android.content.Context
import android.util.AttributeSet
import com.devgary.contentview.AbstractCompositeContentHandlerView
import com.devgary.contentview.ContentHandler
import com.devgary.contentview.ui.collection.CollectionContentHandler
import com.devgary.contentview.image.ImageContentHandler
import com.devgary.contentview.video.VideoContentHandler

class ContentView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
) : AbstractCompositeContentHandlerView(context, attrs, defStyleAttr) {
    override fun createContentHandlers(): List<ContentHandler> {
        return listOf(
            ImageContentHandler(context),
            VideoContentHandler(context),
            CollectionContentHandler(context),
        )
    }
}