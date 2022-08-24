package com.devgary.contentview.components.collection

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import androidx.viewpager2.widget.ViewPager2
import com.devgary.contentcore.model.content.CollectionContent
import com.devgary.contentview.databinding.CollectionContentViewBinding

class CollectionContentView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
) : FrameLayout(context, attrs, defStyleAttr) {
    private val binding = CollectionContentViewBinding.inflate(LayoutInflater.from(context), this, true)
    
    fun setViewVisibility(visibility: Int) {
        this.visibility = visibility
    }
    
    fun showContent(content: CollectionContent) {
        binding.viewpager.apply {
            adapter = CollectionContentViewAdapter(context, content.getCollection())
            orientation = ViewPager2.ORIENTATION_HORIZONTAL
            offscreenPageLimit = 1
        }
    }
}