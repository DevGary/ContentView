package com.devgary.contentview.content

import android.content.Context
import android.util.AttributeSet
import com.devgary.contentview.components.image.ImageContentHandler
import com.devgary.contentview.components.video.VideoContentHandler

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