package com.devgary.contentcore.model.content

import com.devgary.contentcore.model.content.components.ContentSource
import com.devgary.contentcore.model.content.components.ContentType

class DefaultContentCreator {
    companion object {
        fun createVideoContent(videoUrl: String, thumbnailUrl: String?): Content {
            val videoContent = Content(ContentSource.Url(videoUrl), ContentType.VIDEO)
            return thumbnailUrl?.let {
                val thumbnailContent = Content(ContentSource.Url(it), ContentType.IMAGE)
                ActivatableContent(
                    contentWhenNotActivated = thumbnailContent,
                    contentWhenActivated = videoContent
                )
            } ?: run { 
                videoContent
            }
        }
    }
}