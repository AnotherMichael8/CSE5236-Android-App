package com.example.cse5236mobileapp

import androidx.annotation.UiThread
import androidx.test.annotation.UiThreadTest
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingResource
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.closeSoftKeyboard
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import com.example.cse5236mobileapp.ui.activity.MainActivity
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4ClassRunner::class)
class MainActivityTesting {
    @Test
    fun activityViewed() {
        // Context of the app under test.
        val activityScenario = ActivityScenario.launch(MainActivity::class.java)

        onView(withId(R.id.main)).check(matches(isDisplayed()))
    }

    @Test
    fun login_with_credentials() {
        val activityScenario = ActivityScenario.launch(MainActivity::class.java)

        onView(withId(R.id.btnContinue)).perform(click())

        onView(withId(R.id.ditUsername)).perform(typeText("jyablok@gmail.com"), closeSoftKeyboard())
        onView(withId(R.id.ditUserPassword)).perform(typeText("jyablok"), closeSoftKeyboard())

        onView(withId(R.id.btnLogIn)).perform(click())

        Thread.sleep(1000)

        onView(withId(R.id.txtHomeScreenWelcome)).check((matches(withText("Welcome: jyablok"))))
    }
}