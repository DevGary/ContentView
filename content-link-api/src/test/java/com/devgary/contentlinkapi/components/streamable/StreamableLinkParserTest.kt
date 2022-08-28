package com.devgary.contentlinkapi.components.streamable

import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

internal class StreamableLinkParserTest {
    @Test
    @DisplayName("Given Streamable Url with Shortcode, When parse() called, Then Shortcode matches")
    fun streamableUrlWithShortcode() {
        val shortCode = "hn8hq"
        val parsedResult = StreamableLinkParser.parse("streamable.com/$shortCode")
        
        parsedResult.shortCode shouldBe shortCode
    }
    
    @Test
    @DisplayName("Given Streamable Url without Shortcode, When parse() called, Then Shortcode null")
    fun streamableUrlWithoutShortcode() {
        val parsedResult = StreamableLinkParser.parse("streamable.com/")

        parsedResult.shortCode shouldBe null
    }

    @Test
    @DisplayName("Given Non Streamable Url with Shortcode, When parse() called, Then Shortcode null")
    fun nonStreamableUrlWithShortcode() {
        val shortCode = "hn8hq"
        val parsedResult = StreamableLinkParser.parse("example.com/$shortCode")

        parsedResult.shortCode shouldBe null
    }
}