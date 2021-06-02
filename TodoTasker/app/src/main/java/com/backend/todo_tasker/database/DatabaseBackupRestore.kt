package com.backend.todo_tasker.database

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import com.backend.todo_tasker.dbTodoClass
import com.backend.todo_tasker.todoDb
import java.io.*


class DatabaseBackupRestore(Context: Context, activity: Activity?) {

    private var Activity = activity

    private var appContext = Context

    fun getLastRestoreInfo(): Long? {
        var sdir = appContext.getExternalFilesDir("/todoBackup/")
        sdir = File(sdir!!.absolutePath,"todo-database" )
        if(sdir.exists())
            return sdir.lastModified();
        else return null;
    }


    fun backup() {
        if (Activity != null) {
            verifyStoragePermissions(Activity)
        }
        val db = DatabaseTodoClass(appContext)
        val datab = db.createDb()
        datab.close()
        val dbFile = appContext.getDatabasePath("todo-database").absolutePath

        val sdir = appContext.getExternalFilesDir("/todoBackup/")
        if (sdir != null) {
            if (!sdir.exists()) {
                sdir.mkdirs()
            }
            val sfpath =
                sdir.path + File.separator.toString() + "TodoDBBackup"

            CopyFile(File(dbFile), File(sdir, "todo-database"))
            CopyFile(File(dbFile + "-shm"), File(sdir, "todo-database-shm"))
            CopyFile(File(dbFile + "-wal"), File(sdir, "todo-database-wal"))
        }
    }

    fun CopyFile(from: File, to: File) {
        val savefile = to
        if (savefile.exists())
            savefile.delete()
        savefile.createNewFile()
        val buffersize = 8 * 1024
        val buffer = ByteArray(buffersize)
        var bytes_read = buffersize
        val savedb: OutputStream = FileOutputStream(to)
        val indb: InputStream = FileInputStream(from)
        while (indb.read(buffer, 0, buffersize).also({ bytes_read = it }) > 0) {
            savedb.write(buffer, 0, bytes_read)
        }
        savedb.flush()
        indb.close()
        savedb.close()
    }

    fun restore() {
        if (Activity != null) {
            verifyStoragePermissions(Activity)
        }
        todoDb.close()
        val dbFile = appContext.getDatabasePath("todo-database").absolutePath

        val sdir = appContext.getExternalFilesDir("/todoBackup/")
        CopyFile(File(sdir, "todo-database"), File(dbFile))
        CopyFile(File(sdir, "todo-database-shm"), File(dbFile + "-shm"))
        CopyFile(File(sdir, "todo-database-wal"), File(dbFile + "-wal"))
        todoDb = dbTodoClass.createDb()
    }

    fun verifyStoragePermissions(activity: Activity?) {
        val REQUEST_EXTERNAL_STORAGE = 1
        val PERMISSIONS_STORAGE =
            arrayOf<String>( //Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
        val permission = ActivityCompat.checkSelfPermission(
            activity!!,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        )
        if (permission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                activity,
                PERMISSIONS_STORAGE,
                REQUEST_EXTERNAL_STORAGE
            )
        }
    }
}