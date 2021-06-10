package com.backend.todo_tasker.background_service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.backend.todo_tasker.R

class NotificationHelper {
    private val CHANNEL_ID = "todoNotifications"
    private var notificationId = 0

    private fun createNotificationChannel(context: Context) {
        with(NotificationManagerCompat.from(context)) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O &&
                getNotificationChannel(CHANNEL_ID) == null) {
                val name = "TodoTasker NotificationChannel"
                val descriptionText = "TodoTasker Notifications"
                val importance = NotificationManager.IMPORTANCE_DEFAULT
                val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                    description = descriptionText
                }
                createNotificationChannel(channel)
            }
        }
    }

    fun notify(context: Context) {
        createNotificationChannel(context)

        val textTitle = context.getString(R.string.NOTIFICATION_TITLE)
        val textContent = context.getString(R.string.NOTIFICATION_TEXT)

        var builder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle(textTitle)
            .setContentText(textContent)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        with(NotificationManagerCompat.from(context)) {
            notify(notificationId, builder.build())
            notificationId++
        }
    }
}