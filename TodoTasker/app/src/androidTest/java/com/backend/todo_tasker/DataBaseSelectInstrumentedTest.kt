package com.backend.todo_tasker

import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.backend.todo_tasker.database.DatabaseClass
import com.backend.todo_tasker.database.Todo

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class DataBaseSelectInstrumentedTest {
    // Test the Get Function by adding a single entrance and checking for it
    @Test
    fun singleSelectPass() {
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext

        val db = DatabaseClass(appContext)
        val datab = db.createDb()
        db.deleteDBEntries(datab)

        val timeInMillis = db.dateToMillis(db.getCurrentDate())
        val testTodo = Todo(6, "Test", timeInMillis, null)

        // Add a single entry into the Database
        val retVal = db.addToDb(datab, testTodo)
        assertNotEquals(retVal, -1)

        // Check that only a single entry is inside of the Database
        val todos = db.getAllDb(datab)
        assertEquals(todos.size, 1)

        // Compare the entry with our handmade one
        assert(todos.contains(testTodo))
    }

    // Test the Get Function by adding lots of entries and checking if we get them back
    @Test
    fun multiSelectPass() {
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext

        val db = DatabaseClass(appContext)
        val datab = db.createDb()
        db.deleteDBEntries(datab)

        // Add 51 differing entries to the Database
        val timeInMillis = db.dateToMillis(db.getCurrentDate())
        for(i in 0..50) {
            val testTodo = Todo(i, "Test", timeInMillis, null)
            val retVal = db.addToDb(datab, testTodo)
            assertNotEquals(retVal, -1)
        }

        // Check if there are actually 51 entries
        val todos = db.getAllDb(datab)
        assertEquals(todos.size, 51)

        // Check if all of the entries are there
        for(i in 0..50) {
            assert(todos[i] == Todo(i, "Test", timeInMillis, null))
        }
    }
}