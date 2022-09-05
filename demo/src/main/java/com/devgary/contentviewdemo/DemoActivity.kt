package com.devgary.contentviewdemo

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.devgary.testcore.SampleContent
import com.devgary.contentlinkapi.handlers.gfycat.GfycatContentLinkHandler
import com.devgary.contentlinkapi.handlers.imgur.ImgurContentLinkHandler
import com.devgary.contentlinkapi.handlers.streamable.StreamableContentLinkHandler
import com.devgary.contentlinkapi.content.BaseContentLinkHandler
import com.devgary.contentlinkapi.content.CompositeContentLinkHandler
import com.devgary.contentlinkapi.content.ContentLinkHandler
import com.devgary.contentviewdemo.databinding.ActivityDemoBinding
import com.devgary.contentviewdemo.util.cancel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class DemoActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDemoBinding

    private val contentLinkHandler: CompositeContentLinkHandler by lazy {
        object : BaseContentLinkHandler() {
            override fun provideContentHandlers(): List<ContentLinkHandler> {
                return listOf(
                    GfycatContentLinkHandler(
                        clientId = BuildConfig.GFYCAT_CLIENT_ID,
                        clientSecret = BuildConfig.GFYCAT_CLIENT_SECRET
                    ),
                    ImgurContentLinkHandler(
                        authorizationHeader = BuildConfig.IMGUR_AUTHORIZATION_HEADER,
                        mashapeKey = BuildConfig.IMGUR_MASHAPE_KEY
                    ),
                    StreamableContentLinkHandler(),
                    DemoFallthroughContentLinkHandler()
                )
            }
        }
    }
    
    var getContentJob: Job? = null
    private val coroutineExceptionHandler = CoroutineExceptionHandler { _, throwable ->
        Toast.makeText(
            /* context = */ this,
            /* text = */ "Error: ${throwable.message}",
            /* duration = */ Toast.LENGTH_LONG
        ).show()
    }
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDemoBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        
        showContent(SampleContent.IMAGE_CONTENT)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_demo, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.menu_image -> SampleContent.IMAGE_CONTENT
            R.id.menu_image_no_ext -> SampleContent.IMAGE_CONTENT_NO_EXTENSION
            R.id.menu_gif -> SampleContent.GIF_CONTENT
            R.id.menu_video -> SampleContent.MP4_VIDEO_CONTENT
            R.id.menu_streamable -> SampleContent.STREAMABLE.BASIC_URL 
            R.id.menu_streamable_parse_webpage -> SampleContent.STREAMABLE.HLS_URL 
            R.id.menu_gfycat_video -> SampleContent.GFYCAT_URL
            R.id.menu_imgur_album -> SampleContent.IMGUR_ALBUM_GALLERY_URL
            R.id.menu_clear_memory -> {
                contentLinkHandler.clearMemory()
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }.let { url ->
            showContent(url)
            return true
        }
    }

    private fun showContent(url: String) {
        getContentJob.cancel()
        binding.contentview.setViewVisibility(View.GONE)
        getContentJob = lifecycleScope.launch(coroutineExceptionHandler) {
            contentLinkHandler.getContent(url)?.let { content ->
                binding.contentview.showContent(content)
            }
        }
    }
}