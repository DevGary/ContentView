package com.devgary.contentview.video

import android.content.Context
import android.view.View
import com.devgary.contentcore.model.content.Content
import com.devgary.contentcore.model.content.components.ContentType
import com.devgary.contentview.ContentHandler
import com.devgary.contentview.interfaces.Disposable
import com.devgary.contentview.interfaces.PlayPausable
import com.devgary.contentview.model.ScaleType

class VideoContentHandler(private val context: Context) : ContentHandler, Disposable, PlayPausable {
    private var videoContentView: ExoVideoView? = null
    private var autoplay: Boolean? = null
    private var scaleType: ScaleType? = null
  
    override fun getView(): ExoVideoView {
        return videoContentView ?: ExoVideoView(context = context).also { 
            videoContentView = it.also { 
                autoplay?.let { autoplay ->
                    setAutoplay(autoplay)
                }
                
                it.scaleType = scaleType
            }
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

    override fun setViewScaleType(scaleType: ScaleType) {
        this.scaleType = scaleType
        videoContentView?.scaleType = scaleType
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

    override fun setAutoplay(autoplay: Boolean) {
        this.autoplay = autoplay
        videoContentView?.setAutoplay(autoplay)
    }
}