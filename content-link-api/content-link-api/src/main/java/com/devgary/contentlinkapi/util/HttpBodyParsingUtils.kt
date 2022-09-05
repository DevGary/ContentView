package com.devgary.contentlinkapi.util

/**
 * Util functions for parsing Http response body
 * 
 * An alternative is to use something like Jsoup but for simple extraction, string manipulation
 * is faster as Jsoup has to parse the entire body first before being able to query it
 */
object HttpBodyParsingUtils {

    /**
     * Tries to parse src attribute from first &lt;video&gt; element in response body string
     */
    fun String.parseFirstVideoElementSrc(): String? {
        try {
            return this
                .substringAfter("<video")
                .substringAfter("src=\"")
                .substringBefore("\"")
        }
        catch (_: Exception) {}
        
        return null
    }
}