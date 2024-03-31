package com.devgary.contentviewdemo.screens.list

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.devgary.contentcore.util.TAG
import com.devgary.contentcore.util.getThroughReflection
import com.devgary.contentcore.util.trim
import com.devgary.contentlinkapi.content.ContentLinkHandler
import com.devgary.contentview.BuildConfig
import com.devgary.contentview.ContentHandler
import com.devgary.contentview.ViewPoolComposite
import com.devgary.contentview.model.ScaleType
import com.devgary.contentview.video.VideoContentHandler
import com.devgary.contentviewdemo.databinding.ItemLayoutListBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.StringBuilder

class ContentAdapter(
    private val context: Context,
    private val contentLinkHandler: ContentLinkHandler,
) : RecyclerView.Adapter<ContentAdapter.ContentViewHolder>() {

    private val showDebugOverlay = BuildConfig.DEBUG
    
    private val viewPoolComposite = ViewPoolComposite().also {
        it.setPoolMaxSize(poolType = VideoContentHandler::class.java.kotlin, maxSize = 1)
    }
                
    private val urlCollection: MutableList<String> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContentViewHolder {
        return ContentViewHolder(
            ItemLayoutListBinding.inflate(
                /* inflater = */ LayoutInflater.from(context),
                /* parent = */parent,
                /* attachToParent = */false
            ).also { binding ->
                binding.contentview.setViewScaleType(ScaleType.FILL_WIDTH)
                viewPoolComposite?.let {
                    binding.contentview.attachViewPool(it)
                }
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
    
    inner class ContentViewHolder(val binding: ItemLayoutListBinding) : RecyclerView.ViewHolder(binding.root) {
        private var contentUrl: String? = null
        private val debugInfoRefreshHandler = Handler(Looper.getMainLooper())

        init {
            binding.debugInfoTextView.setOnClickListener { 
                activateContent()
            }
            
            binding.contentview.setOnClickListener {
                disposeContent()
            }
        }
        
        fun bind(contentUrl: String) {
            binding.contentview.attachViewPool(viewPoolComposite)
            
            this.contentUrl = contentUrl
            binding.urlTextView.text = contentUrl
            showContent()

            if (showDebugOverlay) {
                debugInfoRefreshHandler.post(object : Runnable {
                    override fun run() {
                        bindDebugInfo()
                        debugInfoRefreshHandler.postDelayed(this, 250)
                    }
                })
            }
        }
        
        private fun bindDebugInfo() {
            if (showDebugOverlay) {
                binding.debugInfoTextView.text = createDebugInfoString()
            }
        }
        
        private fun createDebugInfoString(): String {
            val strBuilder = StringBuilder()
            strBuilder.appendLine("Url: $contentUrl")
            strBuilder.appendLine("ContentView: ${binding.contentview.TAG}")
            strBuilder.appendLine("ContentView Content: ${binding.contentview.content?.toLogString()?.trim(maxLength = 130)}")
            strBuilder.appendLine("Content Handler: ${binding.contentview.currentlyUsedHandler?.TAG}")
            strBuilder.appendLine()
            
            val contentHandlers = binding.contentview.getThroughReflection<List<ContentHandler>>("contentHandlers")
            val videoContentHandler = contentHandlers?.filterIsInstance<VideoContentHandler>()?.firstOrNull()
            videoContentHandler?.let {
                strBuilder.append("VideoContentHandler.View: ${it.videoContentView?.TAG}")
            }
            return strBuilder.toString()
        }

        private fun showContent() {
            val contentUrl = contentUrl ?: return

            CoroutineScope(Dispatchers.IO).launch {
                contentLinkHandler.getContent(contentUrl)?.let { content ->
                    Handler(Looper.getMainLooper()).post {
                        binding.contentview.setAutoplay(true)
                        binding.contentview.setViewVisibility(View.VISIBLE)
                        binding.contentview.showContent(content)
                        binding.contentview.play()
                    }
                }
            }
        }
        
        fun activateContent() {
            binding.contentview.activate()
        }
        
        fun deactivateContent() {
            binding.contentview.deactivate()
        }

        private fun disposeContent() {
            binding.contentview.dispose()
        }

        fun pausePlayingContent() {
            binding.contentview.pause()
        }
    }
}