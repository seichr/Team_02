package com.backend.todo_tasker.db_operations

import com.backend.todo_tasker.*
import com.backend.todo_tasker.database.Todo
import com.backend.todo_tasker.tasklist_view.RecyclerAdapter
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


class DbOperations {

    companion object {
        private var instance: DbOperations? = null
    }

    fun getInstance(): DbOperations {
        if(instance == null) {
            instance = DbOperations()
        }
        return instance as DbOperations
    }

    fun refreshListView() {
        GlobalScope.launch {
            val data = dbClass.getAllDb(todoDb)
            todoList?.post {
                RecyclerAdapter().getInstance().setData(data)
                adapter = RecyclerAdapter().getInstance()
                todoList?.adapter = adapter
            }
        }
    }

    fun addOperation(title: String, date: Long) {
        GlobalScope.launch {
            sharedDbLock.acquire()
            if (dbClass.getLastEntry(todoDb) == null) { // This is not always false...
                dbClass.addToDb(todoDb, Todo(0,
                        title,
                        date))
            } else {
                dbClass.addToDb(todoDb, Todo(dbClass.getLastEntry(todoDb).uid + 1,
                        title,
                        date))
            }
            refreshListView()
            todoList?.post {
                todoList!!.adapter?.itemCount?.let { todoList?.layoutManager?.scrollToPosition(it - 1) }
            }
            sharedDbLock.release()
        }
    }

    fun duplicateOperation(UID: Int, adapterPosition: Int) {
        GlobalScope.launch {
            sharedDbLock.acquire()
            dbClass.duplicateDBEntry(todoDb, UID)
            refreshListView()
            sharedDbLock.release()
            todoList?.post {
                todoList?.scrollToPosition(adapterPosition)
            }
        }

    }

    fun deleteOperation(UID: Int, adapterPosition: Int) {
        GlobalScope.launch {
            sharedDbLock.acquire()
            dbClass.deleteDBSingleEntry(todoDb, UID)
            refreshListView()
            sharedDbLock.release()
            todoList?.post {
                todoList?.scrollToPosition(adapterPosition)
            }
        }
    }

    fun updateOperation(uid: Int, title: String, date: Long, adapterPosition: Int) {
        GlobalScope.launch {
            sharedDbLock.acquire()
            dbClass.updateEntry(todoDb,
                    uid,
                    title,
                    date)
            refreshListView()
            sharedDbLock.release()
            todoList?.post {
                todoList?.scrollToPosition(adapterPosition)
            }
        }
    }
}