package com.backend.todo_tasker_long


import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import android.widget.TimePicker
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.contrib.PickerActions
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.filters.LargeTest
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.ActivityTestRule
import androidx.test.runner.AndroidJUnit4
import androidx.test.uiautomator.By
import androidx.test.uiautomator.UiDevice
import androidx.test.uiautomator.UiObject2
import androidx.test.uiautomator.Until
import com.backend.todo_tasker.MainActivity
import com.backend.todo_tasker.R
import com.backend.todo_tasker.database.DatabaseClass
import com.backend.todo_tasker.todoDb
import junit.framework.Assert.assertEquals
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.Matchers
import org.hamcrest.Matchers.`is`
import org.hamcrest.Matchers.allOf
import org.hamcrest.TypeSafeMatcher
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.util.*


@LargeTest
@RunWith(AndroidJUnit4::class)
class ReminderNotificationShowUpRecordedTest {

    @Rule
    @JvmField
    var mActivityTestRule = ActivityTestRule(MainActivity::class.java)

    @Test
    fun reminderNotificationShowUp() {
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        val notificationTitle =  appContext.getString(R.string.NOTIFICATION_TITLE)
        val notificationText =  appContext.getString(R.string.NOTIFICATION_TEXT)
        val timeout:Long =  90 * 1000

        val db = DatabaseClass(appContext)
        db.deleteDBEntries(todoDb) //TODO this clears the production Database, could be changed to mockdb at some point

        val calendar = Calendar.getInstance()


        val appCompatImageButton = onView(
                allOf(withId(R.id.button_add_to_db), withContentDescription("Add Task"),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                1),
                        isDisplayed()))
        appCompatImageButton.perform(click())

        val appCompatEditText = onView(
                allOf(withId(R.id.edittext_name),
                        childAtPosition(
                                withClassName(`is`("android.widget.RelativeLayout")),
                                2),
                        isDisplayed()))
        appCompatEditText.perform(replaceText("foo"), closeSoftKeyboard())

        val appCompatEditText2 = onView(
                allOf(withId(R.id.edittext_datetime),
                        childAtPosition(
                                withClassName(`is`("android.widget.RelativeLayout")),
                                4),
                        isDisplayed()))
        appCompatEditText2.perform(click())
        appCompatEditText2.perform(click())


        onView(
                withClassName(
                        Matchers.equalTo(
                                DatePicker::class.java.name
                        )
                )
        ).perform(PickerActions.setDate(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.DAY_OF_MONTH)))

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
                .perform(PickerActions.setTime(calendar.get(Calendar.HOUR), calendar.get(Calendar.MINUTE) + 1))


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
                allOf(withId(R.id.button_add_to_db), withText("Save"),
                        childAtPosition(
                                withClassName(`is`("android.widget.RelativeLayout")),
                                6),
                        isDisplayed()))
        materialButton3.perform(click())

        val device: UiDevice = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())
        device.openNotification()
        device.wait(Until.hasObject(By.text(notificationTitle)), timeout);
        val title: UiObject2 = device.findObject(By.text(notificationTitle))
        val text: UiObject2 = device.findObject(By.text(notificationText))
        assertEquals(notificationTitle, title.text)
        assertEquals(notificationText, text.text)
    }

    private fun childAtPosition(
            parentMatcher: Matcher<View>, position: Int): Matcher<View> {

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
