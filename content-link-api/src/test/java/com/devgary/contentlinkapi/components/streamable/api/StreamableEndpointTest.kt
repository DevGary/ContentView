package com.devgary.contentlinkapi.components.streamable.api

import com.devgary.contentlinkapi.util.enqueue
import kotlinx.coroutines.runBlocking
import okhttp3.mockwebserver.MockWebServer
import org.junit.jupiter.api.*

class StreamableEndpointTest {
    private lateinit var endpoint: StreamableEndpoint
    private lateinit var mockWebServer: MockWebServer

    @BeforeEach
    fun setUp() {
        mockWebServer = MockWebServer()
        endpoint = StreamableEndpoint.create(baseUrl = mockWebServer.url("").toString())
    }

    @AfterEach
    fun tearDown() {
        mockWebServer.shutdown()
    }
    
    @Test
    @DisplayName("Given success json body, When getVideo() called, StreamableVideoResponse matches")
    fun getVideoSuccess() = runBlocking {
        mockWebServer.enqueue(StreamableMockResponses.GetVideo.STATUS_200)
        val response = endpoint.getVideo("")

        Assertions.assertEquals("Gravity Rush 2 - Lunar Style World Traversal", response.title)
        Assertions.assertEquals(2, response.videos.size)
    }
}