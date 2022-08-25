package com.devgary.contentlinkapi.components.imgur.api

import android.util.Log
import androidx.collection.LruCache
import com.devgary.contentcore.util.TAG
import com.devgary.contentcore.util.name
import com.devgary.contentcore.util.runIfLazyInitialized
import com.devgary.contentlinkapi.Constants
import com.devgary.contentlinkapi.components.imgur.api.model.ImgurAlbum

class ImgurClient(private val imgurEndpoint: ImgurEndpoint) {
    private val cachedAlbumResponses: LruCache<String, ImgurAlbum>
            by lazy { LruCache(/* maxSize = */ Constants.LRU_CACHE_DEFAULT_SIZE) }

    suspend fun getImgur(albumId: String): ImgurAlbum {
        cachedAlbumResponses[albumId]?.let {
            Log.i(TAG, "Returning cached ${name<ImgurAlbum>()} for albumId = $albumId")
            return it
        }

        // TODO: Handle if status not success
        imgurEndpoint.getImgurAlbum(albumId).album.also {
            cachedAlbumResponses.put(albumId, it)
            Log.i(TAG, "Returning network ${name<ImgurAlbum>()} for albumId = $albumId")
            return it
        }
    }

    fun clearMemory() {
        this::cachedAlbumResponses.runIfLazyInitialized {
            Log.i(TAG,"Cleared ${cachedAlbumResponses.size()} ${name<ImgurAlbum>()} from memory cache")
            cachedAlbumResponses.evictAll()
        }
    }
}