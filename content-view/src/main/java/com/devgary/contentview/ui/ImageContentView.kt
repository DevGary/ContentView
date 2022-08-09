package com.devgary.contentview.ui

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import com.bumptech.glide.Glide
import com.devgary.contentview.content.Content
import com.devgary.contentview.content.ContentHandler
import com.devgary.contentview.databinding.ImageContentViewBinding

class ImageContentView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
) : FrameLayout(context, attrs, defStyleAttr), ContentHandler {
    private val binding = ImageContentViewBinding.inflate(LayoutInflater.from(context), this, true)
    
    init {
        initView(attrs)
    }

    private fun initView(attrs: AttributeSet?) {
        setupView()
    }

    private fun setupView() {
    }

    override fun showContent(content: Content) {
        Glide.with(context)
            .load(content.url)
            .into(binding.photoview)
    }
}