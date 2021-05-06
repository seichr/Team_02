package com.backend.todo_tasker

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import androidx.test.platform.app.InstrumentationRegistry
import com.backend.todo_tasker.database.DatabaseClass
import com.backend.todo_tasker.database.Todo
import com.backend.todo_tasker.database.TodoDatabase
import org.junit.Test
import org.junit.runner.RunWith
import java.util.*
@MediumTest
@RunWith(AndroidJUnit4::class)
class DataBaseBasicInstrumentedTest {
    @Test
    fun addModify() {
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext

        val db = DatabaseClass(appContext)
        val datab = db.createDb()
        db.deleteDBEntries(datab)
        val toInsert : Todo = Todo(1,"test",3,6)

        db.addToDb(datab, toInsert)
        var varget = db.getLastEntry(datab)
        assert(varget.uid == 1 && varget.title == "test" && varget.date == 3.toLong() && varget.reminder == 6.toLong())
        db.modificationEntry(datab, varget.uid, "test2", 5, 9)
        var updated : Todo= db.getSingleEntry(datab,varget.uid)
        assert(updated.title == "test2" && updated.date == 5.toLong() && updated.reminder == 9.toLong())
    }

    @Test
    fun DuplicateTest(){
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext

        val db = DatabaseClass(appContext)
        val datab = db.createDb()
        db.deleteDBEntries(datab)

        val toDuplicate:Todo = Todo(1,"notDouble", 42, 69)
        assert(db.addToDb(datab, toDuplicate)==0)

        db.duplicateDBEntry(datab, 1)
        var duplicate : Todo= db.getLastEntry(datab)
        assert(duplicate.title == "notDouble" && duplicate.date == 42.toLong() && duplicate.reminder == 69.toLong())
    }
    @Test
    fun DeleteTest() {
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext

        val db = DatabaseClass(appContext)
        val datab = db.createDb()
        db.deleteDBEntries(datab)

        val toInsert:Todo = Todo(1,"toDelete", 1338, 420)
        db.addToDb(datab,toInsert)
        var varget = db.getLastEntry(datab)
        assert(varget.uid == 1 && varget.title == "toDelete" && varget.date == 1338.toLong() && varget.reminder == 420.toLong())
        db.deleteDBSingleEntry(datab,1)
        assert(db.getSingleEntry(datab,1)==null)
    }
}