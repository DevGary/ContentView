package com.devgary.contentviewdemo.screens.list

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.devgary.contentlinkapi.content.ContentLinkHandler
import com.devgary.contentview.ui.ContentView
import com.devgary.contentviewdemo.util.cancel
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
                "https://gfycat.com/FoolishCompetentAegeancat",
                "https://streamable.com/hwa6l",
                "https://streamable.com/m/kevin-gausman-in-play-run-s-to-mike-zunino",
                "https://i.redd.it/bco4cqkcv8q91.jpg",
                "https://gfycat.com/cookedbadasianwaterbuffalo",
                "https://gfycat.com/equatorialconcernedfinch",
            )
        }
    }

    fun loadContent(url: String, contentView: ContentView) {
        getContentJob.cancel()
        getContentJob = viewModelScope.launch(coroutineExceptionHandler) {
            contentLinkHandler.getContent(url)?.let { it ->
                contentView.showContent(it)
            }
        }
    }  
}