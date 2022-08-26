package com.devgary.contentview.ui.collection

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import androidx.core.view.get
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.devgary.contentcore.model.content.CollectionContent
import com.devgary.contentview.databinding.CollectionContentViewBinding

@com.devgary.contentcore.util.annotations.Experimental
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
            registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(selectedPosition: Int) {
                    super.onPageSelected(selectedPosition)
                    
                    // TODO: Add UI test to make sure this works
                    val viewpagerRecyclerView = get(0) as RecyclerView
                    
                    viewpagerRecyclerView.let {
                        for (i in 0..content.getCollection().size) {
                            val isPositionOfSelectionPrevious = i == selectedPosition - 1
                            val isPositionOfSelectionNext = i == selectedPosition + 1
                            
                            val contentViewHolder = viewpagerRecyclerView.findViewHolderForAdapterPosition(i) as? CollectionContentViewAdapter.ContentViewHolder
                            contentViewHolder?.let {
                                val contentview = it.binding.contentview
                                if (i == selectedPosition) {
                                    contentview.showContent(content.getCollection()[i])
                                    contentview.play()
                                }
                                else {
                                    contentview.pause()
                                    if (!isPositionOfSelectionPrevious && !isPositionOfSelectionNext) {
                                        contentview.dispose()
                                    }
                                }
                            }
                        }
                    }
                }
            })
        }
    }
}