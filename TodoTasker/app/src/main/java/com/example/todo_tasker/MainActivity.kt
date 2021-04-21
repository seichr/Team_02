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
import java.util.concurrent.Semaphore

lateinit var db: database_class
lateinit var datab: TodoDatabase

class MainActivity : AppCompatActivity() {

    lateinit var todo_class: database_class
    lateinit var todo_database: TodoDatabase
    private val shared_db_lock = Semaphore(1)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val button = findViewById<Button>(R.id.button_switch_to_list)
        button.setOnClickListener {
            val intent = Intent(this, TodoListActivity::class.java)
            startActivity(intent)
        }

        db = database_class(applicationContext)
        datab = db.createDb()


        todo_class = database_class(applicationContext)
        todo_database = todo_class.createDb()


        val addButton: Button = findViewById<Button>(R.id.addButton)

        addButton.setOnClickListener {
            val textField: EditText = findViewById<EditText>(R.id.editTextTextPersonName)

            val title = textField.text.toString()
            // TODO set this to spinner.
            val date = 0
            val reminder = 0

            GlobalScope.launch {
                shared_db_lock.acquire()
                //TODO: Should be used later when getLastEntry() works
                /*
                if (todo_class.getLastEntry(todo_database) == null)
                {
                    todo_class.addToDb(todo_database, Todo(0, title, date.toLong(), reminder.toLong()))
                }
                else
                {
                    todo_class.addToDb(todo_database, Todo(todo_class.getLastEntry(todo_database).uid + 1, title, date.toLong(), reminder.toLong()))
                }
                 */
                todo_class.addToDb(todo_database, Todo(0, title, date.toLong(), reminder.toLong()))
                shared_db_lock.release()
            }
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

    fun getLastEntry(db: TodoDatabase): Todo {
        //println("Returning Last Compass Entry")
        return db.todoDao().getLastEntry()
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

    @Query("SELECT * FROM todo ORDER BY uid DESC LIMIT 1")
    fun getLastEntry(): Todo

}

@Database(entities = arrayOf(Todo::class), version = 1)
abstract class TodoDatabase : RoomDatabase() {
    abstract fun todoDao(): TodoDao
}




