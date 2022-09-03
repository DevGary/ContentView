package com.devgary.contentview.ui.collection

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import androidx.core.view.get
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.devgary.contentcore.model.content.CollectionContent
import com.devgary.contentcore.util.setHeight
import com.devgary.contentcore.util.waitForLayout
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
                            val isPositionOfSelection = i == selectedPosition
                            
                            val contentViewHolder = viewpagerRecyclerView.findViewHolderForAdapterPosition(i) as? CollectionContentViewAdapter.ContentViewHolder
                            contentViewHolder?.let {
                                val contentview = it.binding.contentview
                                if (isPositionOfSelection) {
                                    contentview.play()
                                    contentview.showContent(content.getCollection()[i])
                                    
                                    // We want to wait for the container for the actual ContentHandlerViews to be
                                    // laid out which hopefully means the content has loaded. Then we resize the
                                    // ViewPager2 view to wrap the height of the entire ContentView
                                    // TODO: Remove OnGlobalLayoutListeners from non-selected ContentViews
                                    contentview.waitForLayout {
                                        val isForCurrentPage = currentItem == i
                                        if (isForCurrentPage) {
                                            resizeViewPagerToWrapViewHeight(it.itemView)
                                        }
                                    }
                                }
                                else {
                                    contentview.pause()
                                    if (isPositionOfSelectionPrevious || isPositionOfSelectionNext) {
                                        contentview.showContent(content.getCollection()[i])
                                    } else {
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
    
    private fun resizeViewPagerToWrapViewHeight(viewToWrap: View) {
        viewToWrap.post {
            val widthMeasureSpec = MeasureSpec.makeMeasureSpec(viewToWrap.width, MeasureSpec.EXACTLY)
            val heightMeasureSpec = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED)
            viewToWrap.measure(widthMeasureSpec, heightMeasureSpec)

            if (binding.viewpager.layoutParams.height != viewToWrap.measuredHeight) {
                binding.viewpager.setHeight(viewToWrap.measuredHeight)
            }
        }
    }
}