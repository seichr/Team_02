package com.backend.todo_tasker

import android.Manifest
import android.content.Context
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.GrantPermissionRule
import com.backend.todo_tasker.database.DatabaseBackupRestore
import org.junit.Test
import org.junit.runner.RunWith
import java.io.File


@RunWith(AndroidJUnit4::class)
class DatabaseBackupRestoreTest {

    @Test
    fun testBackup() {
        GrantPermissionRule.grant(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        GrantPermissionRule.grant(Manifest.permission.READ_EXTERNAL_STORAGE)
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        val backup = DatabaseBackupRestore(appContext, null)
        backup.backup()
        val f= appContext.getExternalFilesDir("/todoBackup/")
        if(f!=null)
            assert(File(f.absolutePath+"/todo-database").exists()&&
                    File(f.absolutePath+"/todo-database-wal").exists()&&File(f.absolutePath+"/todo-database-shm").exists())
        else
            assert(false)
    }

    @Test
    fun testRestore() {

        GrantPermissionRule.grant(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        GrantPermissionRule.grant(Manifest.permission.READ_EXTERNAL_STORAGE)
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        val backup = DatabaseBackupRestore(appContext, null)
        backup.restore()
        val f= appContext.getExternalFilesDir("/todoBackup/")
        val file = File(f,"/ToDoDatabaseBackup")
        if(file!=null)
            assert(file.exists())
        else
            assert(false)
    }
}