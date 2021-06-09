package com.backend.todo_tasker

import android.os.Bundle
import android.text.SpannableStringBuilder
import android.view.*
import android.widget.*
import android.widget.Toast.LENGTH_LONG
import androidx.appcompat.app.AppCompatActivity
import com.backend.todo_tasker.background_service.NotificationHelper
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.backend.todo_tasker.database.DatabaseBackupRestore
import com.backend.todo_tasker.button_functions.DateTimePickerFunctions
import com.backend.todo_tasker.database.DatabaseTodoClass
import com.backend.todo_tasker.database.TodoDatabase
import com.backend.todo_tasker.db_operations.DbOperations
import com.backend.todo_tasker.language.LanguageHelper
import com.backend.todo_tasker.popup_window.PopUpWindowInflater
import com.backend.todo_tasker.popup_window.WINDOWTYPE
import com.backend.todo_tasker.tasklist_view.RecyclerAdapter
import java.util.concurrent.Semaphore
import androidx.appcompat.widget.Toolbar
import java.text.SimpleDateFormat
import java.util.*
import com.backend.todo_tasker.button_functions.MenuFunctions


lateinit var dbTodoClass: DatabaseTodoClass
lateinit var todoDb: TodoDatabase
lateinit var dbBackupRestore: DatabaseBackupRestore

val sharedDbLock = Semaphore(1)

private var languageHelper = LanguageHelper()
val notificationHelper = NotificationHelper()
val alarmHelper = AlarmHelper()

lateinit var adapter: RecyclerAdapter
var todoList: RecyclerView? = null
var taskTimeMillis = 0L

class MainActivity : AppCompatActivity() {

    private lateinit var linearLayoutManager: LinearLayoutManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        dbTodoClass = DatabaseTodoClass(applicationContext)
        todoDb = dbTodoClass.createDb()
        dbBackupRestore = DatabaseBackupRestore(applicationContext, this)

        loadTodoList()
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
        PopUpWindowInflater().getInstance().dismissAddTaskWindow()
    }

    fun openAddWindowActivity(view: View) {
        PopUpWindowInflater().getInstance().inflateWindow(view, WINDOWTYPE.ADD)
    }

    fun addTodoActivity(view: View) {
        val textField: EditText? =
            PopUpWindowInflater().getInstance().getAddTaskView()?.findViewById(R.id.edittext_name)

        val title = textField?.text.toString()
        val date = taskTimeMillis

        DbOperations().getInstance().addOperation(title, date)

        alarmHelper.replaceNextAlarm(applicationContext, date)
        cancelAddActivity(view)
    }


    fun clickOnDateTimeField(view: View) {
        DateTimePickerFunctions().clickOnDateTimeField(view, WINDOWTYPE.ADD)
    }

    private fun openMenuActivity(view: View) {
        PopUpWindowInflater().getInstance().inflateWindow(view, WINDOWTYPE.MENU)
    }

    fun changeToDarkMode(view: View) {
        MenuFunctions().darkModeFunction()
    }

    fun changeToLightMode(view: View) {
        MenuFunctions().lightModeFunction()
    }


    fun openBackupAndRestoreWindowActivity(view: View) {
        setContentView(R.layout.backup_and_restore_window)

        var timeAsNumber = dbBackupRestore.getLastRestoreInfo()
        if (timeAsNumber != null) {
            var date =  Date(timeAsNumber)
            val format = SimpleDateFormat("dd.MM.yyyy")
            val dateAsString = format.format(date)
            val text = this.findViewById<View>(android.R.id.content).findViewById<TextView>(R.id.textView_LastBackup)
            if (text != null) {
                  text.text = SpannableStringBuilder(dateAsString.toString())
            }
        }

        dbBackupRestore.setExportString(this)

        PopUpWindowInflater().getInstance().dismissMenuWindow()
    }

    fun openMainWindowActivity(view: View) {
        setContentView(R.layout.activity_main)
        loadTodoList()
    }

    fun exportToFile(view: View) {
        dbBackupRestore.backup()
        openMainWindowActivity(view)

        val backupSuccessful = getString(R.string.STRING_BACKUP_SUCCESSFUL)
        Toast.makeText(applicationContext, backupSuccessful, LENGTH_LONG).show()
    }

    fun restoreFromFile(view: View) {
        dbBackupRestore.restore()
        openMainWindowActivity(view)
        val restoreSuccessful = getString(R.string.STRING_RESTORE_SUCCESSFUL)
        Toast.makeText(applicationContext, restoreSuccessful, LENGTH_LONG).show()
    }

    private fun loadTodoList() {
        todoList = findViewById(R.id.todo_list)
        todoList?.adapter = RecyclerAdapter().getInstance()

        linearLayoutManager = LinearLayoutManager(this)
        todoList?.layoutManager = linearLayoutManager

        val dividerItemDecoration = DividerItemDecoration(
            todoList?.context,
            linearLayoutManager.orientation
        )
        todoList?.addItemDecoration(dividerItemDecoration)

        val toolbar: Toolbar? = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        if (supportActionBar != null) {
            supportActionBar!!.setDisplayHomeAsUpEnabled(true)
            supportActionBar!!.setHomeAsUpIndicator(R.drawable.baseline_menu_24)
            supportActionBar!!.title = applicationContext.getString(R.string.STRING_APP_NAME)
        }

        toolbar!!.setNavigationOnClickListener {
            openMenuActivity(it)
        }

        DbOperations().getInstance().refreshListView()
    }
}




