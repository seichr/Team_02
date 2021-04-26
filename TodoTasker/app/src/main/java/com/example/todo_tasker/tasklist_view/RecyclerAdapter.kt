package com.example.todo_tasker

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.recyclerview_item.view.*

class RecyclerAdapter(private val todos: List<Todo>) :
    RecyclerView.Adapter<RecyclerAdapter.TodoHolder>()  {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodoHolder {
        val inflatedView = parent.inflate(R.layout.recyclerview_item, false)
        return TodoHolder(inflatedView)
    }

    override fun getItemCount(): Int {
        return todos.size
    }

    override fun onBindViewHolder(holder: TodoHolder, position: Int) {
        val itemTodo = todos[position]
        holder.bindTodo(itemTodo)
    }


    class TodoHolder(v: View) : RecyclerView.ViewHolder(v), View.OnClickListener {
        private var view: View = v
        private var todo: Todo? = null

        init {
            v.setOnClickListener(this)
        }

        override fun onClick(v: View) {
            // Future TO-DO: Change what happens when you press on a element
        }

        fun bindTodo(todo: Todo) {
            this.todo = todo
            // Only way to access the Recycler Items. Android Studio might be sad but its ok :'/
            // See --> Kotlin Extensions
            view.item_title.text = todo.title
            view.item_date.text = todo.date.toString()
            view.item_reminder.text = todo.reminder.toString()
        }

    }
}
