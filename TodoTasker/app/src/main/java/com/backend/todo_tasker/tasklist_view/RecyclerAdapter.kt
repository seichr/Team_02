package com.backend.todo_tasker.tasklist_view

import android.graphics.Color
import android.text.Editable
import android.text.TextWatcher
import android.text.format.DateFormat
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.PopupWindow
import androidx.recyclerview.widget.RecyclerView
import com.backend.todo_tasker.*
import com.backend.todo_tasker.database.Todo
import kotlinx.android.synthetic.main.recyclerview_item.view.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
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

        private var backgroundDimmerWindow: PopupWindow? = null
        private var modifyTaskWindow:       PopupWindow? = null
        private var modifyTaskView:         View?        = null

        init {
            v.setOnClickListener(this)
        }

        override fun onClick(v: View) {
            val inflater = LayoutInflater.from(view.context)
            val backgroundView: View = inflater.inflate(R.layout.dimming_background, null)
            modifyTaskView = inflater.inflate(R.layout.modify_task_window, null)

            val widthTaskWindow = LinearLayout.LayoutParams.WRAP_CONTENT
            val heightTaskWindow = LinearLayout.LayoutParams.WRAP_CONTENT
            val widthBackgroundWindow = LinearLayout.LayoutParams.MATCH_PARENT
            val heightBackgroundWindow = LinearLayout.LayoutParams.MATCH_PARENT

            backgroundDimmerWindow = PopupWindow(backgroundView, widthBackgroundWindow, heightBackgroundWindow, false)
            backgroundDimmerWindow!!.showAtLocation(view, Gravity.CENTER, 0, 0)

            modifyTaskWindow = PopupWindow(modifyTaskView, widthTaskWindow, heightTaskWindow, true)
            modifyTaskWindow!!.showAtLocation(view, Gravity.CENTER, 0, 0)
            modifyTaskWindow!!.setOnDismissListener {
                backgroundDimmerWindow!!.dismiss()
            }

            val editTextName = modifyTaskView?.findViewById<EditText>(R.id.edittext_name)
            editTextName?.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

                override fun afterTextChanged(s: Editable) {
                    val saveButton = modifyTaskView?.findViewById<Button>(R.id.button_add_to_db)
                    saveButton?.isEnabled = editTextName.text.toString() != ""
                }
            })
            if (editTextName != null && editTextName.text.toString() == "") {
                val saveButton = modifyTaskView?.findViewById<Button>(R.id.button_add_to_db)
                saveButton?.isEnabled = false
            }

            // Fill EditTexts with the proper entries

            val taskName = Editable.Factory.getInstance().newEditable(v.item_title.text)
            val taskDate = Editable.Factory.getInstance().newEditable(v.item_date.text)
            editTextName?.text = taskName
            val editTextDateTime = modifyTaskView?.findViewById<EditText>(R.id.edittext_datetime)
            editTextDateTime?.text = taskDate
            // Configure Cancel-Buttons
            val cancelButton = modifyTaskView?.findViewById<Button>(R.id.button_modify_cancel)
            cancelButton?.setOnClickListener {
                cancelModifyActivity(it)
            }
            val saveButton = modifyTaskView?.findViewById<Button>(R.id.button_modify_to_db)
            saveButton?.setOnClickListener {
                updateModifyActivity(it, editTextName, editTextDateTime)
            }
        }

        fun updateModifyActivity(view: View, editTextName: EditText?, editTextDateTime: EditText?) {
            val title = editTextName?.text.toString()
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
                refreshList()
                sharedDbLock.release()
            }
            cancelModifyActivity(view)
        }

        private fun refreshList() {
            GlobalScope.launch {
                val data = dbClass.getAllDb(todoDb)
                todoList?.post(Runnable {
                    adapter = RecyclerAdapter(data)
                    todoList?.adapter = adapter
                    todoList!!.adapter?.itemCount?.let { todoList?.layoutManager?.scrollToPosition(it - 1) }
                })
            }
        }

        fun cancelModifyActivity(view: View) {
            modifyTaskWindow!!.dismiss()
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
