package com.devgary.contentviewdemo.screens.basic

import androidx.lifecycle.*
import com.devgary.contentcore.model.content.Content
import com.devgary.contentlinkapi.content.AbstractCompositeContentLinkHandler
import com.devgary.contentlinkapi.content.CompositeContentLinkHandler
import com.devgary.contentlinkapi.content.ContentLinkHandler
import com.devgary.contentlinkapi.handlers.gfycat.GfycatContentLinkHandler
import com.devgary.contentlinkapi.handlers.imgur.ImgurContentLinkHandler
import com.devgary.contentlinkapi.handlers.streamable.StreamableContentLinkHandler
import com.devgary.contentviewdemo.BuildConfig
import com.devgary.contentviewdemo.DemoFallthroughContentLinkHandler
import com.devgary.contentviewdemo.util.cancel
import com.devgary.testcore.SampleContent
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class BasicViewModel : ViewModel() {
    private val contentLinkHandler: CompositeContentLinkHandler by lazy {
        object : AbstractCompositeContentLinkHandler() {
            override fun provideContentHandlers(): List<ContentLinkHandler> {
                return listOf(
                    GfycatContentLinkHandler(
                        clientId = BuildConfig.GFYCAT_CLIENT_ID,
                        clientSecret = BuildConfig.GFYCAT_CLIENT_SECRET
                    ),
                    ImgurContentLinkHandler(
                        authorizationHeader = BuildConfig.IMGUR_AUTHORIZATION_HEADER,
                        mashapeKey = BuildConfig.IMGUR_MASHAPE_KEY
                    ),
                    StreamableContentLinkHandler(),
                    DemoFallthroughContentLinkHandler()
                )
            }
        }
    }

    private var getContentJob: Job? = null
    private val coroutineExceptionHandler = CoroutineExceptionHandler { _, throwable ->
        _error.postValue(throwable.message)
    }

    private val _content = MutableLiveData<Content>()
    val content: LiveData<Content> = _content
    
    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error
    
    init {
        // TODO: Remove test code
        if (content.value == null) {
            loadContent(SampleContent.IMAGE_CONTENT)
        }
    }
    
    fun loadContent(url: String) {
        getContentJob.cancel()
        getContentJob = viewModelScope.launch(coroutineExceptionHandler) {
            contentLinkHandler.getContent(url)?.let { it ->
                _content.postValue(it)
            }
        }
    }
    
    fun clearMemory() {
        contentLinkHandler.clearMemory()
    }
}