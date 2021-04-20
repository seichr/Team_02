package com.example.todo_tasker

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import org.junit.Assert.*
/*
@RunWith(AndroidJUnit4::class)
@LargeTest
class UITestExample {
    private lateinit var stringToBetyped: String

    @get:Rule
    var activityRule: ActivityScenarioRule<MainActivity>
            = ActivityScenarioRule(MainActivity::class.java)

    @Before
    fun initValidString() {
        // Specify a valid string.
        stringToBetyped = "Espresso"
    }

    @Test
    fun changeText_sameActivity() {
        // Type text and then press the button.
        onView(withId(R.id.editTextTextPersonName))
            .perform(typeText(stringToBetyped), closeSoftKeyboard())
        onView(withId(R.id.button)).perform(click())

        // Check that the text was changed.
        stringToBetyped = "NaEspressome"
        onView(withId(R.id.editTextTextPersonName))
            .check(matches(withText(stringToBetyped)))
    }

}*/