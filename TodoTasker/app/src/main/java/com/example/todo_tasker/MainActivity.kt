package com.example.todo_tasker

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.room.*
import java.util.*
import kotlin.concurrent.thread

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val db = database_class(applicationContext)
        val datab = db.createDb()
        
        val time_in_millis = db.date_to_millis(db.get_current_date())
        val test_todo = Todo(4, "Test", time_in_millis)
        // TODO: Add Tests ( Single Add | Multi Add |  Multiple Same UIDs)

        thread { db.addToDb(datab, test_todo) }
    }
}

class database_class(context: Context) {
    var appContext = context

    fun createDb(): TodoDatabase {
        val db = Room.databaseBuilder(
            this.appContext,
            TodoDatabase::class.java, "todo-database"
        ).build()
        return db;
    }

    fun addToDb(db: TodoDatabase, test_todo: Todo): Int {
        val todoDao = db.todoDao()
        val todos: List<Todo> = todoDao.getAll()

        val uids: MutableList<Int> = arrayListOf()
        for (todo in todos) {
            uids.add(todo.uid)
        }

        if (uids.contains(test_todo.uid)) {
            println("UID already in DB")
            return -1
        }

        todoDao.insert(test_todo)
        println("Added TODO to DB")
        return 0
    }

    private fun deleteDBEntries(db: TodoDatabase) {
        val todoDao = db.todoDao()
        todoDao.deleteAll()
    }

    fun date_to_millis(date: Date): Long {
        return date.time
    }

    fun millis_to_date(millis: Long): Date {
        return Date(millis)
    }

    fun get_current_date(): Date {
        return Calendar.getInstance().time;
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



