package com.devgary.contentcore.util

import android.view.View

fun View?.setHeight(height: Int) {
    this?.let {
        val params = layoutParams
        params.height = height
        layoutParams = params
    }
}

fun View?.setWidth(width: Int) {
    this?.let {
        val params = layoutParams
        params.height = width
        layoutParams = params
    }
}