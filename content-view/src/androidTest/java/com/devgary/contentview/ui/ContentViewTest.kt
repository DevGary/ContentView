package com.devgary.contentview.ui

import androidx.test.core.app.ActivityScenario
import androidx.test.filters.LargeTest
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import com.devgary.contentcore.model.content.Content
import com.devgary.contentcore.model.content.components.ContentSource
import com.devgary.contentcore.model.content.components.ContentType
import com.devgary.contentview.R
import com.devgary.contentview.image.ImageContentView
import com.devgary.testandroidcore.TestActivity
import com.devgary.contentview.video.ExoVideoView
import com.devgary.testandroidcore.util.AndroidTestUtils.getCurrentActivity
import com.devgary.testandroidcore.util.WaitUtils
import com.devgary.testandroidcore.util.assertDisplayedClass
import com.devgary.testandroidcore.util.assertNotDisplayedOrNotExistClass
import com.devgary.testcore.SampleContent
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@Suppress("TestFunctionName")
@RunWith(AndroidJUnit4ClassRunner::class)
class ContentViewTest {
    private fun isContentViewVisible() = (getContentView()?.height ?: 0) > 1
    private fun getContentView() = getCurrentActivity()?.findViewById<ContentView>(R.id.contentview)

    private lateinit var activityScenario: ActivityScenario<TestActivity>

    @Before
    fun setup() {
        TestActivity.layoutId = R.layout.test_activity_content_view
        activityScenario = ActivityScenario.launch(TestActivity::class.java)
    }

    @Test
    @LargeTest
    fun When_ContentView_Show_Image_Then_ImageContentView_Displayed() {
        val currentActivity = getCurrentActivity()
        currentActivity?.runOnUiThread {
            val contentView = currentActivity.findViewById<ContentView>(R.id.contentview)
            val content = Content(ContentSource.Url(SampleContent.IMAGE_CONTENT), ContentType.IMAGE)
            contentView.showContent(content)
        }

        WaitUtils.waitForLongTimeout(condition = ::isContentViewVisible)

        assertDisplayedClass(ImageContentView::class.java)
        assertNotDisplayedOrNotExistClass(ExoVideoView::class.java)
    }
    
    @Test
    @LargeTest
    fun When_ContentView_Show_Video_Then_VideoContentView_Displayed() {
        val currentActivity = getCurrentActivity()
        currentActivity?.runOnUiThread {
            val contentView = currentActivity.findViewById<ContentView>(R.id.contentview)
            val content = Content(ContentSource.Url(SampleContent.MP4_VIDEO_CONTENT), ContentType.VIDEO)
            contentView.showContent(content)
        }

        WaitUtils.waitForLongTimeout(condition = ::isContentViewVisible)

        assertDisplayedClass(ExoVideoView::class.java)
        assertNotDisplayedOrNotExistClass(ImageContentView::class.java)
    }
}