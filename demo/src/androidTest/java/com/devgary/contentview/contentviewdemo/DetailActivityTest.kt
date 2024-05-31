package com.devgary.contentview.contentviewdemo

import android.view.View
import androidx.test.core.app.ActivityScenario
import androidx.test.filters.LargeTest
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import com.adevinta.android.barista.interaction.BaristaMenuClickInteractions.clickMenu
import com.devgary.contentview.image.ImageContentView
import com.devgary.contentview.video.ExoVideoView
import com.devgary.contentviewdemo.screens.detail.DetailActivity
import com.devgary.testandroidcore.util.AndroidTestUtils.getCurrentActivity
import com.devgary.testandroidcore.util.WaitUtils
import com.devgary.testandroidcore.util.WaitUtils.withDelay
import com.devgary.testandroidcore.util.*
import org.junit.Test
import org.junit.runner.RunWith
import com.devgary.contentviewdemo.R
import org.junit.Before

@RunWith(AndroidJUnit4ClassRunner::class)
class DetailActivityTest {
    private fun isContentViewVisible() = (getCurrentActivity()?.findViewById<View>(R.id.contentview)?.height ?: 0) > 1
    private lateinit var activityScenario: ActivityScenario<DetailActivity>

    @Before
    fun setup() {
        activityScenario = ActivityScenario.launch(DetailActivity::class.java)
    }
    
    @Test
    @LargeTest
    fun When_Load_Image_Menu_Item_Clicked_Then_ImageContentView_displayed() {
        withDelay(1000) { clickMenu(R.id.menu_image) }

        WaitUtils.waitFor(condition = ::isContentViewVisible)

        assertDisplayedClass(ImageContentView::class.java)
        assertNotDisplayedOrNotExistClass(ExoVideoView::class.java)
    }

    @Test
    @LargeTest
    fun When_Load_Image_Menu_Item_Clicked_Then_VideoContentView_displayed() {
        withDelay(1000) { clickMenu(R.id.menu_video) }

        WaitUtils.waitFor(condition = ::isContentViewVisible)

        assertDisplayedClass(ExoVideoView::class.java)
        assertNotDisplayedOrNotExistClass(ImageContentView::class.java)
    }
}