package com.devgary.contentview.model

enum class ScaleType {
    /**
     * Fills width of parent, cropping to maintain aspect ratio
     */
    FILL_WIDTH,

    /**
     * Fits the size of parent, shrinking to maintain aspect ratio
     */
    FIT_CENTER,
}