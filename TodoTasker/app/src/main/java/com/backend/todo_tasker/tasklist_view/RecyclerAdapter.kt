package com.backend.todo_tasker.tasklist_view

import android.graphics.Color
import android.text.format.DateFormat
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.backend.todo_tasker.R
import com.backend.todo_tasker.database.Todo
import kotlinx.android.synthetic.main.recyclerview_item.view.*
import java.util.*

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
        if (position %2 == 1) {
            holder.itemView.setBackgroundColor(Color.parseColor("#FFFAF8FD"));
        }
        else {
            holder.itemView.setBackgroundColor(Color.parseColor("#FFFFFF"));
        }
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
            if(todo.date!= null)
                view.item_date.text = DateFormat.format("dd.MM.yyyy - hh:mm", Date(todo.date)).toString()
        }
    }
}
