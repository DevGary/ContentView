package com.devgary.contentlinkapi.util

object LinkUtils {
    
    private const val REGEX_PATTERN_NON_ALPHABETICAL = "[^a-zA-Z]"
    private const val REGEX_PATTERN_NON_ALPHANUMERIC = "[^a-zA-Z0-9]"
    
    private fun parseSegmentFromUrl(
        url: String,
        startFromOccurrence: String,
        regexPattern: String,
        ignoreCase: Boolean = true,
    ): String? {
        var segment: String? = null

        val split = url.split(startFromOccurrence, ignoreCase = ignoreCase, limit = 2)
        try {
            if (split.size > 1) {
                val stringAfterDomain = split[1]
                segment = stringAfterDomain.split(Regex(regexPattern))[0]
            }
        } catch (_: Exception) {
        }

        return segment
    }
    
    fun parseAlphabeticalIdFromUrl(url: String, startFromOccurrence: String): String? {
        return parseSegmentFromUrl(
            url = url, 
            startFromOccurrence = startFromOccurrence, 
            regexPattern = REGEX_PATTERN_NON_ALPHABETICAL
        )
    }    
    
    fun parseAlphanumericIdFromUrl(url: String, startFromOccurrence: String): String? {
        return parseSegmentFromUrl(
            url = url, 
            startFromOccurrence = startFromOccurrence, 
            regexPattern = REGEX_PATTERN_NON_ALPHANUMERIC
        )
    }
}