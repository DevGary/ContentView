package com.devgary.contentcore.model.content.components

import com.devgary.contentcore.model.content.Content

interface Collection {
    fun getCollection(): List<Content>
}