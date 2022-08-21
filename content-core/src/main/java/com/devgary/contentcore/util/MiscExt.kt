package com.devgary.contentcore.util

/**
 * Miscellaneous extension functions that probably don't need its own class for now
 */

val Any.TAG: String
    get() {
        val tag = javaClass.simpleName
        return if (tag.length <= 23) tag else tag.substring(0, 23)
    }

/**
 * Shortcut function to return [Class.getSimpleName] of [Class] provided by generic
 */
inline fun <reified T> name(): String {
    val clazz: Class<*> = T::class.java
    return clazz.simpleName
}

/**
 * Returns [String] in format: 
 * 
 * [Class.getSimpleName] of [value] = [value].toString()
 * 
 * Example:
 * 
 * Call this function on the [Integer] value of 23 would return "Integer = 23"
 */
inline fun <reified T> classNameWithValue(value: T): String {
    val clazz: Class<*> = T::class.java
    return clazz.simpleName + " = " + value
}