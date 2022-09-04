package com.devgary.contentcore.util

import kotlin.reflect.KProperty0
import kotlin.reflect.jvm.isAccessible

/**
 * Returns true if a lazy property has been initialized or if the property is not lazy.
 */
val KProperty0<*>.isLazyInitialized: Boolean
    get() {
        // Prevent IllegalAccessException from JVM access check on private properties.
        val originalAccessLevel = isAccessible
        isAccessible = true
        val isLazyInitialized = (getDelegate() as? Lazy<*>)?.isInitialized() ?: true
        // Reset access level.
        isAccessible = originalAccessLevel
        return isLazyInitialized
    }

/**
 * Executes [action] if property [isLazyInitialized] is true.
 */
fun <T> KProperty0<T>.runIfLazyInitialized(action: () -> Unit) {
    if (isLazyInitialized) {
        action()
    }
}