package com.devgary.contentview.content

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.widget.FrameLayout
import androidx.core.view.children
import com.devgary.contentcore.model.Content
import com.devgary.contentcore.util.TAG
import com.devgary.contentcore.util.classNameWithValue
import com.devgary.contentcore.util.name

abstract class ContentHandlerView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
) : FrameLayout(context, attrs, defStyleAttr), ContentHandler {

    private val contentHandlers = mutableListOf<ContentHandler>()

    init {
        registerContentHandlers()
    }

    private fun registerContentHandlers() {
        for (providedContentHandler in provideContentHandlers()) {
            contentHandlers.add(providedContentHandler)
        }
    }
    
    abstract fun provideContentHandlers(): List<ContentHandler>

    override fun getView() = this

    override fun setViewVisibility(visibility: Int) {
        this.visibility = visibility
    }

    override fun showContent(content: Content) {
        val firstContentHandlerForContent = contentHandlers.firstOrNull { handler ->
            handler.canShowContent(content)
        }

        firstContentHandlerForContent?.let { handler ->
            setContentHandlerViewVisibility(GONE)
            addContentHandlerViewIfNotAdded(handler)
            handler.showContent(content) 
        } ?: run { 
            Log.e(TAG, "No ${name<ContentHandler>()} found for ${classNameWithValue(content)}") 
        }
    }

    override fun canShowContent(content: Content): Boolean {
        return contentHandlers.any { handler -> handler.canShowContent(content) }
    }

    private fun setContentHandlerViewVisibility(visibility: Int) {
        contentHandlers.forEach { handler -> handler.setViewVisibility(visibility) }
    }

    private fun addContentHandlerViewIfNotAdded(handler: ContentHandler) {
        val contentHandlerView = handler.getView()
        
        if (!children.contains(contentHandlerView)) {
            addView(contentHandlerView)
        }
    }
}