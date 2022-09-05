package com.devgary.testandroidcore.util

import android.app.Activity
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.runner.lifecycle.ActivityLifecycleMonitorRegistry
import androidx.test.runner.lifecycle.Stage

object AndroidTestUtils {

    /**
     * Get Current Activity for Instrumentation Tests
     */
    fun getCurrentActivity(): Activity? {
        val currentActivity = arrayOfNulls<Activity>(1)
        InstrumentationRegistry.getInstrumentation().runOnMainSync {
            val allActivities = ActivityLifecycleMonitorRegistry.getInstance()
                .getActivitiesInStage(Stage.RESUMED)
            if (!allActivities.isEmpty()) {
                currentActivity[0] = allActivities.iterator().next()
            }
        }
        return currentActivity[0]
    }
}