package com.backend.todo_tasker


import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import android.widget.TimePicker
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.scrollTo
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.PickerActions
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.filters.LargeTest
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.ActivityTestRule
import androidx.test.runner.AndroidJUnit4
import com.backend.todo_tasker.database.DatabaseClass
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.Matchers
import org.hamcrest.Matchers.`is`
import org.hamcrest.Matchers.allOf
import org.hamcrest.TypeSafeMatcher
import org.hamcrest.core.StringStartsWith.startsWith
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@LargeTest
@RunWith(AndroidJUnit4::class)
class DateTimePickRecordedTest {

    @Rule
    @JvmField
    var mActivityTestRule = ActivityTestRule(MainActivity::class.java)

    @Test
    fun dateTimePickRecordedTest() {
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        val db = DatabaseClass(appContext)
        db.deleteDBEntries(todoDb) //TODO this clears the production Database, could be changed to mockdb at some point

        val appCompatEditText = onView(
            allOf(
                withId(R.id.edittext_datetime),
                childAtPosition(
                    childAtPosition(
                        withId(android.R.id.content),
                        0
                    ),
                    4
                ),
                isDisplayed()
            )
        )
        appCompatEditText.perform(click())

        onView(
            withClassName(
                Matchers.equalTo(
                    DatePicker::class.java.name
                )
            )
        ).perform(PickerActions.setDate(2021, 5, 2))

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

        onView(withClassName(Matchers.equalTo(TimePicker::class.java.name)))
            .perform(PickerActions.setTime(18, 0))


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
                withId(R.id.button_add_to_db), withText("Add Task"),
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
        materialButton3.perform(click())

        val materialButton4 = onView(
            allOf(
                withId(R.id.button_switch_to_list), withText("Show Task List"),
                childAtPosition(
                    childAtPosition(
                        withId(android.R.id.content),
                        0
                    ),
                    3
                ),
                isDisplayed()
            )
        )
        materialButton4.perform(click())

        val textView = onView(
            allOf(
                withId(R.id.item_date), withText(startsWith("1619971")),
                withParent(withParent(withId(R.id.todo_list))),
                isDisplayed()
            )
        )
        textView.check(matches(isDisplayed()))
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
