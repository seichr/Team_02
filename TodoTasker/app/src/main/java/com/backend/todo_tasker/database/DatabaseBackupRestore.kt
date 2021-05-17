package com.backend.todo_tasker.database

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat


class DatabaseBackupRestore(Context:Context, activity: Activity?) {

    private var Activity = activity

    private var appContext = Context
    fun backup() {

        val db = DatabaseClass(appContext)
        val datab = db.createDb()
        var title = (db.getLastEntry(datab).title)

        print(title)

    }
}