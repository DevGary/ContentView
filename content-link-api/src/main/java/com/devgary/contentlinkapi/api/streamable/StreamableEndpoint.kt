package com.devgary.contentlinkapi.api.streamable

import io.reactivex.rxjava3.core.Observable
import retrofit2.http.GET
import retrofit2.http.Path

interface StreamableEndpoint {
    @GET("/videos/{shortcode}")
    fun getVideo(@Path("shortcode") shortCode: String): Observable<StreamableVideoResponse>
}