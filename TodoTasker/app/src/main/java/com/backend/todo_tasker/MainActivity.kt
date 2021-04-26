package com.backend.todo_tasker

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import android.content.Intent
import com.backend.todo_tasker.database.DatabaseClass
import com.backend.todo_tasker.database.Todo
import com.backend.todo_tasker.database.TodoDatabase
import com.backend.todo_tasker.tasklist_view.TodoListActivity
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

        val addButton: Button = findViewById(R.id.button_add_to_db)
        addButton.setOnClickListener {
            val textField: EditText = findViewById(R.id.edittext_name)

            val title = textField.text.toString()
            // TO-DO [For Date]: set this to spinner.
            val date = 0
            val reminder = 0

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
}




