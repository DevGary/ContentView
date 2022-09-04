package com.devgary.contentlinkapi.components.gfycat.api.model

import com.squareup.moshi.Json

data class GfycatItemResponse(
    @Json(name = "gfyItem") var gfycatItem: GfycatItem,
)