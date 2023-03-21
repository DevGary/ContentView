package com.devgary.contentview

import android.util.Log
import android.view.View
import com.devgary.contentcore.util.TAG
import com.devgary.contentcore.util.name
import com.devgary.contentcore.util.removeFromParentView
import java.lang.IllegalArgumentException

class ViewPool(val viewCreator: () -> View) {
    data class ViewPoolMetadata(var lastUsageMillis: Long, var isInUse: Boolean)
    
    interface Listener {
        fun onViewRecycled(view: View)
    }

    var maxSize = 1
        set(value) {
            if (value < 1) {
                throw IllegalArgumentException("${name<ViewPool>()} max size cannot be less than 1")
            }
            field = value
        }
    
    private val viewPool: MutableMap<View, ViewPoolMetadata> = mutableMapOf()
    private val listeners: MutableSet<Listener> = mutableSetOf()
    
    private fun shouldCreateView(): Boolean {
        return viewPool.size < maxSize
    }
    
    fun getView(): View {
        val viewToUse = if (shouldCreateView()) {
            Log.v(TAG, "Creating a new View")
            viewCreator().also {
                addView(it)
            }
        } else {
            Log.v(TAG, "Returning an existing view from the pool")
            val sortedViewPoolMetadata = viewPool.values
                .sortedWith(
                    compareBy<ViewPoolMetadata> {
                        it.isInUse
                    }.thenBy {
                        it.lastUsageMillis
                    }
                )

            val viewPoolMetadata = sortedViewPoolMetadata.first()
            
            val recycledViewToUse = viewPool.keys.first { key -> viewPool[key] == viewPoolMetadata }
            
            onViewRecycled(recycledViewToUse)
            
            recycledViewToUse
        }

        setViewUsed(viewToUse)

        return viewToUse
    }

    private fun addView(view: View) {
        viewPool[view] = ViewPoolMetadata(
            lastUsageMillis = 0,
            isInUse = false
        )
    }

    private fun setViewUsed(view: View) {
        viewPool[view]?.apply {
            lastUsageMillis = System.currentTimeMillis()
            isInUse = true
        }
    } 
    
    private fun setViewUnused(view: View) {
        viewPool[view]?.apply {
            isInUse = false
        }
    }
    
    private fun onViewRecycled(view: View) {
        listeners.forEach { l -> l.onViewRecycled(view) }
    }
    
    fun recycleView(view: View) {
        setViewUnused(view)
        view.removeFromParentView()
        onViewRecycled(view)
    }
    
    fun addListener(listener: Listener) {
        listeners.add(listener)
    }
}