package com.example.todo_tasker

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_todo_list.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class TodoListActivity : AppCompatActivity() {
    private lateinit var linearLayoutManager: LinearLayoutManager
    private lateinit var adapter: RecyclerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_todo_list)

        linearLayoutManager = LinearLayoutManager(this)
        todo_list.layoutManager = linearLayoutManager

        GlobalScope.launch {
            var data = db.getAllDb(datab)
            adapter = RecyclerAdapter(data)
            todo_list.adapter = adapter
        }

    }
}
