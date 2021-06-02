package com.backend.todo_tasker.button_functions

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.icu.text.SimpleDateFormat
import android.icu.util.Calendar
import android.view.View
import android.widget.EditText
import com.backend.todo_tasker.BuildConfig
import com.backend.todo_tasker.R
import com.backend.todo_tasker.popup_window.PopUpWindowInflater
import com.backend.todo_tasker.popup_window.WINDOWTYPE
import com.backend.todo_tasker.taskTimeMillis

class DateTimePickerFunctions {
    fun clickOnDateTimeField(view: View, type: WINDOWTYPE){
        val calendar = Calendar.getInstance()
        val dateSetListener =
                DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
                    calendar[Calendar.YEAR] = year
                    calendar[Calendar.MONTH] = month
                    calendar[Calendar.DAY_OF_MONTH] = dayOfMonth
                    val timeSetListener =
                            TimePickerDialog.OnTimeSetListener { _, hourOfDay, minute ->
                                calendar[Calendar.HOUR_OF_DAY] = hourOfDay
                                calendar[Calendar.MINUTE] = minute
                                val simpleDateFormat = SimpleDateFormat(view.context.getString(R.string.STRING_DATETIMEFORMAT))
                                var dateInputEditText: EditText? = null
                                when(type) {
                                    WINDOWTYPE.ADD -> {
                                        dateInputEditText =
                                                PopUpWindowInflater().getInstance().getAddTaskView()?.findViewById(R.id.edittext_datetime)
                                    }
                                    WINDOWTYPE.MODIFY -> {
                                        dateInputEditText =
                                                PopUpWindowInflater().getInstance().getModifyTaskView()?.findViewById(R.id.edittext_modify_datetime)
                                    }
                                    else -> {
                                        if (BuildConfig.DEBUG) {
                                            error("We shouldnt come here... Check Type")
                                        }
                                    }
                                }
                                dateInputEditText?.setText(simpleDateFormat.format(calendar.time))
                                taskTimeMillis = calendar.timeInMillis
                            }
                    TimePickerDialog(
                            view.context,
                            R.style.TimePickerTheme,
                            timeSetListener,
                            calendar[Calendar.HOUR_OF_DAY],
                            calendar[Calendar.MINUTE],
                            true
                    ).show()
                }

        DatePickerDialog(
                view.context,
                R.style.DatePickerTheme,
                dateSetListener,
                calendar[Calendar.YEAR],
                calendar[Calendar.MONTH],
                calendar[Calendar.DAY_OF_MONTH]
        ).show()
    }
}