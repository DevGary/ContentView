package com.devgary.contentview.components.image

import android.content.Context
import android.view.View
import com.devgary.contentcore.model.content.Content
import com.devgary.contentcore.model.content.components.ContentType.*
import com.devgary.contentview.content.ContentHandler

class ImageContentHandler(private val context: Context) : ContentHandler {
    private var imageContentView: ImageContentView? = null
  
    private fun createView() {
        if (imageContentView != null) return
        imageContentView = ImageContentView(context = context)
    }

    override fun getView(): View {
        createView()
        return imageContentView!!
    }

    override fun canShowContent(content: Content): Boolean {
        return when (content.type) {
            IMAGE, GIF -> true
            else -> false
        }
    }

    override fun setViewVisibility(visibility: Int) {
        imageContentView?.setViewVisibility(visibility)
    }
    
    override fun showContent(content: Content) {
        createView()
        setViewVisibility(View.VISIBLE)

        imageContentView?.showContent(content)
    }
}