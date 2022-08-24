package com.devgary.contentviewdemo

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.devgary.contentlinkapi.components.gfycat.GfycatContentLinkHandler
import com.devgary.contentlinkapi.components.imgur.ImgurContentLinkHandler
import com.devgary.contentlinkapi.components.streamable.StreamableContentLinkHandler
import com.devgary.contentlinkapi.content.AbstractContentLinkApi
import com.devgary.contentlinkapi.content.ContentLinkHandler
import com.devgary.contentviewdemo.databinding.ActivityDemoBinding
import com.devgary.contentviewdemo.util.cancel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class DemoActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDemoBinding

    private val contentLinkApi: ContentLinkHandler by lazy {
        object : AbstractContentLinkApi() {
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
            R.id.menu_streamable -> SampleContent.STREAMABLE_URL
            R.id.menu_gfycat_video -> SampleContent.GFYCAT_URL
            R.id.menu_imgur_album -> SampleContent.IMGUR_ALBUM_GALLERY_URL
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
            contentLinkApi.getContent(url)?.let { content ->
                binding.contentview.showContent(content)
            }
        }
    }
}