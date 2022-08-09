package com.devgary.contentviewdemo

import com.devgary.contentview.content.Content
import com.devgary.contentview.content.ContentType

class SampleContent {
    companion object {
        val IMAGE_CONTENT = Content("https://images.unsplash.com/photo-1580860749755-f49eb5509d55?ixlib", ContentType.IMAGE)
        val GIF_CONTENT = Content("https://c.tenor.com/VVOA7SCKgmkAAAAM/test.gif", ContentType.GIF)
        val MP4_VIDEO_CONTENT = Content("https://test-videos.co.uk/vids/bigbuckbunny/mp4/h264/720/Big_Buck_Bunny_720_10s_30MB.mp4", ContentType.VIDEO)
    }
}