package com.devgary.contentlinkapi.components.webpageparser

import com.devgary.contentcore.util.nullIfBlank
import com.devgary.contentlinkapi.util.HttpBodyParsingUtils.parseFirstVideoElementSrc
import com.devgary.contentlinkapi.util.okhttp.suspend.executeAsSuspend
import okhttp3.OkHttpClient
import okhttp3.Request

object WebpageParser {
    /**
     * Makes Http Request to [url] and parses webpage response body for a video url
     */
    suspend fun parseVideoUrlFromWebpage(url: String): String? {
        val responseBody = getWebpageSuccessResponseBody(url)
        val videoUrl = responseBody?.parseFirstVideoElementSrc()

        return videoUrl.nullIfBlank()
    }

    private suspend fun getWebpageSuccessResponseBody(url: String): String? {
        val client = OkHttpClient()
        val request = Request.Builder()
            .url(url)
            .build()

        client.newCall(request).executeAsSuspend().let { response ->
            if (response.isSuccessful) {
                return response.body()?.string()
            }
        }

        return null
    }
}