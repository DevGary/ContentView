package com.devgary.contentlinkapi.handlers.imgur.api

import android.util.Log
import androidx.collection.LruCache
import com.devgary.contentcore.util.TAG
import com.devgary.contentcore.util.name
import com.devgary.contentcore.util.nullIfBlank
import com.devgary.contentcore.util.runIfLazyInitialized
import com.devgary.contentlinkapi.Constants
import com.devgary.contentlinkapi.handlers.ApiException
import com.devgary.contentlinkapi.handlers.imgur.api.model.ImgurAlbum

class ImgurClient(private val imgurEndpoint: ImgurEndpoint) {
    private val cachedAlbumResponses: LruCache<String, ImgurAlbum>
            by lazy { LruCache(/* maxSize = */ Constants.LRU_CACHE_DEFAULT_SIZE) }

    suspend fun getImgur(albumId: String): ImgurAlbum {
        cachedAlbumResponses[albumId]?.let {
            Log.i(TAG, "Returning cached ${name<ImgurAlbum>()} for albumId = $albumId")
            return it
        }

        imgurEndpoint.getImgurAlbum(albumId).also {
            if (it.success && it.album.error.nullIfBlank() == null) {
                cachedAlbumResponses.put(albumId, it.album)
                Log.i(TAG, "Returning network ${name<ImgurAlbum>()} for albumId = $albumId")
                return it.album
            }
            else {
                throw ApiException(it.album.error.nullIfBlank())
            }
        }
    }

    fun clearMemory() {
        this::cachedAlbumResponses.runIfLazyInitialized {
            Log.i(TAG,"Cleared ${cachedAlbumResponses.size()} ${name<ImgurAlbum>()} from memory cache")
            cachedAlbumResponses.evictAll()
        }
    }
}