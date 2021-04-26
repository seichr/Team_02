package com.backend.todo_tasker

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import org.junit.Test
import org.junit.runner.RunWith
import java.time.LocalDateTime

@MediumTest
@RunWith(AndroidJUnit4::class)
class ReminderTest {
    @Test
    fun Set_And_Cancel_Alarm() {
        val appContext: Context = ApplicationProvider.getApplicationContext()
        val alarm = AlarmHelper()
        val retVal = alarm.setNewAlarm(appContext, LocalDateTime.now().plusSeconds(60))
        assert(retVal >= 0)
        assert(alarm.cancelAlarm(appContext, retVal))
    }
}