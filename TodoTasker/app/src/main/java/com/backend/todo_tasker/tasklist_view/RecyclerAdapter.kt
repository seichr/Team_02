package com.backend.todo_tasker.tasklist_view

import android.text.format.DateFormat
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.backend.todo_tasker.*
import com.backend.todo_tasker.database.Todo
import com.backend.todo_tasker.popup_window.PopUpWindowInflater
import com.backend.todo_tasker.popup_window.WINDOWTYPE
import kotlinx.android.synthetic.main.recyclerview_item.view.*
import java.util.*


class RecyclerAdapter: RecyclerView.Adapter<RecyclerAdapter.TodoHolder>()  {

    companion object {
        private var instance: RecyclerAdapter? = null
        private var todos: List<Todo> = emptyList()
    }


    fun setData(data: List<Todo>) {
        todos = data
    }

    fun getInstance(): RecyclerAdapter {
        if(instance == null) {
            instance = RecyclerAdapter()
        }
        return instance as RecyclerAdapter
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodoHolder {
        val inflatedView = parent.inflate(R.layout.recyclerview_item, false)
        return TodoHolder(inflatedView)
    }

    override fun getItemCount(): Int {
        return todos.size
    }

    override fun onBindViewHolder(holder: TodoHolder, position: Int) {
        if (position %2 == 1) {
            holder.itemView.setBackgroundColor(ContextCompat.getColor(holder.itemView.context, R.color.tasks1))
        }
        else {
            holder.itemView.setBackgroundColor(ContextCompat.getColor(holder.itemView.context, R.color.tasks2))
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
            PopUpWindowInflater().getInstance().inflateWindow(v, WINDOWTYPE.MODIFY, adapterPosition = adapterPosition)
        }

        fun bindTodo(todo: Todo) {
            this.todo = todo
            view.item_title.text = todo.title
            view.item_uid.text = todo.uid.toString()
            if(todo.date!= null && todo.date != 0.toLong()) {
                val test: String = this.view.context.getString(R.string.STRING_DATETIMEFORMAT)
                view.item_date.text = DateFormat.format(test, Date(todo.date)).toString()
            }
        }
    }
}
