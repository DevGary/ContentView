package com.devgary.contentview.components.image

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import com.bumptech.glide.Glide
import com.devgary.contentcore.model.Content
import com.devgary.contentview.databinding.ImageContentViewBinding

class ImageContentView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
) : FrameLayout(context, attrs, defStyleAttr) {
    private val binding = ImageContentViewBinding.inflate(LayoutInflater.from(context), this, true)
    
    fun setViewVisibility(visibility: Int) {
        this.visibility = visibility
    }
    
    fun showContent(content: Content) {
        Glide.with(context)
            .load(content.url)
            .into(binding.photoview)
    }
}