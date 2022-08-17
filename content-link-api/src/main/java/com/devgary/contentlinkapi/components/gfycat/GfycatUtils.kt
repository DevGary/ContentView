package com.devgary.contentlinkapi.components.gfycat

object GfycatUtils {
    private const val GFYCAT_DOMAIN = "gfycat.com"

    fun parseGfycatNameFromUrl(url: String): String? {
        val split = url.split("$GFYCAT_DOMAIN/", ignoreCase = true, limit = 2)
        var gfycatName: String? = null
        try {
            if (split.size > 1) {
                val stringAfterDomain = split[1]
                gfycatName = stringAfterDomain.split(Regex("[^a-zA-Z]"))[0]
            }
        } catch (_: Exception) {
        }

        return gfycatName
    }
}