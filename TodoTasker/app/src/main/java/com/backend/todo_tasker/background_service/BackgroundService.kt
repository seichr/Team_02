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
    private var counter:Long = 0
    private var alarmMap = HashMap<Long, PendingIntent?>()
    private val NO_ALARM_SET:Long = -1
    private var liveAlarmID:Long = NO_ALARM_SET
    private var liveAlarmTime:Long = NO_ALARM_SET

    fun setNextAlarm(context: Context?) {
        GlobalScope.launch {
            sharedDbLock.acquire()
            val todo = dbTodoClass.getNextDate(todoDb)
            sharedDbLock.release()
            cancelAlarm(context, liveAlarmID)
            if(todo != null && todo.date != null) {
                setNewAlarm(context, todo.date)
            }
        }
    }

    fun replaceNextAlarm(context: Context, time: Long) {
        if (time > System.currentTimeMillis()) {
            if (liveAlarmTime > time) {
                cancelAlarm(context, liveAlarmID)
                setNewAlarm(context, time)
            } else if (liveAlarmTime == NO_ALARM_SET) {
                setNewAlarm(context, time)
            }
        }
    }

    fun setNewAlarm(context: Context?, time: Long): Long {

        val intent = Intent(context, BackgroundService::class.java)
        val pendingIntent = PendingIntent.getBroadcast(context, counter.toInt(), intent, 0)

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
    fun cancelAlarm(context: Context?, id: Long): Boolean {
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