package com.example.todo_tasker

import android.widget.ListView
import androidx.test.espresso.Espresso.onData
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.doesNotExist
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import androidx.test.platform.app.InstrumentationRegistry
import org.hamcrest.Matchers.anything
import org.junit.Assert.assertNotEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@LargeTest
class TaskListActivityTest {
    val appContext = InstrumentationRegistry.getInstrumentation().targetContext
    val db = database_class(appContext)
    val datab = db.createDb()

    @get:Rule
    var activityRule: ActivityScenarioRule<MainActivity>
            = ActivityScenarioRule(MainActivity::class.java)

    @Before
    fun fill_db() {
        db.deleteDBEntries(datab)
        val time_in_millis = db.date_to_millis(db.get_current_date())
        for(i in 0..3) {
            val test_todo = Todo(i, "Test", time_in_millis, null)
            val ret_val = db.addToDb(datab, test_todo)
            assertNotEquals(ret_val, -1)
        }
    }

    @Test
    fun check_tasks_are_there() {
        // Switch to task list
        onView(withId(R.id.button_switch_to_list)).perform(click())

        onView(withId(R.id.task_list)).check(matches(isDisplayed()))


        //onData(anything()).inAdapterView(withId(R.id.task_list)).check()
        //var listView: ListView
        //val listView = TaskListActivity.findViewByID(R.id.task_list)
       // val listView = ViewMatchers.withId(R.id.task_list)
        //assert(listView.getAdapter().getCount() == 4)
    }

}