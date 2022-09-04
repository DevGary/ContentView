package com.devgary.testandroidcore.util

import androidx.test.espresso.matcher.ViewMatchers
import com.adevinta.android.barista.assertion.BaristaVisibilityAssertions
import org.hamcrest.CoreMatchers

fun assertDisplayedClass(clazz: Class<*>) {
    BaristaVisibilityAssertions.assertDisplayed(ViewMatchers.withClassName(CoreMatchers.equalTo(clazz.name)))
}

fun assertNotDisplayedClass(clazz: Class<*>) {
    BaristaVisibilityAssertions.assertNotDisplayed(ViewMatchers.withClassName(CoreMatchers.equalTo(clazz.name)))
}

fun assertNotDisplayedOrNotExistClass(clazz: Class<*>) {
    try {
        BaristaVisibilityAssertions.assertNotDisplayed(ViewMatchers.withClassName(CoreMatchers.equalTo(clazz.name)))
    } catch (e: Exception) {
        // TODO: Clean up this hacky implementation
        if (e.message?.contains("No view matching") == true) {
            // Swallow no view matching exception due to view not existing
        }
        else throw e
    }
}