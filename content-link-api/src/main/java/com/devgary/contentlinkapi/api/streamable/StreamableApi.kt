package com.devgary.contentlinkapi.api.streamable

import android.util.Log
import com.devgary.contentcore.util.TAG
import com.devgary.contentcore.util.name
import com.devgary.contentlinkapi.util.RxAndroidUtils
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import hu.akarnokd.rxjava3.retrofit.RxJava3CallAdapterFactory
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.schedulers.Schedulers
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

class StreamableApi {
    private val BASE_URL = "https://api.streamable.com/"
    
    private val cachedStreamableVideoResponses: MutableMap<String, StreamableVideoResponse> = HashMap()
    private val streamableEndpoint: StreamableEndpoint

    init {
        val moshi = Moshi.Builder()
            .add(StreamableVideoResponseAdapter())
            .add(KotlinJsonAdapterFactory())
            .build()
        
        streamableEndpoint = Retrofit.Builder()
            .client(OkHttpClient())
            .baseUrl(BASE_URL)
            .addCallAdapterFactory(RxJava3CallAdapterFactory.createWithScheduler(Schedulers.io()))
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
            .create(StreamableEndpoint::class.java)
    }

    fun getVideos(shortCode: String): Observable<StreamableVideoResponse> {
        cachedStreamableVideoResponses[shortCode]?.let {
            Log.i(TAG,"Returning cached ${name<StreamableVideoResponse>()}} for shortCode = $shortCode")
            return Observable.just(it)
        }
  
        return Observable
            .defer { streamableEndpoint.getVideo(shortCode) }
            .doOnNext { streamableVideoEndpointResponse ->
                Log.i(TAG,"Returning network ${name<StreamableVideoResponse>()}} for shortCode = $shortCode")
                cachedStreamableVideoResponses[shortCode] = streamableVideoEndpointResponse
            }
            .compose(RxAndroidUtils.setupObservable())
    }
}