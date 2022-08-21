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
