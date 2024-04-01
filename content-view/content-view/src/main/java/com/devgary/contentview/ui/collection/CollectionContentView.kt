package com.devgary.contentview.ui.collection

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import androidx.core.view.get
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.devgary.contentcore.model.content.CollectionContent
import com.devgary.contentcore.model.content.Content
import com.devgary.contentcore.util.setHeight
import com.devgary.contentcore.util.waitForLayout
import com.devgary.contentview.databinding.CollectionContentViewBinding
import com.devgary.contentview.model.ScaleType

@com.devgary.contentcore.util.annotations.Experimental
class CollectionContentView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
) : FrameLayout(context, attrs, defStyleAttr) {
    private val binding = CollectionContentViewBinding.inflate(LayoutInflater.from(context), this, true)
    
    var scaleType: ScaleType? = null
    
    private var pagerAdapter: CollectionContentViewAdapter? = null
    /**
     * Cache of the height of ContentView for Content item in CollectionContent.
     * Store this in View because if the View is destroyed, we will probably
     * want to also destroy this and recalculate the heights
     */
    private val contentItemsViewHeight = mutableMapOf<Content, Int>()
    
    fun setViewVisibility(visibility: Int) {
        this.visibility = visibility
    }
    
    fun showContent(content: CollectionContent) {
        pagerAdapter = CollectionContentViewAdapter(context, content.getCollection())
        binding.viewpager.apply {
            adapter = pagerAdapter
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
                                val contentItem = content.getCollection()[i]
                                if (isPositionOfSelection) {
                                    if (scaleType == ScaleType.FILL_WIDTH) {
                                        wrapViewpagerHeight(contentViewHolder)
                                    }
                                    
                                    contentview.play()
                                    contentview.showContent(contentItem)
                                }
                                else {
                                    contentview.pause()
                                    if (isPositionOfSelectionPrevious || isPositionOfSelectionNext) {
                                        // IF prev or next item, call showContent to preload it
                                        contentview.showContent(contentItem)
                                    } else {
                                        // If not selected, prev, or next, dispose to reclaim resources
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

    /**
     * Updates [ViewPager2] height to wrap around [viewHolder]. Otherwise if the ViewPager does not
     * have a fixed height set, it will expand when displaying an item that is larger than it but
     * never shrink back. Though that may be a behavior that is desired and will be added in the future.
     * 
     * @param viewHolder ViewHolder to wrap ViewPager2 around
     */
    private fun wrapViewpagerHeight(viewHolder: CollectionContentViewAdapter.ContentViewHolder) {
        val adapter = pagerAdapter ?: return
        
        val contentItem = adapter.contentCollection[viewHolder.bindingAdapterPosition]
        val alreadyDeterminedContentViewHeight = contentItemsViewHeight[contentItem]
        if (alreadyDeterminedContentViewHeight != null) {
            binding.viewpager.setHeight(alreadyDeterminedContentViewHeight)
        }
        else {
            // We want to wait for the container for the actual ContentHandlerViews to be
            // laid out which hopefully means the content has loaded. Then we resize the
            // ViewPager2 view to wrap the height of the entire ContentView
            // TODO: Remove OnGlobalLayoutListeners from non-selected ContentViews
            viewHolder.binding.contentview.waitForLayout {
                val viewToWrapAround = viewHolder.itemView
                viewToWrapAround.post {
                    val widthMeasureSpec = MeasureSpec.makeMeasureSpec(viewToWrapAround.width, MeasureSpec.EXACTLY)
                    val heightMeasureSpec = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED)
                    viewToWrapAround.measure(widthMeasureSpec, heightMeasureSpec)
                    contentItemsViewHeight[contentItem] = viewToWrapAround.measuredHeight

                    val isForCurrentPage = binding.viewpager.currentItem == viewHolder.bindingAdapterPosition
                    if (isForCurrentPage) {
                        if (binding.viewpager.layoutParams.height != viewToWrapAround.measuredHeight) {
                            binding.viewpager.setHeight(viewToWrapAround.measuredHeight)
                        }
                    }
                }

            }
        }
    }
    
    fun dispose() {
        val viewPagerRecyclerView = binding.viewpager.get(0) as RecyclerView

        for (i in 0 until viewPagerRecyclerView.childCount) {
            val view = viewPagerRecyclerView.getChildAt(i)
            val viewHolder = viewPagerRecyclerView.getChildViewHolder(view)
            (viewHolder as? CollectionContentViewAdapter.ContentViewHolder)?.let {
                it.binding.contentview.dispose()
            }
        }
    }
}