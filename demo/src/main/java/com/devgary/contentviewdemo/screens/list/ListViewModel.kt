package com.devgary.contentviewdemo.screens.list

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.devgary.contentlinkapi.content.ContentLinkHandler
import com.devgary.contentlinkapi.content.ContentResult
import com.devgary.contentview.ui.ContentView
import com.devgary.contentviewdemo.util.cancel
import com.devgary.testcore.SampleContent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ListViewModel @Inject constructor(
    private val contentLinkHandler: ContentLinkHandler,
) : ViewModel() {

    private var getContentJob: Job? = null
    private val coroutineExceptionHandler = CoroutineExceptionHandler { _, throwable ->
        _error.postValue(throwable.message)
    }

    private val _urls = MutableLiveData<List<String>>()
    val urls: LiveData<List<String>> = _urls

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error

    init {
        if (_urls.value == null) {
            _urls.value = listOf(
                "https://i.imgur.com/pRaLUY1.jpg",
                SampleContent.STREAMABLE.BASIC_URL,
                SampleContent.STREAMABLE.HLS_URL,
                "https://i.redd.it/bco4cqkcv8q91.jpg",
                "https://i.imgur.com/ORUbOpd.jpg",
                "https://i.imgur.com/O3i9yR4.jpg",
                "https://i.imgur.com/M0ggypg.jpg",
                "https://i.imgur.com/q4Hb0rw.jpg",
                "https://streamable.com/i7wmij",
                "https://streamable.com/vp67j",
                "https://imgur.com/gallery/JmMtAjN",
            )
        }
    }

    fun loadContent(url: String, contentView: ContentView) {
        getContentJob.cancel()
        getContentJob = viewModelScope.launch(coroutineExceptionHandler) {
            when (val contentResult = contentLinkHandler.getContent(url)) {
                is ContentResult.Success -> {
                    contentView.showContent(contentResult.content)
                }

                is ContentResult.Failure -> {
                    _error.postValue(contentResult.error?.message ?: "Error")
                }
            }
        }
    }  
}