package com.devgary.contentview.util

import android.view.View
import android.widget.FrameLayout

/**
 * Sets gravity of view for when parent is a [FrameLayout]
 */
fun View.setGravityFrameLayoutParent(gravity: Int) {
    val params = FrameLayout.LayoutParams(
        /* width = */ FrameLayout.LayoutParams.MATCH_PARENT, 
        /* height = */ FrameLayout.LayoutParams.WRAP_CONTENT
    )
    params.gravity = gravity
    layoutParams = params
}

fun View.generateIdIfNotExists() {
    if (id == View.NO_ID) {
        id = View.generateViewId()
    }
}