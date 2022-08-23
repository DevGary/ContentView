package com.devgary.contentlinkapi.components.imgur.api

import android.util.Log
import com.devgary.contentcore.util.TAG
import com.devgary.contentcore.util.name
import com.devgary.contentlinkapi.components.imgur.api.model.ImgurAlbum

class ImgurClient(private val imgurEndpoint: ImgurEndpoint) {
    private val cachedAlbumItems: MutableMap<String, ImgurAlbum> by lazy { HashMap() }

    suspend fun getImgur(albumId: String): ImgurAlbum {
        cachedAlbumItems[albumId]?.let {
            Log.i(TAG, "Returning cached ${name<ImgurAlbum>()} for albumId = $albumId")
            return it
        }

        // TODO: Handle if status not success
        imgurEndpoint.getImgurAlbum(albumId).album.also {
            cachedAlbumItems[albumId] = it
            Log.i(TAG, "Returning network ${name<ImgurAlbum>()} for albumId = $albumId")
            return it
        }
    }
}