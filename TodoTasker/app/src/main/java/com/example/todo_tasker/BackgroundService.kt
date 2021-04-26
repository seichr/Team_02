package com.example.todo_tasker

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import java.time.LocalDateTime
import java.util.*


class BackgroundService : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        doWork()
    }

    private fun doWork(): Int {

        val cal = Calendar.getInstance()
        println("Current time in millis is: " + cal.timeInMillis)

        return 0
    }
}

class AlarmHelper {
    private var counter = 0
    private var alarm_map = HashMap<Int, PendingIntent?>()

    fun setNewAlarm(context: Context?, dateTime: LocalDateTime): Int {

       val intent = Intent(context, BackgroundService::class.java)

       val alarmManager = context?.getSystemService(Context.ALARM_SERVICE) as? AlarmManager
       val pendingIntent =
               PendingIntent.getBroadcast(context, counter, intent,
                       0)
       if (pendingIntent != null && alarmManager != null) {
           alarmManager.cancel(pendingIntent)
       }

       val repeatTime : Long = 60000
       val calendar = Calendar.getInstance()
       alarmManager?.setRepeating(AlarmManager.RTC_WAKEUP,
               calendar.timeInMillis,
               repeatTime,
               pendingIntent)

        alarm_map[counter] = pendingIntent
        counter += 1

       return counter - 1
    }

    // Needs to be called when databank:
    // https://stackoverflow.com/questions/4315611/android-get-all-pendingintents-set-with-alarmmanager
    fun cancelAlarm(context: Context?, id: Int): Boolean {
        if(!alarm_map.containsKey(id)) {
            return false
        }

        val alarmManager = context?.getSystemService(Context.ALARM_SERVICE) as? AlarmManager
        val pendingIntent = alarm_map[id]

        alarmManager?.cancel(pendingIntent)
        return true
    }
}