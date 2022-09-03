package com.devgary.contentcore.util

import android.widget.TextView

/**
 * Sets [text] to [TextView] and sets visibility to [TextView.GONE] if [text] is null or empty
 * and [TextView.VISIBLE] otherwise
 */
fun TextView.setTextAndVisibility(text: String?) {
    visibleOrGone(!text.isNullOrEmpty())
    this.text = text
}