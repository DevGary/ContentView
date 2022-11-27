package com.devgary.contentviewdemo.di

import com.devgary.contentlinkapi.content.AbstractCompositeContentLinkHandler
import com.devgary.contentlinkapi.content.ContentLinkHandler
import com.devgary.contentlinkapi.handlers.FallthroughContentLinkHandler
import com.devgary.contentlinkapi.handlers.gfycat.GfycatContentLinkHandler
import com.devgary.contentlinkapi.handlers.imgur.ImgurContentLinkHandler
import com.devgary.contentlinkapi.handlers.streamable.StreamableContentLinkHandler
import com.devgary.contentviewdemo.BuildConfig
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class ApplicationModule {
    
    @Provides
    @Singleton
    fun provideContentLinkHandler(): ContentLinkHandler {
        return object : AbstractCompositeContentLinkHandler() {
            override fun provideContentHandlers(): List<ContentLinkHandler> {
                return listOf(
                    GfycatContentLinkHandler(
                        clientId = BuildConfig.GFYCAT_CLIENT_ID,
                        clientSecret = BuildConfig.GFYCAT_CLIENT_SECRET
                    ),
                    ImgurContentLinkHandler(
                        authorizationHeader = BuildConfig.IMGUR_AUTHORIZATION_HEADER,
                        mashapeKey = BuildConfig.IMGUR_MASHAPE_KEY
                    ),
                    StreamableContentLinkHandler(),
                    FallthroughContentLinkHandler()
                )
            }
        }
    }
}