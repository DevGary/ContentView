package com.devgary.contentcore.model.content

import com.devgary.contentcore.model.content.components.ContentType
import com.devgary.contentcore.model.content.components.Collection
import com.devgary.contentcore.model.content.components.ContentSource

class CollectionContent(
    source: ContentSource,
    private val collection: List<Content>,
) : Content(source = source, type = ContentType.COLLECTION), Collection {
    override fun getCollection(): List<Content> = collection
}