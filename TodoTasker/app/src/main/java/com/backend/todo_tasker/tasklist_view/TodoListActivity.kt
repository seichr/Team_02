package com.backend.todo_tasker.tasklist_view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.backend.todo_tasker.R
import com.backend.todo_tasker.dbClass
import com.backend.todo_tasker.todoDb
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class TodoListActivity : AppCompatActivity() {
    private lateinit var linearLayoutManager: LinearLayoutManager
    private lateinit var adapter: RecyclerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_todo_list)

        val todoList = findViewById<RecyclerView>(R.id.todo_list)
        todoList.adapter = RecyclerAdapter(emptyList())

        linearLayoutManager = LinearLayoutManager(this)
        todoList.layoutManager = linearLayoutManager

        GlobalScope.launch {
            val data = dbClass.getAllDb(todoDb)
            this@TodoListActivity.runOnUiThread {
                adapter = RecyclerAdapter(data)
                todoList.adapter = adapter
            }
        }
    }
}
