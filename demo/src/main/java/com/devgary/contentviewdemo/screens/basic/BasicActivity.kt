package com.devgary.contentviewdemo.screens.basic

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.devgary.contentviewdemo.databinding.ActivityBasicBinding

class BasicActivity : AppCompatActivity() {
    private lateinit var binding: ActivityBasicBinding
   
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBasicBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
    }
}