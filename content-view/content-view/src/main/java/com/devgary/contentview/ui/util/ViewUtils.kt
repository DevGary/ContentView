package com.devgary.contentview.ui.util

import android.graphics.Rect
import android.view.View

// TODO [!]: Convert some to extension functions after RecyclerViewUtils converted to Kotlin
fun viewIsPartiallyHiddenBottom(v: View): Boolean {
    val rect = Rect()
    v.getLocalVisibleRect(rect)
    return rect.bottom > 0 && rect.bottom < v.height
}

fun viewIsPartiallyHiddenTop(v: View): Boolean {
    val localVisibleRect = Rect()
    v.getLocalVisibleRect(localVisibleRect)
    return localVisibleRect.top > 0
}

/**
 * Gets absolute top of view including cut off portions
 *
 * @param v
 * @return
 */
fun getAbsoluteTop(v: View): Int {
    val globalVisibleRect = Rect()
    v.getGlobalVisibleRect(globalVisibleRect)
    return if (viewIsPartiallyHiddenTop(v)) {
        globalVisibleRect.bottom - v.height
    } else {
        globalVisibleRect.top
    }
}

/**
 * Gets absolute bottom of view including cut off portions
 *
 * @param v
 * @return
 */
fun getAbsoluteBottom(v: View): Int {
    val globalVisibleRect = Rect()
    v.getGlobalVisibleRect(globalVisibleRect)
    return if (viewIsPartiallyHiddenBottom(v)) {
        globalVisibleRect.top + v.height
    } else {
        globalVisibleRect.bottom
    }
}

fun getPercentVisible(view: View): Int {
    var percentVisible = 100
    val localVisibleRect = Rect()
    view.getLocalVisibleRect(localVisibleRect)
    val height = view.height
    if (viewIsPartiallyHiddenTop(view)) {
        percentVisible = (height - localVisibleRect.top) * 100 / height
    } else if (viewIsPartiallyHiddenBottom(view)) {
        percentVisible = localVisibleRect.bottom * 100 / height
    } else if (!view.getLocalVisibleRect(Rect())) {
        percentVisible = 0
    }
    return percentVisible
}
