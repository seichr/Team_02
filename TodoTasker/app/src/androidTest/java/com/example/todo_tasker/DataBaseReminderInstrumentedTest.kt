package com.example.todo_tasker

import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class DataBaseReminderInstrumentedTest {
    // Single entry with reminder info
    @Test
    fun SingleAddPass() {
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext

        val db = DatabaseClass(appContext)
        val datab = db.createDb()
        db.deleteDBEntries(datab)

        val time_in_millis = db.dateToMillis(db.getCurrentDate())
        val test_todo = Todo(6, "Test", time_in_millis, time_in_millis)

        val ret_val = db.addToDb(datab, test_todo)
        assertNotEquals(ret_val, -1)
    }

    // Loads of Entries with reminder info
    @Test
    fun MultiAddPass() {
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext

        val db = DatabaseClass(appContext)
        val datab = db.createDb()
        db.deleteDBEntries(datab)

        val time_in_millis = db.dateToMillis(db.getCurrentDate())
        for(i in 0..50) {
            val test_todo = Todo(i, "Test", time_in_millis, time_in_millis)
            val ret_val = db.addToDb(datab, test_todo)
            assertNotEquals(ret_val, -1)
        }
    }

    // Loads of Entries that should fail as the UID is the same
    @Test
    fun MultiUIDFail() {
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext

        val db = DatabaseClass(appContext)
        val datab = db.createDb()
        db.deleteDBEntries(datab)

        val time_in_millis = db.dateToMillis(db.getCurrentDate())
        var test_todo = Todo(1337, "TestThatPasses", time_in_millis, time_in_millis)
        var ret_val = db.addToDb(datab, test_todo)
        assertNotEquals(ret_val, -1)

        for(i in 0..50) {
            test_todo = Todo(1337, "TestThatFails", time_in_millis, time_in_millis)
            ret_val = db.addToDb(datab, test_todo)
            assertEquals(ret_val, -1)
        }
    }
}