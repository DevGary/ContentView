package com.devgary.testandroidcore

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

/**
 * Activity used for instrumentation tests. Avoid modifying.
 */
class TestActivity: AppCompatActivity() {
    companion object {
        var layoutId: Int = 0
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layoutId)
    }
}