package com.backend.todo_tasker

import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.app.TimePickerDialog
import android.app.TimePickerDialog.OnTimeSetListener
import android.icu.text.SimpleDateFormat
import android.icu.util.Calendar
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.*
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.backend.todo_tasker.database.DatabaseClass
import com.backend.todo_tasker.database.Todo
import com.backend.todo_tasker.database.TodoDatabase
import com.backend.todo_tasker.language.LanguageHelper
import com.backend.todo_tasker.tasklist_view.RecyclerAdapter
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.concurrent.Semaphore


lateinit var dbClass: DatabaseClass
lateinit var todoDb: TodoDatabase
val sharedDbLock = Semaphore(1)
private var languageHelper = LanguageHelper()
lateinit var adapter: RecyclerAdapter
var todoList: RecyclerView? = null
var taskTimeMillis = 0L

class MainActivity : AppCompatActivity() {

    private lateinit var linearLayoutManager: LinearLayoutManager

    private var backgroundDimmerWindow: PopupWindow? = null
    private var addTaskWindow:          PopupWindow? = null
    private var addTaskView:            View?        = null



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        dbClass = DatabaseClass(applicationContext)
        todoDb = dbClass.createDb()

        todoList = findViewById(R.id.todo_list)
        todoList?.adapter = RecyclerAdapter(emptyList())

        linearLayoutManager = LinearLayoutManager(this)
        todoList?.layoutManager = linearLayoutManager

        GlobalScope.launch {
            val dividerItemDecoration = DividerItemDecoration(todoList?.context,
                    linearLayoutManager.orientation)
            todoList?.addItemDecoration(dividerItemDecoration)
        }
        refreshListView()
    }

    private fun refreshListView() {
        GlobalScope.launch {
            val data = dbClass.getAllDb(todoDb)
            this@MainActivity.runOnUiThread {
                adapter = RecyclerAdapter(data)
                todoList?.adapter = adapter
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

    fun cancelAddActivity(view: View) {
        addTaskWindow!!.dismiss()
    }

    fun openAddWindowActivity(view: View) {
        val inflater = getSystemService(LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val backgroundView: View = inflater.inflate(R.layout.dimming_background, null)
        addTaskView = inflater.inflate(R.layout.create_task_window, null)

        val widthTaskWindow = LinearLayout.LayoutParams.WRAP_CONTENT
        val heightTaskWindow = LinearLayout.LayoutParams.WRAP_CONTENT
        val widthBackgroundWindow = LinearLayout.LayoutParams.MATCH_PARENT
        val heightBackgroundWindow = LinearLayout.LayoutParams.MATCH_PARENT

        backgroundDimmerWindow = PopupWindow(backgroundView, widthBackgroundWindow, heightBackgroundWindow, false)
        backgroundDimmerWindow!!.showAtLocation(view, Gravity.CENTER, 0, 0)

        addTaskWindow = PopupWindow(addTaskView, widthTaskWindow, heightTaskWindow, true)
        addTaskWindow!!.showAtLocation(view, Gravity.CENTER, 0, 0)
        addTaskWindow!!.setOnDismissListener {
            backgroundDimmerWindow!!.dismiss()
        }

        val editText = addTaskView?.findViewById<EditText>(R.id.edittext_name)
        editText?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable) {
                val saveButton = addTaskView?.findViewById<Button>(R.id.button_add_to_db)
                saveButton?.isEnabled = editText.text.toString() != ""
            }
        })
        if (editText != null && editText.text.toString() == "") {
            val saveButton = addTaskView?.findViewById<Button>(R.id.button_add_to_db)
            saveButton?.isEnabled = false
        }
    }

    fun addTodoActivity(view: View) {
        val textField: EditText = addTaskView!!.findViewById(R.id.edittext_name)

        val title = textField.text.toString()
        val date = taskTimeMillis
        val reminder = 0 // TODO: Change


        GlobalScope.launch {
            sharedDbLock.acquire()
            if (dbClass.getLastEntry(todoDb) == null) { // This is not always false...
                dbClass.addToDb(todoDb, Todo(0,
                        title,
                        date,
                        reminder.toLong()))
            } else {
                dbClass.addToDb(todoDb, Todo(dbClass.getLastEntry(todoDb).uid + 1,
                        title,
                        date,
                        reminder.toLong()))
            }
            refreshListView()
            this@MainActivity.runOnUiThread {
                todoList!!.adapter?.itemCount?.let { todoList?.layoutManager?.scrollToPosition(it - 1) }
            }
            sharedDbLock.release()
        }
        cancelAddActivity(view)
    }

    fun clickOnDateTimeField(view: View){
        val calendar = Calendar.getInstance()
        val dateSetListener =
            OnDateSetListener { _, year, month, dayOfMonth ->
                calendar[Calendar.YEAR] = year
                calendar[Calendar.MONTH] = month
                calendar[Calendar.DAY_OF_MONTH] = dayOfMonth
                val timeSetListener =
                    OnTimeSetListener { _, hourOfDay, minute ->
                        calendar[Calendar.HOUR_OF_DAY] = hourOfDay
                        calendar[Calendar.MINUTE] = minute
                        val simpleDateFormat = SimpleDateFormat("dd.MM.yy HH:mm")
                        val dateInputEditText = addTaskView?.findViewById<EditText>(R.id.edittext_datetime)
                        dateInputEditText?.setText(simpleDateFormat.format(calendar.time))
                        taskTimeMillis = calendar.timeInMillis
                    }
                TimePickerDialog(
                        this@MainActivity,
                        R.style.DatePickerTheme,
                        timeSetListener,
                        calendar[Calendar.HOUR_OF_DAY],
                        calendar[Calendar.MINUTE],
                        true
                ).show()
            }

        DatePickerDialog(
                this@MainActivity,
                R.style.DatePickerTheme,
                dateSetListener,
                calendar[Calendar.YEAR],
                calendar[Calendar.MONTH],
                calendar[Calendar.DAY_OF_MONTH]
        ).show()
    }
}




