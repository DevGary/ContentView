package com.devgary.contentviewdemo.screens.detail.model

import com.devgary.contentcore.model.content.Content

sealed interface DetailDataState {
    object Initial: DetailDataState
    
    object Loading: DetailDataState
    
    data class ShowContent(val content: Content): DetailDataState
    
    data class Error(val message: String?): DetailDataState
}