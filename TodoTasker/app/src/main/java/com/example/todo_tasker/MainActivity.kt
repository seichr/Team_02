package com.example.todo_tasker


import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.room.*
import java.util.*
import android.content.Intent
import android.widget.Button

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val button = findViewById<Button>(R.id.button_switch_to_list)
        button.setOnClickListener {
            val intent = Intent(this, TaskListActivity::class.java)
            startActivity(intent)
        }

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
        @ColumnInfo(name = "date") val date: Long?,
        @ColumnInfo(name = "reminder") val reminder: Long?
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


