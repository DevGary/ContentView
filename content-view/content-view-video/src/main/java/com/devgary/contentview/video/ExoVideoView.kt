package com.devgary.contentview.video

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.widget.FrameLayout
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.source.DefaultMediaSourceFactory
import androidx.media3.exoplayer.source.MediaSource
import com.devgary.contentcore.model.content.Content
import com.devgary.contentcore.model.content.areContentsTheSame
import com.devgary.contentcore.model.content.components.ContentSource
import com.devgary.contentcore.util.TAG
import com.devgary.contentcore.util.classNameWithValue
import com.devgary.contentcore.util.name
import com.devgary.contentcore.util.setHeight
import com.devgary.contentcore.util.setWidth
import com.devgary.contentview.model.ScaleType
import com.devgary.contentview.video.databinding.ExoVideoViewBinding

class ExoVideoView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
) : FrameLayout(context, attrs, defStyleAttr) {
    private val binding = ExoVideoViewBinding.inflate(LayoutInflater.from(context), this, true)

    var scaleType: ScaleType? = null
    
    private var content: Content? = null
    private var exoplayer: ExoPlayer? = null
    private var autoplay: Boolean = true
    private var mediaSource: MediaSource? = null

    init {
        binding.playerView.setOnClickListener { 
            togglePlayPause()
        }
    }
    
    private fun getOrCreatePlayer(): ExoPlayer {
        if (exoplayer == null) {
            Log.d(TAG, "Creating instance of ${name<ExoPlayer>()} for ${content?.source?.toLogString()}")
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
        else {
            Log.d(TAG, "Reusing instance of ${name<ExoPlayer>()} for ${content?.source?.toLogString()}")
        }
        
        return exoplayer!!
    }

    fun showContent(content: Content) {
        if (this.content?.areContentsTheSame(content) == true) {
            Log.w(
                TAG,
                "showContent(content) called with content that is already being shown. Function will " +
                        "return immediately. Consider using other functions if you want to restart or play " +
                        "the currently configured content"
            )
            return
        }
        
        this.content = content

        val mediaItem = when (content.source) {
            is ContentSource.Url -> {
                val url = (content.source as ContentSource.Url).url
                MediaItem.fromUri(url)
            }
            else -> {
                Log.e(TAG, "${name<ExoVideoView>()} does not support ${name<Content>()} of ${classNameWithValue(content.source)}")
                null
            }
        }

        mediaItem?.let {
            val mediaSourceFactory = DefaultMediaSourceFactory(context)
            releaseMedia()
            mediaSourceFactory.createMediaSource(it).also { mediaSource ->
                this.mediaSource = mediaSource
                getOrCreatePlayer().setMediaSource(mediaSource)
            }
        }
    }

    private fun onPlaybackReady() {
        if (scaleType == ScaleType.FILL_WIDTH) {
            setVideoViewToWrapHeight()
        }
    }

    private fun setVideoViewToWrapHeight() {
        binding.playerView.post {
            exoplayer?.videoFormat?.let {
                if (it.width > 0 && it.height > 0) {
                    when (scaleType) {
                        ScaleType.FILL_WIDTH -> {
                            val aspectRatio = it.width.toFloat() / it.height
                            val height = (width / aspectRatio).toInt()
                            Log.v(TAG, "Setting video view height to $height")
                            setHeight(height)
                        }
                        ScaleType.FIT_CENTER -> {
                            val aspectRatio = it.width.toFloat() / it.height
                            val width = (measuredHeight * aspectRatio).toInt()
                            Log.v(TAG, "Setting video view width to $height based on measuredHeight = $measuredHeight and aspectRatio=$aspectRatio")
                            setWidth(width)
                        }
                        null -> {}
                    }
   
                }
            }
        }
    }

    fun releasePlayer() {
        exoplayer?.let { exoplayer ->
            Log.i(TAG, "Releasing player")
            releaseMedia()
            exoplayer.release()
        }
        content = null
        exoplayer = null
    }

    fun releaseMedia() {
        mediaSource?.releaseSource { _, _ -> }
        exoplayer?.clearMediaItems()
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