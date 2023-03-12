package com.devgary.contentview

import kotlin.reflect.KClass

class ViewPoolComposite {
    private val viewPools = mutableMapOf<KClass<out ContentHandler>, ViewPool>()
    private val viewPoolMaxSizeConfig = mutableMapOf<KClass<out ContentHandler>, Int>()

    fun getOrCreatePool(clazz: KClass<out ContentHandler>, viewPoolCreator: () -> ViewPool): ViewPool {
        val viewPool = viewPools[clazz]?.let { existingPool ->
            existingPool
        } ?: run {
            val newPool = viewPoolCreator()
            viewPools[clazz] = newPool
            newPool
        }

        getPoolMaxSize(clazz)?.let {
            viewPool.maxSize = it
        }
        
        return viewPool
    }
    
    fun setPoolMaxSize(poolType: KClass<out ContentHandler>, maxSize: Int) {
        viewPoolMaxSizeConfig[poolType] = maxSize
        viewPools[poolType]?.maxSize = maxSize
    }

    private fun getPoolMaxSize(poolType: KClass<out ContentHandler>): Int? {
        return viewPoolMaxSizeConfig[poolType]
    }
}