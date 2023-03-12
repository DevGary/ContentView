package com.devgary.contentview.video

import android.content.Context
import android.util.Log
import android.view.View
import com.devgary.contentcore.model.content.Content
import com.devgary.contentcore.model.content.components.ContentType
import com.devgary.contentcore.util.TAG
import com.devgary.contentcore.util.name
import com.devgary.contentview.ContentHandler
import com.devgary.contentview.ViewPool
import com.devgary.contentview.interfaces.Disposable
import com.devgary.contentview.interfaces.PlayPausable
import com.devgary.contentview.interfaces.Recyclable
import com.devgary.contentview.model.ScaleType

class VideoContentHandler(private val context: Context) : ContentHandler, Disposable, Recyclable, PlayPausable {
    private var viewPool: ViewPool? = null
    
    private var videoContentView: ExoVideoView? = null
    private var autoplay: Boolean? = null
    private var scaleType: ScaleType? = null

    override fun getView(): ExoVideoView {
        // TODO [!]: Refactor into reusable code
        val exoVideoView =
            if (videoContentView != null) {
                videoContentView!!
            } else if (viewPool != null) {
                // TODO: Unsafe cast. Use generics in ViewPool
                viewPool!!.getView() as ExoVideoView
            } else {
                Log.i(TAG, "Creating new ${name<ExoVideoView>()}. View will not be recyclable as no pool has been attached")
                ExoVideoView(context)
            }
        
        viewPool?.setViewUsed(exoVideoView)

        return exoVideoView.also {
            videoContentView = it.also {
                autoplay?.let { autoplay ->
                    setAutoplay(autoplay)
                }
                it.scaleType = scaleType
            }
        }
    }

    override fun getOrCreateViewPool(): ViewPool {
        if (viewPool == null) {
            viewPool = ViewPool(viewCreator = { ExoVideoView(context) })
        }
        return viewPool!!
    }

    override fun setViewPool(viewPool: ViewPool) {
        this.viewPool = viewPool
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

    override fun recycle() {
        videoContentView?.let {
            it.setAutoplay(false)
            it.releaseMedia()
            viewPool?.setViewUnused(it)
        }
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