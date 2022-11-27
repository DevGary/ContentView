package com.devgary.contentviewdemo.screens.list

import android.content.Context
import android.os.Looper
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.devgary.contentlinkapi.content.ContentLinkHandler
import com.devgary.contentviewdemo.databinding.ItemLayoutListBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ContentAdapter(
    private val context: Context,
    private val contentLinkHandler: ContentLinkHandler,
) : RecyclerView.Adapter<ContentAdapter.ContentViewHolder>() {

    private val urlCollection: MutableList<String> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContentViewHolder {
        val viewHolder = ContentViewHolder(
            ItemLayoutListBinding.inflate(
                /* inflater = */ LayoutInflater.from(context),
                /* parent = */parent,
                /* attachToParent = */false
            )
        )
        
        viewHolder.binding.urlTextView.setOnClickListener {
            viewHolder.binding.contentview.dispose()

            val contentUrl = urlCollection[viewHolder.bindingAdapterPosition]
            
            CoroutineScope(Dispatchers.IO).launch {
                contentLinkHandler.getContent(contentUrl)?.let { it ->
                    android.os.Handler(Looper.getMainLooper()).post {
                        viewHolder.binding.contentview.showContent(it)
                    }
                }
            }
        }
        
        return viewHolder
    }

    override fun onBindViewHolder(holder: ContentViewHolder, position: Int) {
        holder.bind(urlCollection[position])
    }

    override fun onViewRecycled(holder: ContentViewHolder) {
        super.onViewRecycled(holder)
        holder.release()
    }

    override fun getItemCount(): Int = urlCollection.size

    fun setData(urls: List<String>) {
        urlCollection.clear()
        urlCollection.addAll(urls)
        notifyDataSetChanged()
    }
    
    inner class ContentViewHolder(val binding: ItemLayoutListBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(contentUrl: String) {
            binding.urlTextView.text = contentUrl
            
            binding.contentview.apply {
                setAutoplay(false)
                CoroutineScope(Dispatchers.IO).launch {
                    contentLinkHandler.getContent(contentUrl)?.let { it ->
                        android.os.Handler(Looper.getMainLooper()).post {
                            binding.contentview.showContent(it)
                        }
                    }
                }
            }
        }
        
        fun release() {
            binding.contentview.dispose()
        }
    }
}