package com.devgary.contentlinkapi.components.imgur.api

import com.devgary.contentlinkapi.components.imgur.api.model.ImgurAlbumResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface ImgurEndpoint {
    @GET("album/{id}")
    suspend fun getImgurAlbum(@Path("id") id: String): ImgurAlbumResponse
}