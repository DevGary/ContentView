package com.devgary.contentview.ui

import android.content.Context
import android.net.Uri
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import com.devgary.contentcore.model.Content
import com.devgary.contentview.content.ContentHandler
import com.devgary.contentview.databinding.VideoContentViewBinding

class VideoContentView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
) : FrameLayout(context, attrs, defStyleAttr), ContentHandler {
    private val binding = VideoContentViewBinding.inflate(LayoutInflater.from(context), this, true)
    
    init {
        initView(attrs)
    }

    private fun initView(attrs: AttributeSet?) {
        setupView()
    }

    private fun setupView() {
    }

    override fun showContent(content: Content) {
        binding.videoview.apply { 
            setOnPreparedListener { 
                this.start()
            }
            setVideoURI(Uri.parse(content.url))
            setVideoViewToWrapHeight(this)
        }
    }
    
    override fun setViewVisibility(visibility: Int) {
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