package com.devgary.contentview.image

import android.content.Context
import android.view.View
import com.devgary.contentcore.model.content.Content
import com.devgary.contentcore.model.content.components.ContentType.*
import com.devgary.contentview.ContentHandler

class ImageContentHandler(private val context: Context) : ContentHandler {
    private var imageContentView: ImageContentView? = null

    override fun getView(): ImageContentView {
        return imageContentView ?: ImageContentView(context = context).also { 
            imageContentView = it
        }
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
        getView()
        setViewVisibility(View.VISIBLE)

        imageContentView?.showContent(content)
    }
}