package com.backend.todo_tasker.tasklist_view

import android.R.attr.x
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.graphics.Color
import android.icu.text.SimpleDateFormat
import android.icu.util.Calendar
import android.text.Editable
import android.text.TextWatcher
import android.text.format.DateFormat
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
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

        private var currentUID: Int? = null

        private var backgroundDimmerWindow: PopupWindow? = null
        private var modifyTaskWindow:       PopupWindow? = null
        private var moreOptionsTaskWindow:  PopupWindow? = null
        private var modifyTaskView:         View?        = null
        private var moreOptionsTaskView:    View?        = null

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

            val editTextName = modifyTaskView?.findViewById<EditText>(R.id.edittext_modify_name)
            editTextName?.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

                override fun afterTextChanged(s: Editable) {
                    val saveButton = modifyTaskView?.findViewById<Button>(R.id.button_modify_to_db)
                    saveButton?.isEnabled = editTextName.text.toString() != ""
                }
            })
            if (editTextName != null && editTextName.text.toString() == "") {
                val saveButton = modifyTaskView?.findViewById<Button>(R.id.button_modify_to_db)
                saveButton?.isEnabled = false
            }

            // Fill EditTexts with the proper entries

            val taskName = Editable.Factory.getInstance().newEditable(v.item_title.text)
            val taskDate = Editable.Factory.getInstance().newEditable(v.item_date.text)

            editTextName?.text = taskName

            // Configure Buttons and EditText
            val cancelButton = modifyTaskView?.findViewById<Button>(R.id.button_modify_cancel)
            cancelButton?.setOnClickListener {
                cancelModifyActivity(it)
            }
            val saveButton = modifyTaskView?.findViewById<Button>(R.id.button_modify_to_db)
            saveButton?.setOnClickListener {
                updateModifyActivity(it, editTextName, v.item_uid)
            }
            val editTextDateTime = modifyTaskView?.findViewById<EditText>(R.id.edittext_modify_datetime)
            editTextDateTime?.setOnClickListener {
                clickOnDateTimeField(it)
            }
            editTextDateTime?.text = taskDate
            val moreOptionsButton = modifyTaskView?.findViewById<ImageButton>(R.id.button_more_options)
            moreOptionsButton?.setOnClickListener {
                openMoreOptionsWindows(it, v, v.item_uid)
            }
        }

        fun openMoreOptionsWindows(it: View, v: View, itemUid: TextView) {
            val inflater = LayoutInflater.from(v.context)
            moreOptionsTaskView = inflater.inflate(R.layout.more_options_window, null)

            currentUID = itemUid.text.toString().toInt()
            val widthTaskWindow = LinearLayout.LayoutParams.WRAP_CONTENT
            val heightTaskWindow = LinearLayout.LayoutParams.WRAP_CONTENT

            moreOptionsTaskWindow = PopupWindow(moreOptionsTaskView, widthTaskWindow, heightTaskWindow, true)
            moreOptionsTaskWindow!!.showAtLocation(view, Gravity.CENTER, 0, -300)
            //TODO: Y-Position Hardcoded for now Could not find correct Pos from Vars

            val duplicateButton = moreOptionsTaskView?.findViewById<Button>(R.id.button_duplicate_task)
            duplicateButton?.setOnClickListener {
                duplicateTask(it, currentUID!!)
                moreOptionsTaskWindow!!.dismiss()
            }
            val deleteButton = moreOptionsTaskView?.findViewById<Button>(R.id.button_delete_task)
            deleteButton?.setOnClickListener {
                deleteTask(it, currentUID!!)
                moreOptionsTaskWindow!!.dismiss()
            }

            moreOptionsTaskWindow!!.setOnDismissListener(PopupWindow.OnDismissListener {
                modifyTaskWindow!!.dismiss()
            })

        }

        fun duplicateTask(it: View, UID: Int) {
            GlobalScope.launch {
                sharedDbLock.acquire()
                dbClass.duplicateDBEntry(todoDb, UID)
                refreshList()
                sharedDbLock.release()
                todoList?.post {
                    // TODO: Couldn't figure out why this is not working yet
                    todoList?.scrollToPosition(adapterPosition)
                }
            }

        }

        fun deleteTask(view: View?, UID: Int) {
            GlobalScope.launch {
                sharedDbLock.acquire()
                dbClass.deleteDBSingleEntry(todoDb, UID)
                refreshList()
                sharedDbLock.release()
                todoList?.post {
                    // TODO: Couldn't figure out why this is not working yet
                    todoList?.scrollToPosition(adapterPosition)
                }
            }
        }

        fun clickOnDateTimeField(view: View){
            val calendar = Calendar.getInstance()
            val dateSetListener =
                    DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
                        calendar[Calendar.YEAR] = year
                        calendar[Calendar.MONTH] = month
                        calendar[Calendar.DAY_OF_MONTH] = dayOfMonth
                        val timeSetListener =
                                TimePickerDialog.OnTimeSetListener { view1, hourOfDay, minute ->
                                    calendar[Calendar.HOUR_OF_DAY] = hourOfDay
                                    calendar[Calendar.MINUTE] = minute
                                    val simpleDateFormat = SimpleDateFormat("dd.MM.yy HH:mm")
                                    taskTimeMillis = calendar.timeInMillis
                                    val editTextDateTime = modifyTaskView?.findViewById<EditText>(R.id.edittext_modify_datetime)
                                    editTextDateTime?.text = Editable.Factory.getInstance().newEditable(simpleDateFormat.format(calendar.time))
                                }
                        TimePickerDialog(
                                view.context,
                                R.style.DatePickerTheme,
                                timeSetListener,
                                calendar[Calendar.HOUR_OF_DAY],
                                calendar[Calendar.MINUTE],
                                true
                        ).show()
                    }

            DatePickerDialog(
                    view.context,
                    R.style.DatePickerTheme,
                    dateSetListener,
                    calendar[Calendar.YEAR],
                    calendar[Calendar.MONTH],
                    calendar[Calendar.DAY_OF_MONTH]
            ).show()
        }

        fun updateModifyActivity(view: View, editTextName: EditText?, textViewUID: TextView) {
            val title = editTextName?.text.toString()
            val date = taskTimeMillis
            val uid = textViewUID.text.toString().toInt()
            val reminder = 0 // TODO: Change


            GlobalScope.launch {
                sharedDbLock.acquire()
                dbClass.updateEntry(todoDb,
                        uid,
                        title,
                        date,
                        reminder.toLong())

                refreshList()
                todoList?.post {
                    // TODO: Couldn't figure out why this is not working yet
                    todoList?.scrollToPosition(adapterPosition)
                }
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
            view.item_uid.text = todo.uid.toString()
            if(todo.date!= null && todo.date != 0.toLong()) {
                view.item_date.text = DateFormat.format("dd.MM.yyyy - HH:mm", Date(todo.date)).toString()
            }
        }
    }
}
