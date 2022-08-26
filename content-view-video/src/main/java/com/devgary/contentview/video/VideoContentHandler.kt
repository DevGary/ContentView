package com.devgary.contentview.video

import android.content.Context
import android.view.View
import com.devgary.contentcore.model.content.Content
import com.devgary.contentcore.model.content.components.ContentType
import com.devgary.contentview.ContentHandler
import com.devgary.contentview.interfaces.Disposable
import com.devgary.contentview.interfaces.PlayPausable

class VideoContentHandler(private val context: Context) : ContentHandler, Disposable, PlayPausable {
    private var videoContentView: ExoVideoView? = null
  
    override fun getView(): ExoVideoView {
        return videoContentView ?: ExoVideoView(context = context).also { 
            videoContentView = it
        }
    }

    override fun canShowContent(content: Content): Boolean {
        return when (content.type) {
            ContentType.VIDEO -> true
            else -> false
        }
    }

    override fun setViewVisibility(visibility: Int) {
        videoContentView?.setViewVisibility(visibility)
    }
    
    override fun showContent(content: Content) {
        getView()
        setViewVisibility(View.VISIBLE)

        videoContentView?.showContent(content)
    }

    override fun dispose() {
        videoContentView?.releasePlayer()
    }

    override fun play() {
        videoContentView?.play()
    }

    override fun pause() {
        videoContentView?.pause()
    }
}