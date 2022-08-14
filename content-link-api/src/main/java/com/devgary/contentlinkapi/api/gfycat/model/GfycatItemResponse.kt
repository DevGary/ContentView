package com.devgary.contentlinkapi.api.gfycat.model

import com.squareup.moshi.Json

data class GfycatItemResponse(
    @Json(name = "gfyItem") var gfycatItem: GfycatItem,
)