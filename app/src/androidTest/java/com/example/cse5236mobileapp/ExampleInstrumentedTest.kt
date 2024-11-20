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
import com.example.cse5236mobileapp.ui.activity.HomeScreenActivity
import com.example.cse5236mobileapp.ui.activity.LoginActivity

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
    fun login_test() {

        val activityScenario = ActivityScenario.launch(LoginActivity::class.java)

        login_with_credentials("instrument@gmail.com", "instrument")

        onView(withId(R.id.txtHomeScreenWelcome)).check(matches(isDisplayed()))
    }

    @Test
    fun create_and_delete_user() {
        // Creating a dummy account
        val email = "${random_name()}@gmail.com"
        val password = random_name()
        val username = random_name()

        // Pulling up create account screen and creating account
        val activityScenario = ActivityScenario.launch(LoginActivity::class.java)
        onView(withId(R.id.btnCreateNew)).perform(click())
        onView(withId(R.id.txtEnterUsername)).perform(typeText(username), closeSoftKeyboard())
        onView(withId(R.id.ditCreateUsername)).perform(typeText(email), closeSoftKeyboard())
        onView(withId(R.id.ditCreatePassword)).perform(typeText(password), closeSoftKeyboard())
        onView(withId(R.id.ditReenterPassword)).perform(typeText(password), closeSoftKeyboard())
        onView(withId(R.id.btnCreateAccount)).perform(click())

        Thread.sleep(100000)
    }


    @Test
    fun change_display_name() {
        val activityScenario = ActivityScenario.launch(HomeScreenActivity::class.java)

        Thread.sleep(1000)
    }


    private fun random_name(): String {
        val possibleValues = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890"
        var dummyUsername = ""

        for (i in 0 until 6) {
            dummyUsername += possibleValues.random()
        }

        return dummyUsername
    }


    private fun login_with_credentials(email: String, password: String) {
        onView(withId(R.id.ditUsername)).perform(typeText(email), closeSoftKeyboard())
        onView(withId(R.id.ditUserPassword)).perform(typeText(password), closeSoftKeyboard())

        onView(withId(R.id.btnLogIn)).perform(click())

        Thread.sleep(500)
    }
}