package com.devgary.contentview.ui

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import com.devgary.contentview.content.Content
import com.devgary.contentview.content.ContentHandler
import com.devgary.contentview.content.ContentType
import com.devgary.contentview.databinding.ContentViewBinding

class ContentView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
) : FrameLayout(context, attrs, defStyleAttr), ContentHandler {
    private val binding = ContentViewBinding.inflate(LayoutInflater.from(context), this, true)

    init {
        initView(attrs)
    }

    private fun initView(attrs: AttributeSet?) {
        setupView()
    }

    private fun setupView() {
        hideContentViews()
    }

    override fun showContent(content: Content) {
        hideContentViews()
        when(content.type) {
            ContentType.IMAGE, ContentType.GIF -> {
                binding.imageContentview.visibility = VISIBLE
                binding.imageContentview.showContent(content)
            }
            ContentType.VIDEO -> {
                binding.videoContentview.visibility = VISIBLE
                binding.videoContentview.showContent(content)
            }
            ContentType.ALBUM -> TODO()
        }
    }

    private fun hideContentViews() {
        binding.imageContentview.visibility = GONE
        binding.videoContentview.visibility = GONE
    }
}