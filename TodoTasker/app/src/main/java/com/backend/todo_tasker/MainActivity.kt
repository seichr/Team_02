package com.backend.todo_tasker

import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.app.TimePickerDialog
import android.app.TimePickerDialog.OnTimeSetListener
import android.icu.text.SimpleDateFormat
import android.icu.util.Calendar
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.backend.todo_tasker.database.DatabaseClass
import com.backend.todo_tasker.database.Todo
import com.backend.todo_tasker.database.TodoDatabase
import com.backend.todo_tasker.language.LanguageHelper
import com.backend.todo_tasker.tasklist_view.RecyclerAdapter
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.concurrent.Semaphore

lateinit var dbClass: DatabaseClass
lateinit var todoDb: TodoDatabase
private val sharedDbLock = Semaphore(1)
private var languageHelper = LanguageHelper()

class MainActivity : AppCompatActivity() {
    private var taskTimeMillis = 0L
    private lateinit var linearLayoutManager: LinearLayoutManager
    private lateinit var adapter: RecyclerAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        dbClass = DatabaseClass(applicationContext)
        todoDb = dbClass.createDb()

        val todoList = findViewById<RecyclerView>(R.id.todo_list)
        todoList.adapter = RecyclerAdapter(emptyList())

        linearLayoutManager = LinearLayoutManager(this)
        todoList.layoutManager = linearLayoutManager

        GlobalScope.launch {
            val dividerItemDecoration = DividerItemDecoration(todoList.getContext(),
                linearLayoutManager.getOrientation());
            todoList.addItemDecoration(dividerItemDecoration)
            val data = dbClass.getAllDb(todoDb)
            this@MainActivity.runOnUiThread {
                adapter = RecyclerAdapter(data)
                todoList.adapter = adapter
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu_main, menu)
        return true
    }

    fun changeLanguageActivity(item: MenuItem) {
        languageHelper.toggleLanguage(resources, this)
    }

    /*
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
    fun clickOnDateTimeField(view: View){
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
                        val dateInputEditText = findViewById<EditText>(R.id.edittext_datetime)
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
         */
}




