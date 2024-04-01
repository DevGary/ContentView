package com.devgary.contentviewdemo.screens.detail.model.mapper

import com.devgary.contentviewdemo.screens.detail.model.DetailDataState
import com.devgary.contentviewdemo.screens.detail.model.DetailViewState
import javax.inject.Inject

class DetailDataStateToDetailViewStateMapper @Inject constructor() {
    fun map(detailDataState: DetailDataState): DetailViewState {
        return when (detailDataState) {
            DetailDataState.Initial -> {
                DetailViewState(
                    loadingVisibility = false,
                    contentVisibility = false,
                    errorVisibility = false,
                    errorText = null,
                    content = null,
                )
            }
            DetailDataState.Loading -> {
                DetailViewState(
                    loadingVisibility = true,
                    contentVisibility = false,
                    errorVisibility = false,
                    errorText = null,
                    content = null,
                )
            }

            is DetailDataState.Error -> {
                DetailViewState(
                    loadingVisibility = false,
                    contentVisibility = false,
                    errorVisibility = true,
                    errorText = detailDataState.message ?: "Error",
                    content = null,
                )
            }

            is DetailDataState.ShowContent -> {
                DetailViewState(
                    loadingVisibility = false,
                    contentVisibility = true,
                    errorVisibility = false,
                    errorText = null,
                    content = detailDataState.content,
                )
            }
        }
    }
}