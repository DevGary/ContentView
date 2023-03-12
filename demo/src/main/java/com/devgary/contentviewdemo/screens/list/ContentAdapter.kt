package com.devgary.contentviewdemo.screens.list

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.devgary.contentlinkapi.content.ContentLinkHandler
import com.devgary.contentview.ViewPoolComposite
import com.devgary.contentview.model.ScaleType
import com.devgary.contentviewdemo.databinding.ItemLayoutListBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ContentAdapter(
    private val context: Context,
    private val contentLinkHandler: ContentLinkHandler,
) : RecyclerView.Adapter<ContentAdapter.ContentViewHolder>() {

    private val viewPoolComposite = ViewPoolComposite()
                
    private val urlCollection: MutableList<String> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContentViewHolder {
        return ContentViewHolder(
            ItemLayoutListBinding.inflate(
                /* inflater = */ LayoutInflater.from(context),
                /* parent = */parent,
                /* attachToParent = */false
            ).also { 
                it.contentview.setViewScaleType(ScaleType.FIT_CENTER)
            }
        )
    }

    override fun onBindViewHolder(holder: ContentViewHolder, position: Int) {
        holder.bind(urlCollection[position])
    }

    override fun getItemCount(): Int = urlCollection.size

    fun setData(urls: List<String>) {
        urlCollection.clear()
        urlCollection.addAll(urls)
        notifyDataSetChanged()
    }
    
    inner class ContentViewHolder(private val binding: ItemLayoutListBinding) : RecyclerView.ViewHolder(binding.root) {
        private var contentUrl: String? = null
        
        fun bind(contentUrl: String) {
            this.contentUrl = contentUrl
            binding.urlTextView.text = contentUrl
            binding.contentview.attachViewPool(viewPoolComposite)
            
            // TODO [!]: Remove temp code after ActivatableContent implemented
            if (contentUrl.endsWith(".png") || contentUrl.endsWith(".jpg")) {
                binding.contentview.setViewVisibility(View.VISIBLE)
                showContent()
            }
            else {
                binding.contentview.setViewVisibility(View.GONE)
            }
        }

        fun showContent() {
            val contentUrl = contentUrl ?: return

            CoroutineScope(Dispatchers.IO).launch {
                contentLinkHandler.getContent(contentUrl)?.let { content ->
                    Handler(Looper.getMainLooper()).post {
                        binding.contentview.setAutoplay(true)
                        binding.contentview.setViewVisibility(View.VISIBLE)
                        binding.contentview.showContent(content)
                    }
                }
            }
        }

        fun stopPlayingContent() {
            binding.contentview.pause()
        }
    }
}