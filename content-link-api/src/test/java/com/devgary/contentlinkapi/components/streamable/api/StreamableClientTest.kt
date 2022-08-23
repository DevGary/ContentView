package com.devgary.contentlinkapi.components.streamable.api

import com.devgary.contentlinkapi.components.streamable.api.model.StreamableVideoResponse
import io.mockk.*
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.*

internal class StreamableClientTest {
    private lateinit var streamableClient: StreamableClient

    private var streamableEndpoint = mockk<StreamableEndpoint>()

    private val validShortcode = "validShortcode"
    private val validUrl = "https://validurl.com/valid"
    
    private val videoEndpointFakeData = mapOf(
        validShortcode to StreamableVideoResponse(
            url = validUrl
        ),
    )
       
    @BeforeEach
    fun setup() {
        MockKAnnotations.init(this)
        
        coEvery { streamableEndpoint.getVideo(any()) } answers {
            videoEndpointFakeData[firstArg()]!!
        } 
        
        streamableClient = StreamableClient(streamableEndpoint)
    }

    @Nested
    @DisplayName("Given shortCode for valid response")
    inner class GivenValidShortCode {
        val shortCode = validShortcode

        @Nested
        @DisplayName("When getVideo() called")
        inner class WhenGetVideoCalled {
            private lateinit var response: StreamableVideoResponse
            
            @BeforeEach
            fun setup() = runBlocking {
                response = streamableClient.getVideo(shortCode)
            }
            
            @Test
            @DisplayName("Then StreamableEndpoint.getVideos() called 1 times with shortCode")
            fun calledWithShortcode() {
                coVerify(exactly = 1) { streamableEndpoint.getVideo(shortCode) }
            }

            @Test
            @DisplayName("Then url is valid")
            fun urlIsValid() {
                Assertions.assertEquals(validUrl, response.url)
            }
        }

        @Nested
        @DisplayName("When getVideo() called multiple times")
        inner class WhenGetVideoCalledMultipleTimes {
            private lateinit var response1: StreamableVideoResponse
            private lateinit var response2: StreamableVideoResponse

            @BeforeEach
            fun setup() = runBlocking {
                response1 = streamableClient.getVideo(shortCode)
                response2 = streamableClient.getVideo(shortCode)
            }

            @Test
            @DisplayName("Then StreamableEndpoint.getVideos() called 1 times with shortCode (cache test)")
            fun calledWithShortcode() {
                coVerify(exactly = 1) { streamableEndpoint.getVideo(shortCode) }
            }

            @Test
            @DisplayName("Then url is valid")
            fun urlIsValid() {
                Assertions.assertEquals(validUrl, response1.url)
            }

            @Test
            @DisplayName("Then responses match")
            fun responsesMatch() {
                Assertions.assertEquals(response1, response2)
            }
        }
    }
}