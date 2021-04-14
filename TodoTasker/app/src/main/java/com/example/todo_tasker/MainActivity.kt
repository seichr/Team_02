package com.example.todo_tasker

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.room.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}

// Database To-do Structure:
//   - UID [x]
//   - Titel [x]
//   - Datum [x]
//   - [Opt] Beschreibung
//   - [Opt] Erstellungsdatum+Zeit
@Entity
data class Todo(
    @PrimaryKey val uid: Int,
    @ColumnInfo(name = "title") val title: String?,
    @ColumnInfo(name = "date") val time: Long?
    )

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
    fun deleteAll();
}

@Database(entities = arrayOf(Todo::class), version = 1)
abstract class TodoDatabase : RoomDatabase() {
    abstract fun todoDao(): TodoDao
}



