package com.devgary.contentcore.model

class CollectionContent(
    url: String,
    private val collection: List<Content>,
) : Content(url = url, type = ContentType.COLLECTION), Collection {
    override fun getCollection(): List<Content> = collection
}