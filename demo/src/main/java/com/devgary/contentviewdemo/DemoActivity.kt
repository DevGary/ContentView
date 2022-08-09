package com.devgary.contentviewdemo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.devgary.contentview.R
import com.devgary.contentview.databinding.ActivityDemoBinding

class DemoActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDemoBinding

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