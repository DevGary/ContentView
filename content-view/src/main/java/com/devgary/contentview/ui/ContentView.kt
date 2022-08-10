package com.devgary.contentview.ui

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import com.devgary.contentcore.model.Content
import com.devgary.contentview.content.ContentHandler
import com.devgary.contentcore.model.ContentType
import com.devgary.contentcore.util.TAG
import com.devgary.contentcore.util.classNameWithValue
import com.devgary.contentcore.util.name
import com.devgary.contentview.databinding.ContentViewBinding

class ContentView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
) : FrameLayout(context, attrs, defStyleAttr), ContentHandler {

    private val binding = ContentViewBinding.inflate(
        /* inflater = */ LayoutInflater.from(context), 
        /* parent = */ this, 
        /* attachToParent = */ true
    )
    
    private val allContentViews = mutableListOf<ContentHandler>()
    private val viewForContentType = mutableMapOf<ContentType, ContentHandler?>()

    init {
        initView(attrs)
        defineContentHandlers()
    }

    private fun initView(attrs: AttributeSet?) {
        hideAllContentViews()
    }

    private fun defineContentHandlers() {
        allContentViews.apply {
            add(binding.imageContentview)
            add(binding.videoContentview)
        }

        for (contentType in ContentType.values()) {
            viewForContentType[contentType] = getViewForContentType(contentType)
        }
    }

    private fun getViewForContentType(contentType: ContentType): ContentHandler? {
        return when (contentType) {
            ContentType.IMAGE, ContentType.GIF -> binding.imageContentview
            ContentType.VIDEO -> binding.videoContentview
            ContentType.ALBUM -> null // TODO: Create view for ALBUM
            else -> null
        }
    }

    override fun setViewVisibility(visibility: Int) {
        this.visibility = visibility
    }

    override fun showContent(content: Content) {
        hideAllContentViews()
        viewForContentType[content.type]?.apply {
            setViewVisibility(View.VISIBLE)
            showContent(content)
        } ?: run {
            Log.e(TAG, "No ${name<ContentHandler>()} found for ${classNameWithValue(content.type)}")
        }
    }

    private fun hideAllContentViews() {
        for (contentView in allContentViews) {
            contentView.setViewVisibility(View.GONE)
        }
    }
}