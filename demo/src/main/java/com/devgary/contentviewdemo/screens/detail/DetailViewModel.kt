package com.devgary.contentviewdemo.screens.detail

import androidx.lifecycle.*
import com.devgary.contentcore.model.content.Content
import com.devgary.contentlinkapi.content.AbstractCompositeContentLinkHandler
import com.devgary.contentlinkapi.content.CompositeContentLinkHandler
import com.devgary.contentlinkapi.content.ContentLinkHandler
import com.devgary.contentlinkapi.content.ContentResult
import com.devgary.contentlinkapi.handlers.gfycat.GfycatContentLinkHandler
import com.devgary.contentlinkapi.handlers.imgur.ImgurContentLinkHandler
import com.devgary.contentlinkapi.handlers.streamable.StreamableContentLinkHandler
import com.devgary.contentviewdemo.BuildConfig
import com.devgary.contentviewdemo.DemoFallthroughContentLinkHandler
import com.devgary.contentviewdemo.screens.detail.model.DetailDataState
import com.devgary.contentviewdemo.screens.detail.model.mapper.DetailDataStateToDetailViewStateMapper
import com.devgary.contentviewdemo.util.cancel
import com.devgary.testcore.SampleContent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val detailDataStateToDetailViewStateMapper: DetailDataStateToDetailViewStateMapper,
) : ViewModel() {
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

    private val _detailDataState = MutableStateFlow<DetailDataState>(DetailDataState.Initial)

    val detailDataState = _detailDataState.map {
        detailDataStateToDetailViewStateMapper.map(it)
    }.asLiveData().distinctUntilChanged()

    private var getContentJob: Job? = null

    init {
        // TODO: Remove test code
        loadContent(SampleContent.IMAGE_CONTENT)
    }
    
    fun loadContent(url: String) {
        getContentJob.cancel()
        getContentJob = viewModelScope.launch {
            _detailDataState.value = DetailDataState.Loading

            val resultState = try {
                when (val contentResult = contentLinkHandler.getContent(url)) {
                    is ContentResult.Success -> {
                        DetailDataState.ShowContent(content = contentResult.content)
                    }

                    is ContentResult.Failure -> {
                        DetailDataState.Error(message = contentResult.error?.message)
                    }
                }
            } catch (e: CancellationException) {
                DetailDataState.Error(message = "Load Cancelled")
            } catch (e: Exception) {
                DetailDataState.Error(message = e.message)
            }

            _detailDataState.value = resultState
        }
    }
    
    fun cancelLoad() {
        getContentJob.cancel()
    }
    
    fun clearMemory() {
        contentLinkHandler.clearMemory()
    }
}