package com.backend.todo_tasker

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.backend.todo_tasker.database.DatabaseClass
import com.backend.todo_tasker.database.Todo
import com.backend.todo_tasker.database.TodoDatabase
import com.backend.todo_tasker.language.LanguageHelper
import com.backend.todo_tasker.tasklist_view.TodoListActivity
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.concurrent.Semaphore

lateinit var dbClass: DatabaseClass
lateinit var todoDb: TodoDatabase
private val sharedDbLock = Semaphore(1)
private var languageHelper = LanguageHelper()

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
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu_main, menu)
        return true
    }

    fun changeLanguageActivity(item: MenuItem) {
        languageHelper.toggleLanguage(resources, this)
    }

    fun addTodoActivity(view: View) {
        val textField: EditText = findViewById(R.id.edittext_name)

        val title = textField.text.toString()
        // TO-DO [For Date]: set this to spinner.
        val date = 0 // TODO: Change
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




