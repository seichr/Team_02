package com.backend.todo_tasker.button_functions

import com.backend.todo_tasker.db_operations.DbOperations

class MoreOptionsFunctions {
    fun duplicateFunction(currentUID: Int?, adapterPosition: Int?) {
        if (adapterPosition != null) {
            DbOperations().getInstance().duplicateOperation(currentUID!!, adapterPosition)
        }
    }

    fun deleteFunction(currentUID: Int?, adapterPosition: Int?) {
        if (adapterPosition != null) {
            DbOperations().getInstance().deleteOperation(currentUID!!, adapterPosition)
        }
    }
}