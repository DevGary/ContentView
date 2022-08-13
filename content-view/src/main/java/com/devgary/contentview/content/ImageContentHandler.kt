package com.devgary.contentview.content

import android.content.Context
import android.view.View
import com.devgary.contentcore.model.Content
import com.devgary.contentcore.model.ContentType.*
import com.devgary.contentview.ui.ImageContentView

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