package com.devgary.contentcore.util

import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import androidx.core.view.contains

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

fun View.visibleOrGone(visibleOrGone: Boolean) {
    visibility = if (visibleOrGone) View.VISIBLE else View.GONE
}

/**
 * Executes [func] upon view laid out and visible
 */
fun View.waitForLayout(func: () -> Unit) = with(viewTreeObserver) {
    addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
        override fun onGlobalLayout() {
            if (measuredHeight > 0 && measuredWidth > 0) {
                removeOnGlobalLayoutListener(this)
                func()
            }
        }
    })
}

fun View.removeFromParentView() {
    (this.parent as? ViewGroup)?.let { parent ->
        parent.removeView(this)
    }
}

fun ViewGroup.addViewIfNotExist(viewToAdd: View) {
    if (contains(viewToAdd)) return
    viewToAdd.removeFromParentView()
    addView(viewToAdd)
}