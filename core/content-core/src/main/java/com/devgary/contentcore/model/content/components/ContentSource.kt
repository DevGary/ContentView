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
    
    class Drawable(val drawableResId: Int) : ContentSource() {
        override fun areContentsTheSame(other: ContentSource): Boolean {
            if (this == other) return true

            return (other as? Drawable)?.let {
                this.drawableResId == other.drawableResId
            } ?: run {
                false
            }
        }
    }

    object Empty : ContentSource() {
        override fun areContentsTheSame(other: ContentSource) = true
    }
    
    fun toLogString(): String {
        return when (this) {
            is Drawable -> drawableResId.toString()
            is Url -> url
            is Empty -> "EMPTY"
        }
    }
}