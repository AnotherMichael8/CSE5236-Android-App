package com.example.cse5236mobileapp

import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.closeSoftKeyboard
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import com.example.cse5236mobileapp.ui.activity.MainActivity
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import com.example.cse5236mobileapp.ui.activity.HomeScreenActivity
import com.example.cse5236mobileapp.ui.activity.LoginActivity
import org.hamcrest.Matchers.containsString
import org.junit.After

import org.junit.Test
import org.junit.runner.RunWith

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4ClassRunner::class)
class InstrumentUiTest {
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

        logging_out()
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

        // Creating account
        create_account(username, email, password)
        Thread.sleep(1000)
        // Should be at home screen now

        // Navigate settings menu
        onView(withId(R.id.settingsButton)).perform(click())
        Thread.sleep(500)
        onView(withId(R.id.btnDeleteAccount)).perform(click())
        Thread.sleep(500)

        // Should see create account screen again
        onView(withId(R.id.btnCreateAccount)).check(matches(isDisplayed()))

    }


    @Test
    fun change_display_name() {
        val activityScenario = ActivityScenario.launch(LoginActivity::class.java)

        val newName = random_name()

        // Logging in to testing account
        login_with_credentials("instrument@gmail.com", "instrument")

        // Navigate settings menu
        onView(withId(R.id.settingsButton)).perform(click())
        Thread.sleep(50)

        // Fill in new username and submit
        onView(withId(R.id.settingsEditUsername)).perform(typeText(newName))
        onView(withId(R.id.settingsSubmitButton)).perform(click())
        Thread.sleep(500)

        // Verify new welcome text (livedata)
        onView(withId(R.id.txtHomeScreenWelcome)).check(matches(withText("Welcome: $newName")))

        logging_out()
    }

    private fun logging_out() {
        onView(withId(R.id.LogoutButton)).perform(click())
    }


    private fun random_name(): String {
        val possibleValues = "abcdefghijklmnopqrstuvwxyz1234567890"
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

        Thread.sleep(1000)
    }

    private fun create_account(username: String, email: String, password: String) {
        onView(withId(R.id.txtEnterUsername)).perform(typeText(username), closeSoftKeyboard())
        onView(withId(R.id.ditCreateUsername)).perform(typeText(email), closeSoftKeyboard())
        onView(withId(R.id.ditCreatePassword)).perform(typeText(password), closeSoftKeyboard())
        onView(withId(R.id.ditReenterPassword)).perform(typeText(password), closeSoftKeyboard())
        onView(withId(R.id.btnCreateAccount)).perform(click())
    }
}