package com.backend.todo_tasker


import android.app.Application
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.filters.LargeTest
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.ActivityTestRule
import androidx.test.runner.AndroidJUnit4
import com.backend.todo_tasker.database.DatabaseClass
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
class EndToEndTest {

    @Rule
    @JvmField
    var mActivityTestRule = ActivityTestRule(MainActivity::class.java)

    @Test
    fun endToEndTest() {
        val appContext = ApplicationProvider.getApplicationContext<Application>()

        val db = DatabaseClass(appContext)
        val datab = db.createDb()
        db.deleteDBEntries(datab)

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
        Thread.sleep(1000)

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
        appCompatEditText.perform(replaceText("TC1"), closeSoftKeyboard())
        Thread.sleep(1000)

        val materialButton = onView(
            allOf(
                withId(R.id.button_add_to_db), withText("Save"),
                childAtPosition(
                    withClassName(`is`("android.widget.RelativeLayout")),
                    6
                ),
                isDisplayed()
            )
        )
        materialButton.perform(click())
        Thread.sleep(1000)

        val appCompatImageButton2 = onView(
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
        appCompatImageButton2.perform(click())
        Thread.sleep(1000)

        val appCompatEditText2 = onView(
            allOf(
                withId(R.id.edittext_name),
                childAtPosition(
                    withClassName(`is`("android.widget.RelativeLayout")),
                    2
                ),
                isDisplayed()
            )
        )
        appCompatEditText2.perform(replaceText("TC2"), closeSoftKeyboard())
        Thread.sleep(1000)

        val materialButton2 = onView(
            allOf(
                withId(R.id.button_add_to_db), withText("Save"),
                childAtPosition(
                    withClassName(`is`("android.widget.RelativeLayout")),
                    6
                ),
                isDisplayed()
            )
        )
        materialButton2.perform(click())
        Thread.sleep(1000)

        val appCompatImageButton3 = onView(
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
        appCompatImageButton3.perform(click())
        Thread.sleep(1000)

        val appCompatEditText3 = onView(
            allOf(
                withId(R.id.edittext_name),
                childAtPosition(
                    withClassName(`is`("android.widget.RelativeLayout")),
                    2
                ),
                isDisplayed()
            )
        )
        appCompatEditText3.perform(replaceText("TC3"), closeSoftKeyboard())
        Thread.sleep(1000)

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
        Thread.sleep(1000)

        val appCompatImageButton4 = onView(
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
        appCompatImageButton4.perform(click())
        Thread.sleep(1000)

        val appCompatEditText4 = onView(
            allOf(
                withId(R.id.edittext_name),
                childAtPosition(
                    withClassName(`is`("android.widget.RelativeLayout")),
                    2
                ),
                isDisplayed()
            )
        )
        appCompatEditText4.perform(replaceText("TC4"), closeSoftKeyboard())
        Thread.sleep(1000)

        val materialButton4 = onView(
            allOf(
                withId(R.id.button_add_to_db), withText("Save"),
                childAtPosition(
                    withClassName(`is`("android.widget.RelativeLayout")),
                    6
                ),
                isDisplayed()
            )
        )
        materialButton4.perform(click())
        Thread.sleep(1000)

        val appCompatImageButton5 = onView(
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
        appCompatImageButton5.perform(click())
        Thread.sleep(1000)

        val appCompatEditText5 = onView(
            allOf(
                withId(R.id.edittext_name),
                childAtPosition(
                    withClassName(`is`("android.widget.RelativeLayout")),
                    2
                ),
                isDisplayed()
            )
        )
        appCompatEditText5.perform(replaceText("TC5"), closeSoftKeyboard())
        Thread.sleep(1000)

        val materialButton5 = onView(
            allOf(
                withId(R.id.button_add_to_db), withText("Save"),
                childAtPosition(
                    withClassName(`is`("android.widget.RelativeLayout")),
                    6
                ),
                isDisplayed()
            )
        )
        materialButton5.perform(click())
        Thread.sleep(1000)

        val recyclerView = onView(
            allOf(
                withId(R.id.todo_list),
                childAtPosition(
                    withClassName(`is`("android.widget.RelativeLayout")),
                    0
                )
            )
        )
        recyclerView.perform(actionOnItemAtPosition<ViewHolder>(1, click()))
        Thread.sleep(1000)

        val appCompatEditText6 = onView(
            allOf(
                withId(R.id.edittext_modify_name), withText("TC2"),
                childAtPosition(
                    withClassName(`is`("android.widget.RelativeLayout")),
                    3
                ),
                isDisplayed()
            )
        )
        appCompatEditText6.perform(replaceText("TC7"))
        Thread.sleep(1000)

        val appCompatEditText7 = onView(
            allOf(
                withId(R.id.edittext_modify_name), withText("TC7"),
                childAtPosition(
                    withClassName(`is`("android.widget.RelativeLayout")),
                    3
                ),
                isDisplayed()
            )
        )
        appCompatEditText7.perform(closeSoftKeyboard())
        Thread.sleep(1000)

        val materialButton6 = onView(
            allOf(
                withId(R.id.button_modify_to_db), withText("Save"),
                childAtPosition(
                    withClassName(`is`("android.widget.RelativeLayout")),
                    7
                ),
                isDisplayed()
            )
        )
        materialButton6.perform(click())
        Thread.sleep(1000)

        val textView = onView(
            allOf(
                withId(R.id.item_title), withText("TC7"),
                withParent(withParent(withId(R.id.todo_list))),
                isDisplayed()
            )
        )
        textView.check(matches(withText("TC7")))
        Thread.sleep(1000)

        val recyclerView2 = onView(
            allOf(
                withId(R.id.todo_list),
                childAtPosition(
                    withClassName(`is`("android.widget.RelativeLayout")),
                    0
                )
            )
        )
        recyclerView2.perform(actionOnItemAtPosition<ViewHolder>(1, click()))
        Thread.sleep(1000)

        val appCompatImageButton6 = onView(
            allOf(
                withId(R.id.button_more_options),
                childAtPosition(
                    withClassName(`is`("android.widget.RelativeLayout")),
                    1
                ),
                isDisplayed()
            )
        )
        appCompatImageButton6.perform(click())
        Thread.sleep(1000)

        val materialButton7 = onView(
            allOf(
                withId(R.id.button_duplicate_task), withText("Duplicate Task"),
                childAtPosition(
                    withClassName(`is`("android.widget.RelativeLayout")),
                    0
                ),
                isDisplayed()
            )
        )
        materialButton7.perform(click())
        Thread.sleep(1000)

        val recyclerView3 = onView(
            allOf(
                withId(R.id.todo_list),
                childAtPosition(
                    withClassName(`is`("android.widget.RelativeLayout")),
                    0
                )
            )
        )
        recyclerView3.perform(actionOnItemAtPosition<ViewHolder>(1, click()))
        Thread.sleep(1000)

        val appCompatImageButton7 = onView(
            allOf(
                withId(R.id.button_more_options),
                childAtPosition(
                    withClassName(`is`("android.widget.RelativeLayout")),
                    1
                ),
                isDisplayed()
            )
        )
        appCompatImageButton7.perform(click())
        Thread.sleep(1000)

        val materialButton8 = onView(
            allOf(
                withId(R.id.button_delete_task), withText("Delete Task"),
                childAtPosition(
                    withClassName(`is`("android.widget.RelativeLayout")),
                    1
                ),
                isDisplayed()
            )
        )
        materialButton8.perform(click())
        Thread.sleep(1000)

        val recyclerView4 = onView(
            allOf(
                withId(R.id.todo_list),
                childAtPosition(
                    withClassName(`is`("android.widget.RelativeLayout")),
                    0
                )
            )
        )
        recyclerView4.perform(actionOnItemAtPosition<ViewHolder>(4, click()))
        Thread.sleep(1000)

        val appCompatImageButton8 = onView(
            allOf(
                withId(R.id.button_more_options),
                childAtPosition(
                    withClassName(`is`("android.widget.RelativeLayout")),
                    1
                ),
                isDisplayed()
            )
        )
        appCompatImageButton8.perform(click())
        Thread.sleep(1000)

        val materialButton9 = onView(
            allOf(
                withId(R.id.button_delete_task), withText("Delete Task"),
                childAtPosition(
                    withClassName(`is`("android.widget.RelativeLayout")),
                    1
                ),
                isDisplayed()
            )
        )
        materialButton9.perform(click())
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
