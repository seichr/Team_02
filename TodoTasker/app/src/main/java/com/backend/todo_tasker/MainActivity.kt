package com.backend.todo_tasker

import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.app.TimePickerDialog
import android.app.TimePickerDialog.OnTimeSetListener
import android.content.Intent
import android.icu.text.SimpleDateFormat
import android.icu.util.Calendar
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.backend.todo_tasker.database.DatabaseClass
import com.backend.todo_tasker.database.Todo
import com.backend.todo_tasker.database.TodoDatabase
import com.backend.todo_tasker.tasklist_view.TodoListActivity
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.concurrent.Semaphore

lateinit var dbClass: DatabaseClass
lateinit var todoDb: TodoDatabase
private val sharedDbLock = Semaphore(1)

class MainActivity : AppCompatActivity() {
    private var taskTimeMillis = 0L
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val button = findViewById<Button>(R.id.button_switch_to_list)
        button.setOnClickListener {
            val intent = Intent(this, TodoListActivity::class.java)
            startActivity(intent)
        }

        dbClass = DatabaseClass(applicationContext)
        todoDb = dbClass.createDb()

        //DatePicker Code
        val dateInputEditText = findViewById<EditText>(R.id.edittext_datetime)
        dateInputEditText.setOnClickListener {
            val calendar = Calendar.getInstance()
            val dateSetListener =
                OnDateSetListener { view, year, month, dayOfMonth ->
                    calendar[Calendar.YEAR] = year
                    calendar[Calendar.MONTH] = month
                    calendar[Calendar.DAY_OF_MONTH] = dayOfMonth
                    val timeSetListener =
                        OnTimeSetListener { view, hourOfDay, minute ->
                            calendar[Calendar.HOUR_OF_DAY] = hourOfDay
                            calendar[Calendar.MINUTE] = minute
                            val simpleDateFormat =
                                SimpleDateFormat("dd-MM-yy HH:mm")
                            dateInputEditText.setText(simpleDateFormat.format(calendar.time))
                            taskTimeMillis = calendar.timeInMillis
                        }
                    TimePickerDialog(
                        this@MainActivity,
                        timeSetListener,
                        calendar[Calendar.HOUR_OF_DAY],
                        calendar[Calendar.MINUTE],
                        false
                    ).show()
                }

            DatePickerDialog(
                this@MainActivity,
                dateSetListener,
                calendar[Calendar.YEAR],
                calendar[Calendar.MONTH],
                calendar[Calendar.DAY_OF_MONTH]
            ).show()
        }
    }

    fun addTodoActivity(view: View) {
        val textField: EditText = findViewById(R.id.edittext_name)

        val title = textField.text.toString()
        val date = taskTimeMillis
        val reminder = 0 // TOOD: Change

        GlobalScope.launch {
            sharedDbLock.acquire()
            if (dbClass.getLastEntry(todoDb) == null) { // This is not always false...
                dbClass.addToDb(todoDb, Todo(0,
                    title,
                    date.toLong(),
                    reminder.toLong()))
            } else {
                dbClass.addToDb(todoDb, Todo(dbClass.getLastEntry(todoDb).uid + 1,
                    title,
                    date.toLong(),
                    reminder.toLong()))
            }
            sharedDbLock.release()
        }
    }

}




