package com.example.todo_tasker

import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*
import kotlin.concurrent.thread

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class DataBaseSelectInstrumentedTest {
    // Test the Get Function by adding a single entrance and checking for it
    @Test
    fun SingleSelectPass() {
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext

        val db = database_class(appContext)
        val datab = db.createDb()
        db.deleteDBEntries(datab)

        val time_in_millis = db.date_to_millis(db.get_current_date())
        val test_todo = Todo(6, "Test", time_in_millis, null)

        // Add a single entry into the Database
        val ret_val = db.addToDb(datab, test_todo)
        assertNotEquals(ret_val, -1)

        // Check that only a single entry is inside of the Database
        val todos = db.getAllDb(datab)
        assertEquals(todos.size, 1)

        // Compare the entry with our handmade one
        assert(todos.contains(test_todo))
    }

    // Test the Get Function by adding lots of entries and checking if we get them back
    @Test
    fun MultiSelectPass() {
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext

        val db = database_class(appContext)
        val datab = db.createDb()
        db.deleteDBEntries(datab)

        // Add 51 differing entries to the Database
        val time_in_millis = db.date_to_millis(db.get_current_date())
        for(i in 0..50) {
            val test_todo = Todo(i, "Test", time_in_millis, null)
            val ret_val = db.addToDb(datab, test_todo)
            assertNotEquals(ret_val, -1)
        }

        // Check if there are actually 51 entries
        val todos = db.getAllDb(datab)
        assertEquals(todos.size, 51)

        // Check if all of the entries are there
        for(i in 0..50) {
            assert(todos[i] == Todo(i, "Test", time_in_millis, null))
        }
    }

    // We cannot think of any Test Cases that could potentially fail with some degree of common sense
}