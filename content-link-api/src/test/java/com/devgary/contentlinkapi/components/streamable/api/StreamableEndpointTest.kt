package com.devgary.contentlinkapi.components.streamable.api

import com.devgary.contentlinkapi.components.streamable.api.model.StreamableVideoResponse
import com.devgary.contentlinkapi.util.GenericMockResponses
import com.devgary.contentlinkapi.util.enqueue
import com.squareup.moshi.JsonDataException
import kotlinx.coroutines.runBlocking
import okhttp3.mockwebserver.MockWebServer
import okhttp3.mockwebserver.RecordedRequest
import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.*

class StreamableEndpointTest {
    private lateinit var endpoint: StreamableEndpoint
    private lateinit var server: MockWebServer

    @BeforeEach
    fun setUp() {
        server = MockWebServer()
        endpoint = StreamableEndpoint.create(baseUrl = server.url("").toString())
    }

    @AfterEach
    fun tearDown() {
        server.shutdown()
    }

    @Nested
    @DisplayName("Given success json")
    inner class GivenSuccessJson {

        @BeforeEach
        fun setup() {
            server.enqueue(StreamableMockResponses.GetVideo.STATUS_SUCCESS)
        }

        @Nested
        @DisplayName("When getVideo() called")
        inner class WhenGetVideoCalled {
            private lateinit var response: StreamableVideoResponse
            private lateinit var request: RecordedRequest
            private var shortCode = "testShortcode"

            @BeforeEach
            fun setup() = runBlocking {
                response = endpoint.getVideo(shortCode)
                request = server.takeRequest()
            }
            
            @Test
            @DisplayName("Then request path matches")
            fun requestPathMatches() {
                assertEquals("/videos/$shortCode", request.path)
            }
            
            @Test
            @DisplayName("Then title matches")
            fun titleMatches() {
                assertEquals("Gravity Rush 2 - Lunar Style World Traversal", response.title)
            }

            @Test
            @DisplayName("Then number of video matches")
            fun videosSizeMatches() {
                assertEquals(2, response.videos.size)
            }

            @Test
            @DisplayName("Then mp4-mobile video exists")
            fun containsMp4MobileVideo() {
                assertEquals(1, response.videos.count { video -> video.url == "//cdn-b-east.streamable.com/video/mp4-mobile/hn8hq.mp4?token=YDcwufloEg8_KL0vnx4b6A&expires=1591923000" })
            }
            
            @Test
            @DisplayName("Then mp4 video exists")
            fun containsMp4Video() {
                assertEquals(1, response.videos.count { video -> video.url == "//cdn-b-east.streamable.com/video/mp4/hn8hq.mp4?token=EEioHptvKoLeACuujevHbQ&expires=1591923000" })
            }
        }
    }

    @Test
    @DisplayName("Given invalid json, When getVideo() called, Then throws JsonDataException")
    fun getVideoInvalid(): Unit = runBlocking {
        server.enqueue(GenericMockResponses.INVALID)
        val exception = assertThrows<JsonDataException> {
            val response = endpoint.getVideo("")
        }
    }
}