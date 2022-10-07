package com.devgary.contentview.video

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.widget.FrameLayout
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import com.devgary.contentcore.model.content.Content
import com.devgary.contentcore.model.content.components.ContentSource
import com.devgary.contentcore.util.TAG
import com.devgary.contentcore.util.name
import com.devgary.contentcore.util.setHeight
import com.devgary.contentview.video.databinding.ExoVideoViewBinding

class ExoVideoView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
) : FrameLayout(context, attrs, defStyleAttr) {
    private val binding = ExoVideoViewBinding.inflate(LayoutInflater.from(context), this, true)

    private var content: Content? = null
    private var exoplayer: ExoPlayer? = null
    private var autoplay: Boolean = true

    init {
        binding.playerView.setOnClickListener { 
            togglePlayPause()
        }
    }
    
    private fun getOrCreatePlayer(): ExoPlayer {
        if (exoplayer == null) {
            Log.d(TAG, "Creating instance of ${name<ExoPlayer>()}")
            exoplayer = ExoPlayer.Builder(context)
                .build()
                .also { exoplayer ->
                    exoplayer.repeatMode = Player.REPEAT_MODE_ONE
                    binding.playerView.player = exoplayer
                    
                    exoplayer.playWhenReady = autoplay
                    exoplayer.prepare()

                    binding.playerView.visibility = GONE
                    exoplayer.addListener(object : Player.Listener {
                        override fun onPlaybackStateChanged(playbackState: Int) {
                            super.onPlaybackStateChanged(playbackState)
                            if (playbackState == ExoPlayer.STATE_READY) {
                                binding.playerView.visibility = VISIBLE
                                onPlaybackReady()
                            }
                        }
                    })
                }
        }
        
        return exoplayer!!
    }

    fun showContent(content: Content) {
        // TODO: Do smarter comparison
        if (this.content != content) {
            this.content = content

            val mediaItem = when (content.source) {
                is ContentSource.Url -> {
                    val url = (content.source as ContentSource.Url).url
                    MediaItem.fromUri(url)
                }
                else -> null
            }

            mediaItem?.let {
                getOrCreatePlayer().setMediaItem(it)
            }
        }
    }

    private fun onPlaybackReady() {
        if (this.measuredHeight < 10) {
            setVideoViewToWrapHeight()
        }
    }

    private fun setVideoViewToWrapHeight() {
        binding.playerView.post {
            exoplayer?.videoFormat?.let {
                if (it.width > 0 && it.height > 0) {
                    val aspectRatio = it.width.toFloat() / it.height
                    val height = (width / aspectRatio).toInt()
                    Log.v(TAG, "Setting video view height to $height")
                    setHeight(height)
                }
            }
        }
    }

    fun releasePlayer() {
        exoplayer?.let { exoplayer ->
            Log.i(TAG, "Releasing player")
            exoplayer.release()
        }
        content = null
        exoplayer = null
    }

    fun setViewVisibility(visibility: Int) {
        this.visibility = visibility
    }
    
    fun setAutoplay(autoplay: Boolean) {
        this.autoplay = autoplay
    }
    
    fun play() {
        exoplayer?.playWhenReady = true
    }

    fun pause() {
        exoplayer?.playWhenReady = false
    }
    
    private fun togglePlayPause() {
        exoplayer?.let { 
            if (it.playWhenReady) pause() else play()
        }
    }
}