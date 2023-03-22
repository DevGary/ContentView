package com.devgary.contentcore.util

fun String?.isNotNullOrBlank(): Boolean {
    return !this.isNullOrBlank()
}

fun String?.defaultIfNullOrBlank(default: String): String {
    return if (isNullOrBlank()) default else this
}

fun String?.equalsIgnoreCase(value: String): Boolean {
    return this.equals(value, ignoreCase = true)
}

fun String.containsIgnoreCase(value: String): Boolean {
    return this.contains(value, ignoreCase = true)
}

/**
 * Return null if string [isNullOrBlank], otherwise returns string
 */
fun String?.nullIfBlank(): String? {
    return if (this.isNullOrBlank()) null else this
}

fun String.trim(maxLength: Int): String {
    return if (this.length <= maxLength) this
    else substring(0, kotlin.math.min(maxLength, this.length))
}