package com.devgary.contentlinkapi.content

import com.devgary.contentcore.model.content.Content

sealed interface ContentResult {
    data class Success(val content: Content) : ContentResult
    data class Failure(val error: Throwable?) : ContentResult
}