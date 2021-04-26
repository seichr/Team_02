package com.backend.todo_tasker

import android.content.Context
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import androidx.test.platform.app.InstrumentationRegistry
import com.backend.todo_tasker.database.DatabaseClass
import com.backend.todo_tasker.database.Todo
import org.junit.Assert.assertNotEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@LargeTest
class TodoListActivityTest {
    private val appContext: Context = InstrumentationRegistry.getInstrumentation().targetContext
    private val db = DatabaseClass(appContext)
    private val datab = db.createDb()

    @get:Rule
    var activityRule: ActivityScenarioRule<MainActivity>
            = ActivityScenarioRule(MainActivity::class.java)

    @Before
    fun fill_db() {
        db.deleteDBEntries(datab)
        val timeInMillis = db.dateToMillis(db.getCurrentDate())
        for(i in 0..3) {
            val testTodo = Todo(i, "Test", timeInMillis, null)
            val retVal = db.addToDb(datab, testTodo)
            assertNotEquals(retVal, -1)
        }
    }

    @Test
    fun check_tasks_are_there() {
        // Switch to task list
        onView(withId(R.id.button_switch_to_list)).perform(click())

        onView(withId(R.id.todo_list)).check(matches(isDisplayed()))
        // TODO: Should be finished
        //onData(anything()).inAdapterView(withId(R.id.task_list)).check()
        //var listView: ListView
        //val listView = TaskListActivity.findViewByID(R.id.task_list)
       // val listView = ViewMatchers.withId(R.id.task_list)
        //assert(listView.getAdapter().getCount() == 4)
    }

}