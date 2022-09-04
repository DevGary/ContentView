package com.devgary.contentlinkapi.util

import com.devgary.testcore.TestException
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okio.buffer
import okio.source

fun MockWebServer.enqueue(mockResponsePath: String) {
    val inputStream = this.javaClass.classLoader?.getResourceAsStream(mockResponsePath)
    val bufferSource = inputStream?.source()?.buffer() ?: throw TestException()

    this.enqueue(
        MockResponse().setBody(
            bufferSource.readString(Charsets.UTF_8)
        )
    )
}