package com.devgary.contentcore.util

import android.util.Log
import com.devgary.contentcore.util.annotations.Hacky

val Any.TAG: String
    get() {
        val tag = javaClass.simpleName
        return if (tag.length <= 23) tag else tag.substring(0, 23)
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
