package com.devgary.contentcore.model.content

import com.devgary.contentcore.model.content.components.ContentSource
import com.devgary.contentcore.model.content.components.ContentType

open class Content(
    val source: ContentSource,
    val type: ContentType,
) {
    fun toLogString() =  "$type ${source.toLogString()}"
}

fun Content.areContentsTheSame(other: Content): Boolean {
    if (this == other) return true

    if (type != other.type) return false
    return source.areContentsTheSame(other.source)
}