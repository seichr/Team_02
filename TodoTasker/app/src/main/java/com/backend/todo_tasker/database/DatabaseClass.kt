package com.backend.todo_tasker.database

import android.content.Context
import androidx.room.Room
import java.util.*

class DatabaseClass(context: Context) {
    private var appContext = context

    fun createDb(): TodoDatabase {
        return Room.databaseBuilder(
                appContext,
                TodoDatabase::class.java, "todo-database"
        ).build()
    }

    fun addToDb(db: TodoDatabase, test_todo: Todo): Int {
        val todos: List<Todo> = getAllDb(db)

        val uids: MutableList<Int> = arrayListOf()
        for (todo in todos) {
            uids.add(todo.uid)
        }

        if (uids.contains(test_todo.uid)) {
            println("UID already in DB")
            return -1
        }

        db.todoDao().insert(test_todo)
        println("Added TODO to DB")
        return 0
    }

    fun getAllDb(db: TodoDatabase): List<Todo> {
        println("Getting all DB entries")
        return db.todoDao().getAll()
    }

    fun deleteDBEntries(db: TodoDatabase) {
        val todoDao = db.todoDao()
        todoDao.deleteAll()
    }

    fun getLastEntry(db: TodoDatabase): Todo {
        return db.todoDao().getLastEntry()
    }

    fun dateToMillis(date: Date): Long {
        return date.time
    }

    fun getCurrentDate(): Date {
        return Calendar.getInstance().time
    }

    fun getNextReminder(db: TodoDatabase): Todo {
        return db.todoDao().getNextReminder();
    }
}