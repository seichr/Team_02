package com.backend.todo_tasker

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.*

class BackgroundService : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        val retVal = doWork()
        if(retVal != 0) {
            println("[DEBUG] doWork was not successful!")
        }
    }

    private fun doWork(): Int {
        val cal = Calendar.getInstance()
        println("Current time in millis is: " + cal.timeInMillis)
        return 0
    }
}

class AlarmHelper {
    private var counter = 0
    private var alarmMap = HashMap<Int, PendingIntent?>()

    fun setNewAlarm(context: Context?, dateTime: LocalDateTime): Int {
        val zoneTimeDate = dateTime.atZone(ZoneId.of(ZoneId.systemDefault().toString()))
        val time = zoneTimeDate.toInstant().toEpochMilli()

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
        counter += 1

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
        return true
    }
}