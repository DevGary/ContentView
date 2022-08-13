package com.devgary.contentviewdemo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.devgary.contentcore.model.Content
import com.devgary.contentcore.model.ContentType
import com.devgary.contentviewdemo.util.cancel
import com.devgary.contentlinkapi.api.streamable.StreamableApi
import com.devgary.contentview.R
import com.devgary.contentview.databinding.ActivityDemoBinding
import kotlinx.coroutines.*

class DemoActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDemoBinding

    val streamableApi: StreamableApi by lazy { StreamableApi() }
    
    var coroutineJob: Job? = null
    private val coroutineExceptionHandler = CoroutineExceptionHandler { _, throwable ->
        Toast.makeText(
            /* context = */ this,
            /* text = */ "Error: ${throwable.cause?.message}",
            /* duration = */ Toast.LENGTH_LONG
        ).show()
    }
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDemoBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        
        binding.contentview.showContent(SampleContent.IMAGE_CONTENT)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_demo, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.menu_streamable -> {
                coroutineJob.cancel()
                coroutineJob = lifecycleScope.launch(coroutineExceptionHandler) {
                    val response = streamableApi.getVideo("hn8hq")
                    response.let {
                        val video = response.videos
                            .filter { v -> v.url.isNotEmpty() }
                            .minByOrNull { v -> v.bitrate }

                        video?.let {
                            binding.contentview.showContent(Content(video.url, ContentType.VIDEO))
                        }
                    }
                }
                return true
            }
        }
        
        when(item.itemId) {
            R.id.menu_image -> SampleContent.IMAGE_CONTENT
            R.id.menu_gif -> SampleContent.GIF_CONTENT
            R.id.menu_video -> SampleContent.MP4_VIDEO_CONTENT
            else -> return super.onOptionsItemSelected(item)
        }.let {
            binding.contentview.showContent(it)
            return true
        }
    }
}