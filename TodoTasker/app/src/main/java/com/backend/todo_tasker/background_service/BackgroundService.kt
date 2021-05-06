package com.backend.todo_tasker

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.*

class BackgroundService : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent?) {
        notificationHelper.notify(context)
        alarmHelper.setNextAlarm(context)
    }
}

class AlarmHelper {
    private var counter = 0
    private var alarmMap = HashMap<Int, PendingIntent?>()
    private var liveAlarmID = -1
    private var liveAlarmTime:Long = -1

    fun setNextAlarm(context: Context?) {
        GlobalScope.launch {
            sharedDbLock.acquire()
            val todo = dbClass.getNextReminder(todoDb)
            sharedDbLock.release()
            if(todo.reminder != null) {
                setNewAlarm(context, todo.reminder)
            }
        }
    }

    fun replaceNextAlarm(context: Context, time: Long) {
        if(liveAlarmTime > time) {
            cancelAlarm(context, liveAlarmID)
            setNewAlarm(context, time)
        }
    }

    fun setNewAlarm(context: Context?, time: Long): Int {

        val intent = Intent(context, BackgroundService::class.java)
        val pendingIntent = PendingIntent.getBroadcast(context, counter, intent, 0)

        val alarmManager = context?.getSystemService(Context.ALARM_SERVICE) as? AlarmManager

        if (pendingIntent != null && alarmManager != null) {
            alarmManager.cancel(pendingIntent)
        }

        val repeatTime: Long = 60000 // 60s in Millis | This is the shortest possible repeat Time!
        alarmManager?.setRepeating(
            AlarmManager.RTC_WAKEUP,
            time,
            repeatTime,
            pendingIntent
        )

        alarmMap[counter] = pendingIntent
        liveAlarmID = counter
        counter += 1

        liveAlarmTime = time

        return counter - 1
    }

    // Needs to be called when Alarm is reached and accepted:
    // https://stackoverflow.com/questions/4315611/android-get-all-pendingintents-set-with-alarmmanager
    fun cancelAlarm(context: Context?, id: Int): Boolean {
        if (!alarmMap.containsKey(id)) {
            return false
        }

        val alarmManager = context?.getSystemService(Context.ALARM_SERVICE) as? AlarmManager
        val pendingIntent = alarmMap[id]

        alarmManager?.cancel(pendingIntent)
        liveAlarmID = -1
        liveAlarmTime = -1
        return true
    }
}