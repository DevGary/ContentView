package com.devgary.contentcore.model.content

import com.devgary.contentcore.model.content.components.ContentSource
import com.devgary.contentcore.model.content.components.ContentType

open class Content(
    val source: ContentSource,
    val type: ContentType,
)