package com.backend.todo_tasker.background_service

import android.content.Context
import androidx.core.app.NotificationCompat
import com.backend.todo_tasker.R

class NotificationHelper {

    fun foo(context: Context) {

        val textTitle = "Notification"
        val textContent = "Test content"
        val channel_id = "todo_notifications"

        var builder = NotificationCompat.Builder(context, channel_id)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle(textTitle)
            .setContentText(textContent)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
    }

}