package com.devgary.contentview.ui

import android.content.Context
import android.util.AttributeSet
import com.devgary.contentview.content.ContentHandler
import com.devgary.contentview.content.ImageContentHandler
import com.devgary.contentview.content.VideoContentHandler

class ContentView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
) : ContentHandlerView(context, attrs, defStyleAttr) {
    override fun provideContentHandlers(): List<ContentHandler> {
        return listOf(
            ImageContentHandler(context),
            VideoContentHandler(context),
        )
    }
}