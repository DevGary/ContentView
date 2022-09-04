package com.devgary.contentlinkapi.components.streamable

import com.devgary.contentlinkapi.handlers.streamable.StreamableLinkParser
import com.devgary.testcore.SampleContent.STREAMABLE.BASIC_URL
import com.devgary.testcore.SampleContent.STREAMABLE.HLS_URL
import com.devgary.testcore.SampleContent.STREAMABLE.URL_S_SHORTCODE
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource

internal class StreamableLinkParserTest {
    companion object {
        @JvmStatic
        private fun streamableUrlWithShortcode(): List<Arguments> {
            return listOf(
                Arguments.of(BASIC_URL, "hwa6l"),
                Arguments.of(URL_S_SHORTCODE, "hwa6l"),
                Arguments.of(HLS_URL, null),
            )
        }
    }

    @ParameterizedTest
    @MethodSource("streamableUrlWithShortcode")
    @DisplayName("Given Streamable Url with Shortcode, When parse() called, Then Shortcode matches")
    fun streamableUrlWithShortcode(url: String, expectedShortcode: String?) {
        val parsedResult = StreamableLinkParser.parse(url)
        parsedResult.shortCode shouldBe expectedShortcode
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