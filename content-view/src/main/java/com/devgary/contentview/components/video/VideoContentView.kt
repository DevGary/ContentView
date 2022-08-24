package com.devgary.contentview.components.video

import android.content.Context
import android.net.Uri
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import com.devgary.contentcore.model.content.Content
import com.devgary.contentcore.model.content.components.ContentSource
import com.devgary.contentview.databinding.VideoContentViewBinding

class VideoContentView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
) : FrameLayout(context, attrs, defStyleAttr) {
    private val binding = VideoContentViewBinding.inflate(LayoutInflater.from(context), this, true)

    fun showContent(content: Content) {
        binding.videoview.apply { 
            setOnPreparedListener { 
                this.start()
            }
            setVideoViewToWrapHeight(this)

            when(content.source) {
                is ContentSource.Url -> {
                    val url = (content.source as ContentSource.Url).url
                    setVideoURI(Uri.parse(url))
                }
                else -> {}
            }
        }
    }
    
    fun setViewVisibility(visibility: Int) {
        this.visibility = visibility
    }

    private fun setVideoViewToWrapHeight(videoView: View) {
        videoView.post {
            val params = layoutParams
            params.height = (width * 9 / 16)
            layoutParams = params
        }
    } 
}