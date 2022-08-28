package com.devgary.contentview.interfaces

interface PlayPausable {
    fun play()
    fun pause()

    /**
     * Whether [PlayPausable] content should autoplay when ready or only play when [play] called
     */
    fun setAutoplay(autoplay: Boolean)
}