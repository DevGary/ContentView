package com.devgary.contentlinkapi.components.imgur.api

import android.util.Log
import com.devgary.contentcore.util.TAG
import com.devgary.contentcore.util.name
import com.devgary.contentlinkapi.components.imgur.api.model.ImgurAlbum
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

class ImgurClient(
    private val authorizationHeader: String,
    private val mashapeKey: String,
) {
    private val imgurEndpoint: ImgurEndpoint
    private val BASE_URL = "https://imgur-apiv3.p.rapidapi.com/3/"

    private val cachedAlbumItems: MutableMap<String, ImgurAlbum> by lazy { HashMap() }
    
    init {
        val moshi = Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()

        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor{
                val request = it.request().newBuilder()
                    .header("Authorization", authorizationHeader)
                    .header("X-Mashape-Key", mashapeKey)
                    .header("content-type", "application/json")
                    .build()
                
                it.proceed(request)
            }.build()
        
        imgurEndpoint = Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl(BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
            .create(ImgurEndpoint::class.java)
    }

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