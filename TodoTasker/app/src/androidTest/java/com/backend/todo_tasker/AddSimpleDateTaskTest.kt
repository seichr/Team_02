package com.backend.todo_tasker


import android.view.View
import android.view.ViewGroup
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.filters.LargeTest
import androidx.test.rule.ActivityTestRule
import androidx.test.runner.AndroidJUnit4
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.Matchers.`is`
import org.hamcrest.Matchers.allOf
import org.hamcrest.TypeSafeMatcher
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@LargeTest
@RunWith(AndroidJUnit4::class)
class AddSimpleDateTaskTest {

    @Rule
    @JvmField
    var mActivityTestRule = ActivityTestRule(MainActivity::class.java)

    @Test
    fun addSimpleDateTaskTest() {
        val appCompatImageButton = onView(
            allOf(
                withId(R.id.button_add_to_db), withContentDescription("Add Task"),
                childAtPosition(
                    childAtPosition(
                        withId(android.R.id.content),
                        0
                    ),
                    1
                ),
                isDisplayed()
            )
        )
        appCompatImageButton.perform(click())

        val appCompatEditText = onView(
            allOf(
                withId(R.id.edittext_name),
                childAtPosition(
                    withClassName(`is`("android.widget.RelativeLayout")),
                    2
                ),
                isDisplayed()
            )
        )
        appCompatEditText.perform(replaceText("TestCase"), closeSoftKeyboard())

        val appCompatEditText2 = onView(
            allOf(
                withId(R.id.edittext_datetime),
                childAtPosition(
                    withClassName(`is`("android.widget.RelativeLayout")),
                    4
                ),
                isDisplayed()
            )
        )
        appCompatEditText2.perform(click())

        val appCompatEditText3 = onView(
            allOf(
                withId(R.id.edittext_datetime),
                childAtPosition(
                    withClassName(`is`("android.widget.RelativeLayout")),
                    4
                ),
                isDisplayed()
            )
        )
        appCompatEditText3.perform(click())

        val materialButton = onView(
            allOf(
                withId(android.R.id.button1), withText("OK"),
                childAtPosition(
                    childAtPosition(
                        withClassName(`is`("android.widget.ScrollView")),
                        0
                    ),
                    3
                )
            )
        )
        materialButton.perform(scrollTo(), click())

        val materialButton2 = onView(
            allOf(
                withId(android.R.id.button1), withText("OK"),
                childAtPosition(
                    childAtPosition(
                        withClassName(`is`("android.widget.ScrollView")),
                        0
                    ),
                    3
                )
            )
        )
        materialButton2.perform(scrollTo(), click())

        val materialButton3 = onView(
            allOf(
                withId(R.id.button_add_to_db), withText("Save"),
                childAtPosition(
                    withClassName(`is`("android.widget.RelativeLayout")),
                    6
                ),
                isDisplayed()
            )
        )
        materialButton3.perform(click())

        val textView = onView(
            allOf(
                withId(R.id.item_title), withText("TestCase"),
                withParent(withParent(withId(R.id.todo_list))),
                isDisplayed()
            )
        )
        textView.check(matches(withText("TestCase")))
    }

    private fun childAtPosition(
        parentMatcher: Matcher<View>, position: Int
    ): Matcher<View> {

        return object : TypeSafeMatcher<View>() {
            override fun describeTo(description: Description) {
                description.appendText("Child at position $position in parent ")
                parentMatcher.describeTo(description)
            }

            public override fun matchesSafely(view: View): Boolean {
                val parent = view.parent
                return parent is ViewGroup && parentMatcher.matches(parent)
                        && view == parent.getChildAt(position)
            }
        }
    }
}
