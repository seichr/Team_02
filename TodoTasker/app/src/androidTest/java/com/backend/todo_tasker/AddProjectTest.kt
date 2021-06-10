package com.backend.todo_tasker


import android.view.View
import android.view.ViewGroup
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
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
class AddProjectTest {

    @Rule
    @JvmField
    var mActivityTestRule = ActivityTestRule(MainActivity::class.java)

    @Test
    fun projectAddTest() {
        val appCompatImageButton = onView(
            allOf(
                withContentDescription("Navigate up"),
                childAtPosition(
                    allOf(
                        withId(R.id.toolbar),
                        childAtPosition(
                            withId(R.id.tToolbar),
                            0
                        )
                    ),
                    1
                ),
                isDisplayed()
            )
        )
        appCompatImageButton.perform(click())

        val appCompatImageButton2 = onView(
            allOf(
                withId(R.id.button_projects_settings), withContentDescription("Project Settings"),
                childAtPosition(
                    withClassName(`is`("android.widget.RelativeLayout")),
                    2
                ),
                isDisplayed()
            )
        )
        appCompatImageButton2.perform(click())

        val appCompatImageButton3 = onView(
            allOf(
                withId(R.id.button_add_projects), withContentDescription("Back Arrow"),
                childAtPosition(
                    allOf(
                        withId(R.id.layout_purple_zone),
                        childAtPosition(
                            withClassName(`is`("android.widget.RelativeLayout")),
                            0
                        )
                    ),
                    2
                ),
                isDisplayed()
            )
        )
        appCompatImageButton3.perform(click())

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
        appCompatEditText.perform(replaceText("ABC"), closeSoftKeyboard())

        val appCompatImageButton4 = onView(
            allOf(
                withId(R.id.image_btn_color),
                childAtPosition(
                    withClassName(`is`("android.widget.RelativeLayout")),
                    5
                ),
                isDisplayed()
            )
        )
        appCompatImageButton4.perform(click())

        val appCompatImageButton5 = onView(
            allOf(
                withId(R.id.image_btn_color),
                childAtPosition(
                    withClassName(`is`("android.widget.RelativeLayout")),
                    5
                ),
                isDisplayed()
            )
        )
        appCompatImageButton5.perform(click())

        val appCompatButton = onView(
            allOf(
                withId(R.id.color),
                childAtPosition(
                    allOf(
                        withId(R.id.linearLayout),
                        childAtPosition(
                            withId(R.id.color_palette),
                            2
                        )
                    ),
                    0
                ),
                isDisplayed()
            )
        )
        appCompatButton.perform(click())

        val appCompatButton2 = onView(
            allOf(
                withId(R.id.positive), withText("OK"),
                childAtPosition(
                    allOf(
                        withId(R.id.buttons_layout),
                        childAtPosition(
                            withId(R.id.colorpicker_base),
                            2
                        )
                    ),
                    0
                ),
                isDisplayed()
            )
        )
        appCompatButton2.perform(click())

        val materialButton = onView(
            allOf(
                withId(R.id.button_add_to_db), withText("Save"),
                childAtPosition(
                    withClassName(`is`("android.widget.RelativeLayout")),
                    7
                ),
                isDisplayed()
            )
        )
        materialButton.perform(click())

        val appCompatImageButton6 = onView(
            allOf(
                withId(R.id.button_back_to_main), withContentDescription("Back Arrow"),
                childAtPosition(
                    allOf(
                        withId(R.id.layout_purple_zone),
                        childAtPosition(
                            withClassName(`is`("android.widget.RelativeLayout")),
                            0
                        )
                    ),
                    0
                ),
                isDisplayed()
            )
        )
        appCompatImageButton6.perform(click())
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
