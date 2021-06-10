package com.backend.todo_tasker.db_operations

import com.backend.todo_tasker.*
import com.backend.todo_tasker.database.Todo
import com.backend.todo_tasker.tasklist_view.RecyclerAdapter
import com.backend.todo_tasker.tasklist_view.RecyclerAdapterCategory
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
            val data = dbTodoClass.getAllDb(todoDb)
            todoList?.post {
                RecyclerAdapter().getInstance().setDataToDo(data)
                adapter = RecyclerAdapter().getInstance()
                todoList?.adapter = adapter
            }
        }
    }

    fun refreshListViewProjects() {
        GlobalScope.launch {
            val data = dbCategoryClass.getAllDb(categoryDb)
            projectList?.post {
                RecyclerAdapterCategory().getInstance().setDataCategory(data)
                adapterCategory = RecyclerAdapterCategory().getInstance()
                projectList?.adapter = adapterCategory
            }
        }
    }

    fun addOperation(title: String, date: Long) {
        GlobalScope.launch {
            sharedDbLock.acquire()
            if (dbTodoClass.getLastEntry(todoDb) == null) { // This is not always false...
                dbTodoClass.addToDb(todoDb, Todo(0,
                        title,
                        date))
            } else {
                dbTodoClass.addToDb(todoDb, Todo(dbTodoClass.getLastEntry(todoDb).uid + 1,
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
            dbTodoClass.duplicateDBEntry(todoDb, UID)
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
            dbTodoClass.deleteDBSingleEntry(todoDb, UID)
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
            dbTodoClass.updateEntry(todoDb,
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