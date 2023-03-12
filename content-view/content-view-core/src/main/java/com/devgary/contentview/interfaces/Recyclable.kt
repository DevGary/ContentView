package com.devgary.contentview.interfaces

import com.devgary.contentview.ViewPool

interface Recyclable {
    fun recycle()
    
    fun getOrCreateViewPool(): ViewPool
    
    fun setViewPool(viewPool: ViewPool)
}