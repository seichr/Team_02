package com.backend.todo_tasker.button_functions

import android.view.View
import android.widget.EditText
import android.widget.TextView
import com.backend.todo_tasker.db_operations.DbOperations
import com.backend.todo_tasker.popup_window.PopUpWindowInflater
import com.backend.todo_tasker.popup_window.WINDOWTYPE
import com.backend.todo_tasker.taskTimeMillis

class ModifyFunctions {
    fun cancelModifyActivity() {
        PopUpWindowInflater().getInstance().dismissModifyTaskWindow()
    }

    fun updateModifyActivity(editTextName: EditText?, textViewUID: TextView, adapterPosition: Int) {
        val title = editTextName?.text.toString()
        val date = taskTimeMillis
        val uid = textViewUID.text.toString().toInt()

        DbOperations().getInstance().updateOperation(uid, title, date, adapterPosition)

        cancelModifyActivity()
    }

    fun openMoreOptionsWindows(it: View, v: View, pos: Int) {
        PopUpWindowInflater().getInstance().inflateWindow(
            v,
            WINDOWTYPE.MOREOPTIONS,
            it,
            adapterPosition = pos,
            backgroundDimmed = false
        )
    }

    fun deleteCompletedTask(textViewUID: TextView, adapterPosition: Int) {
        val uid = textViewUID.text.toString().toInt()
        DbOperations().getInstance().deleteOperation(uid, adapterPosition)
        PopUpWindowInflater().getInstance().dismissModifyTaskWindow()
    }
}