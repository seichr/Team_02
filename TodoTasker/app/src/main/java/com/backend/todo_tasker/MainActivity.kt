package com.backend.todo_tasker

import android.app.DatePickerDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import android.content.Intent
import android.icu.text.SimpleDateFormat
import android.icu.util.Calendar
import android.view.View
import android.widget.DatePicker
import com.backend.todo_tasker.database.DatabaseClass
import com.backend.todo_tasker.database.Todo
import com.backend.todo_tasker.database.TodoDatabase
import com.backend.todo_tasker.tasklist_view.TodoListActivity
import java.util.*
import java.util.concurrent.Semaphore

lateinit var dbClass: DatabaseClass
lateinit var todoDb: TodoDatabase
private val sharedDbLock = Semaphore(1)

class MainActivity : AppCompatActivity() {
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

        //DarePicker Code
        val myCalendar = Calendar.getInstance()
        val edittext = findViewById<EditText>(R.id.editTextDate)
        val date =
            DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                myCalendar.set(Calendar.YEAR, year)
                myCalendar.set(Calendar.MONTH, monthOfYear)
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                val myFormat = "dd/MM/yy"
                val sdf = SimpleDateFormat(myFormat, Locale.US)
                edittext.setText(sdf.format(myCalendar.time))
            }
        edittext.setOnClickListener {
            DatePickerDialog(this, date, myCalendar
                .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH)).show()
        }
    }

    fun addTodoActivity(view: View) {
        val textField: EditText = findViewById(R.id.edittext_name)

        val title = textField.text.toString()
        // TO-DO [For Date]: set this to spinner.
        val myCalendar = Calendar.getInstance()
        val date = myCalendar.timeInMillis
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




