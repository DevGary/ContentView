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
    
    // TODO [!]: Only public for logging/debugging purposes. Maybe use reflection instead.
    var videoContentView: ExoVideoView? = null
        private set
    private var autoplay: Boolean? = null
    private var scaleType: ScaleType? = null
    private val viewPoolListener = object : ViewPool.Listener {
        override fun onViewRecycled(view: View) {
            if (view == videoContentView) {
                Log.d(TAG, "Detaching view from ${this@VideoContentHandler.TAG} as view was recycled")
                videoContentView = null
            }
        }
    }

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
            val newViewPool = ViewPool(viewCreator = { ExoVideoView(context) })
            setViewPool(newViewPool)
        }
        return viewPool!!
    }

    override fun setViewPool(viewPool: ViewPool) {
        this.viewPool = viewPool.also {
            it.addListener(viewPoolListener)
        }
    }

    override fun canShowContent(content: Content): Boolean {
        return when (content.type) {
            ContentType.VIDEO -> true
            else -> false
        }
    }

    override fun setViewVisibility(visibility: Int) {
        videoContentView?.let {
            Log.d(TAG, "Setting visibility of view $it to $visibility")
            it.setViewVisibility(visibility)
        }
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
            viewPool?.recycleView(it)
            videoContentView = null
        }
    }

    override fun dispose() {
        videoContentView?.releaseAll()
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