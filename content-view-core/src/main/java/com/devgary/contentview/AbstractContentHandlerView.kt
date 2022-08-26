package com.devgary.contentview

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.widget.FrameLayout
import androidx.core.view.children
import com.devgary.contentcore.model.content.Content
import com.devgary.contentcore.util.TAG
import com.devgary.contentcore.util.classNameWithValue
import com.devgary.contentcore.util.name
import com.devgary.contentview.interfaces.Disposable
import com.devgary.contentview.interfaces.PlayPausable

abstract class AbstractContentHandlerView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
) : FrameLayout(context, attrs, defStyleAttr), ContentHandler, Disposable, PlayPausable {

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
        setContentHandlersVisibility(visibility)
    }
    
    private fun setContentHandlersVisibility(visibility: Int, excludedContentHandler: ContentHandler? = null) {
        contentHandlers
            .filter { handler -> handler != excludedContentHandler }
            .forEach { handler -> handler.setViewVisibility(visibility) }
    }

    override fun showContent(content: Content) {
        val firstContentHandlerForContent = contentHandlers.firstOrNull { handler ->
            handler.canShowContent(content)
        }
        setContentHandlersVisibility(GONE, excludedContentHandler = firstContentHandlerForContent)

        firstContentHandlerForContent?.let { handler ->
            addContentHandlerViewIfNotAdded(handler)
            handler.showContent(content) 
        } ?: run { 
            Log.e(TAG, "No ${name<ContentHandler>()} found for ${classNameWithValue(content)}") 
        }
    }

    override fun canShowContent(content: Content): Boolean {
        return contentHandlers.any { handler -> handler.canShowContent(content) }
    }

    private fun addContentHandlerViewIfNotAdded(handler: ContentHandler) {
        val contentHandlerView = handler.getView()
        
        if (!children.contains(contentHandlerView)) {
            addView(contentHandlerView)
        }
    }

    override fun dispose() {
        contentHandlers.forEach {
            (it as? Disposable)?.dispose()
        }
    }
    
    override fun play() {
        contentHandlers.forEach {
            (it as? PlayPausable)?.play()
        }
    }    
    
    override fun pause() {
        contentHandlers.forEach {
            (it as? PlayPausable)?.pause()
        }
    }
}