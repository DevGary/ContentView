package com.devgary.contentcore.util

import android.util.Log
import com.devgary.contentcore.util.annotations.Hacky

val Any.TAG: String
    get() {
        val maxLength = 19

        val trimmedTag = if (!javaClass.isAnonymousClass) {
            val name = javaClass.simpleName
            if (name.length <= maxLength) name else name.substring(0, maxLength)
        } else {
            val name = javaClass.name
            if (name.length <= maxLength) name else name.substring(name.length - maxLength, name.length)
        }

        return trimmedTag + "@ID=" + hashCode().toString().substring(0, 4)
    }

/**
 * Tries to log name of function that is calling [logExecutedFunction]
 */
@Hacky
fun Any.logExecutedFunction(tag: String = TAG) {
    try {
        Log.d(tag, "Executed function: ${Thread.currentThread().stackTrace[4].methodName}()")
    }
    catch (_: Exception) {}
}
