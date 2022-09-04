package com.devgary.contentlinkapi.util.okhttp.suspend

import kotlinx.coroutines.suspendCancellableCoroutine
import okhttp3.Call
import okhttp3.Response

/**
 * Executes OkHttp call as a suspend function
 * 
 * @see <a href="https://github.com/coil-kt/coil/blob/0af5fe016971ba54518a24c709feea3a1fc075eb/coil-base/src/main/java/coil/util/Extensions.kt#L45-L51">Source from Coil lib</a>
 */
internal suspend inline fun Call.executeAsSuspend(): Response {
    return suspendCancellableCoroutine { continuation ->
        val callback = ContinuationCallback(this, continuation)
        enqueue(callback)
        continuation.invokeOnCancellation(callback)
    }
}