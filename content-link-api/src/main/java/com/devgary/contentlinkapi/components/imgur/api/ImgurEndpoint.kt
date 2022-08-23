package com.devgary.contentlinkapi.components.imgur.api

import com.devgary.contentlinkapi.components.imgur.api.model.ImgurAlbumResponse
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path

interface ImgurEndpoint {
    @GET("album/{id}")
    suspend fun getImgurAlbum(@Path("id") id: String): ImgurAlbumResponse

    companion object {
        fun create(
            authorizationHeader: String,
            mashapeKey: String,
            baseUrl: String = "https://imgur-apiv3.p.rapidapi.com/3/",
        ): ImgurEndpoint {
            val moshi = Moshi.Builder()
                .add(KotlinJsonAdapterFactory())
                .build()

            val okHttpClient = OkHttpClient.Builder()
                .addInterceptor {
                    val request = it.request().newBuilder()
                        .header("Authorization", authorizationHeader)
                        .header("X-Mashape-Key", mashapeKey)
                        .header("content-type", "application/json")
                        .build()

                    it.proceed(request)
                }.build()

            return Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl(baseUrl)
                .addConverterFactory(MoshiConverterFactory.create(moshi))
                .build()
                .create(ImgurEndpoint::class.java)
        }
    }
}