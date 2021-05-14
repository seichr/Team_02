package com.backend.todo_tasker.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

// Database To-do Operations
@Dao
interface TodoDao {
    @Insert
    fun insertAll(vararg todos: Todo)

    @Insert
    fun insert(vararg todo: Todo)

    @Query("SELECT * FROM todo")
    fun getAll(): List<Todo>

    @Query("DELETE FROM todo")
    fun deleteAll()

    @Query("SELECT * FROM todo ORDER BY uid DESC LIMIT 1")
    fun getLastEntry(): Todo

    // the CAST((julianday))... is a SQLite way to say "Current timestamp in milliseconds".
    @Query("SELECT * FROM todo " +
            "WHERE date > CAST((julianday('now') - 2440587.5)*86400000 AS INTEGER) " +
            "ORDER BY reminder ASC LIMIT 1 ")
    fun getNextDate(): Todo

    @Query("DELETE FROM todo WHERE uid = :uid")
    fun deleteSingle(uid: Int)

    @Query("Select * FROM todo WHERE uid = :uid")
    fun getSingle(uid: Int) : Todo

    @Query("Update todo set title = :title, date = :date, reminder = :reminder where uid = :uid")
    fun update(uid: Int, title:String, date:Long ,reminder:Long)
}