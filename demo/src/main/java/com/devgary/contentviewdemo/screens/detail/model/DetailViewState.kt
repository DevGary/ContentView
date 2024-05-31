package com.devgary.contentviewdemo.screens.detail.model

import com.devgary.contentcore.model.content.Content

data class DetailViewState(
    val loadingVisibility: Boolean,
    val errorVisibility: Boolean,
    val errorText: String?,
    val contentVisibility: Boolean,
    val content: Content?,
)