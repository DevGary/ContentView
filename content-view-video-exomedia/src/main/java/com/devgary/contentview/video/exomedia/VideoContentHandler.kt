package com.devgary.contentview.video.exomedia

import android.content.Context
import android.view.View
import com.devgary.contentcore.model.content.Content
import com.devgary.contentcore.model.content.components.ContentType.*
import com.devgary.contentview.ContentHandler

class VideoContentHandler(private val context: Context) : ContentHandler {
    private var videoContentView: VideoContentView? = null
  
    private fun createView() {
        if (videoContentView != null) return
        videoContentView = VideoContentView(context = context)
    }

    override fun getView(): View {
        createView()
        return videoContentView!!
    }

    override fun canShowContent(content: Content): Boolean {
        return when (content.type) {
            VIDEO -> true
            else -> false
        }
    }

    override fun setViewVisibility(visibility: Int) {
        videoContentView?.setViewVisibility(visibility)
    }
    
    override fun showContent(content: Content) {
        createView()
        setViewVisibility(View.VISIBLE)

        videoContentView?.showContent(content)
    }
}