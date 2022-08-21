package com.devgary.contentview.components.collection

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.devbrackets.android.exomedia.ui.widget.VideoView
import com.devgary.contentcore.model.Content
import com.devgary.contentview.R
import com.devgary.contentview.databinding.ItemLayoutContentViewPagerBinding

class CollectionContentViewAdapter(
    private val context: Context,
    private val contentCollection: List<Content>,
) : RecyclerView.Adapter<CollectionContentViewAdapter.ContentViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContentViewHolder =
        ContentViewHolder(
            ItemLayoutContentViewPagerBinding.inflate(
                /* inflater = */ LayoutInflater.from(context),
                /* parent = */parent,
                /* attachToParent = */false
            )
        )

    override fun onBindViewHolder(holder: ContentViewHolder, position: Int) {
        holder.bind(contentCollection[position])
    }

    override fun getItemCount(): Int = contentCollection.size

    inner class ContentViewHolder(private val binding: ItemLayoutContentViewPagerBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(content: Content) {
            binding.contentview.apply {
                showContent(content)

                // TODO: Improve this with generic disposable interface and investigate potential memory leak
                findViewById<VideoView>(R.id.videoview)?.setReleaseOnDetachFromWindow(false)
            }
        }
    }
}