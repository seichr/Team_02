package com.backend.todo_tasker

import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.backend.todo_tasker.database.Category
import com.backend.todo_tasker.database.DatabaseCategoryClass
import com.backend.todo_tasker.database.DatabaseTodoClass
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
class DataBaseCategoryTest {
    @Test
    fun addCategoryPass() {
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext

        val db = DatabaseCategoryClass(appContext)
        val datab = db.createDb()
        db.deleteDBEntries(datab)

        val testCategory = Category(6, "TestCategory", null, null)

        val retVal = db.addToDb(datab, testCategory)
        assertNotEquals(retVal, -1)
    }

    @Test
    fun addCategoryPassAddsDefaultColor() {
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext

        val db = DatabaseCategoryClass(appContext)
        val datab = db.createDb()
        db.deleteDBEntries(datab)

        val testCategory = Category(6, "TestCategory", null, null)

        val retVal = db.addToDb(datab, testCategory)
        assertNotEquals(retVal, -1)

        var last : Category= db.getLastEntry(datab)
        assert(last.color != null)
    }

    @Test
    fun addCategoryPassAddsParent() {
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext

        val db = DatabaseCategoryClass(appContext)
        val datab = db.createDb()
        db.deleteDBEntries(datab)

        val testCategory = Category(6, "TestCategory", null, 0)

        val retVal = db.addToDb(datab, testCategory)
        assertNotEquals(retVal, -1)

        var last : Category= db.getLastEntry(datab)
        assert(last.color == null)
    }
}