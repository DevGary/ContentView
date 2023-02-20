package com.devgary.contentcore.model.content.components

sealed class ContentSource {
    class Url(val url: String) : ContentSource()
    class Drawable(val drawable: android.graphics.drawable.Drawable) : ContentSource()
    
    fun toLog(): String {
        return when (this) {
            is Drawable -> drawable.toString()
            is Url -> url
        }
    }
}