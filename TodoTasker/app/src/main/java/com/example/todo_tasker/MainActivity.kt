package com.example.todo_tasker

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.room.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.*
import android.content.Intent
import com.example.todo_tasker.tasklist_view.TodoListActivity
import java.util.concurrent.Semaphore

lateinit var dbClass: DatabaseClass
lateinit var todoDb: TodoDatabase
private val sharedDbLock = Semaphore(1)

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val button = findViewById<Button>(R.id.button_switch_to_list)
        button.setOnClickListener {
            val intent = Intent(this, TodoListActivity::class.java)
            startActivity(intent)
        }

        dbClass = DatabaseClass(applicationContext)
        todoDb = dbClass.createDb()

        val addButton: Button = findViewById(R.id.button_add_to_db)
        addButton.setOnClickListener {
            val textField: EditText = findViewById(R.id.edittext_name)

            val title = textField.text.toString()
            // TO-DO [For Date]: set this to spinner.
            val date = 0
            val reminder = 0

            GlobalScope.launch {
                sharedDbLock.acquire()
                if (dbClass.getLastEntry(todoDb) == null) { // This is not always false...
                    dbClass.addToDb(todoDb, Todo(0,
                                                 title,
                                                 date.toLong(),
                                                 reminder.toLong()))
                } else {
                    dbClass.addToDb(todoDb, Todo(dbClass.getLastEntry(todoDb).uid + 1,
                                                 title,
                                                 date.toLong(),
                                                 reminder.toLong()))
                }
                sharedDbLock.release()
            }
        }
    }
}

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
    fun deleteAll()

    @Query("SELECT * FROM todo ORDER BY uid DESC LIMIT 1")
    fun getLastEntry(): Todo

}

@Database(entities = [Todo::class], version = 1)
abstract class TodoDatabase : RoomDatabase() {
    abstract fun todoDao(): TodoDao
}




