package com.devgary.contentview.image

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import com.bumptech.glide.Glide
import com.devgary.contentcore.model.content.Content
import com.devgary.contentcore.model.content.components.ContentSource
import com.devgary.contentview.image.databinding.ImageContentViewBinding

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
        @Suppress("IMPLICIT_CAST_TO_ANY") 
        val model = when(content.source) {
            is ContentSource.Drawable -> {
                (content.source as ContentSource.Drawable).drawable
            }
            is ContentSource.Url -> {
                (content.source as ContentSource.Url).url
            }
            else -> null
        }
        
        model?.let {
            Glide.with(context)
                .load(it)
                .into(binding.photoview)
        }
    }
}