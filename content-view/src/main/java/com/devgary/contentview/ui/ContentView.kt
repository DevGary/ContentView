package com.devgary.contentview.ui

import android.content.Context
import android.util.AttributeSet
import com.devgary.contentview.AbstractContentHandlerView
import com.devgary.contentview.ContentHandler
import com.devgary.contentview.ui.collection.CollectionContentHandler
import com.devgary.contentview.image.ImageContentHandler
import com.devgary.contentview.video.exomedia.VideoContentHandler

class ContentView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
) : AbstractContentHandlerView(context, attrs, defStyleAttr) {
    override fun provideContentHandlers(): List<ContentHandler> {
        return listOf(
            ImageContentHandler(context),
            VideoContentHandler(context),
            CollectionContentHandler(context),
        )
    }
}