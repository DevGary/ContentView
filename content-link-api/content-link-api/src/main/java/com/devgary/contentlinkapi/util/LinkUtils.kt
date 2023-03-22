package com.devgary.contentlinkapi.util

import android.net.Uri
import com.devgary.contentcore.util.isNotNullOrBlank

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
    
    fun parseAlphabeticalIdFromUrl(url: String, startFromOccurrence: String, minLength: Int? = null): String? {
        val parsed = parseSegmentFromUrl(
            url = url, 
            startFromOccurrence = startFromOccurrence, 
            regexPattern = REGEX_PATTERN_NON_ALPHABETICAL
        )

        parsed?.let {
            if (it.isNotNullOrBlank() && it.length >= (minLength ?: -1)) {
                return parsed
            }
        }

        return null
    }    
    
    fun parseAlphanumericIdFromUrl(url: String, startFromOccurrence: String, minLength: Int? = null): String? {
        val parsed = parseSegmentFromUrl(
            url = url, 
            startFromOccurrence = startFromOccurrence, 
            regexPattern = REGEX_PATTERN_NON_ALPHANUMERIC
        )

        parsed?.let { 
            if (it.isNotNullOrBlank() && it.length >= (minLength ?: -1)) {
                return parsed
            }
        }
        
        return null
    }
    
    fun String?.parseDomainFromUrl(): String? {
        return if (this.isNotNullOrBlank()) {
            Uri.parse(this).host
        } else {
            null
        }
    }
    
    fun String.prependHttpsIfMissing(): String {
        var finalUrl = this
        
        if (startsWith("//")) {
            finalUrl = "https:" + finalUrl
        }
        
        return finalUrl
    }
}