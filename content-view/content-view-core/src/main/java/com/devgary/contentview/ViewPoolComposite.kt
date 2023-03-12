package com.devgary.contentview

import kotlin.reflect.KClass

class ViewPoolComposite {
    private val viewPools = mutableMapOf<KClass<out ContentHandler>, ViewPool>()

    fun getOrCreatePool(clazz: KClass<out ContentHandler>, viewPoolCreator: () -> ViewPool): ViewPool {
        viewPools[clazz]?.let { existingPool ->
            return existingPool
        } ?: run {
            val newPool = viewPoolCreator()
            viewPools[clazz] = newPool
            return newPool
        }
    }
}