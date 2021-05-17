package com.backend.todo_tasker

import android.content.Context
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.backend.todo_tasker.database.DatabaseBackupRestore
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class DatabaseBackupRestoreTest {

    @Test
    fun testBackup() {
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        //val backup = DatabaseBackupRestore(appContext)
        //backup.backup()
    }
}