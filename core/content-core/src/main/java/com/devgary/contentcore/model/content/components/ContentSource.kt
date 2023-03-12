package com.devgary.contentcore.model.content.components

sealed class ContentSource {
    abstract fun areContentsTheSame(other: ContentSource): Boolean
    
    class Url(val url: String) : ContentSource() {
        override fun areContentsTheSame(other: ContentSource): Boolean {
            if (this == other) return true

            return (other as? Url)?.let {
                this.url == other.url
            } ?: run {
                false
            }
        }
    }
    class Drawable(val drawable: android.graphics.drawable.Drawable) : ContentSource() {
        override fun areContentsTheSame(other: ContentSource): Boolean {
            if (this == other) return true

            return (other as? Drawable)?.let {
                this.drawable == other.drawable
            } ?: run {
                false
            }
        }
    }
    
    fun toLogString(): String {
        return when (this) {
            is Drawable -> drawable.toString()
            is Url -> url
        }
    }
}