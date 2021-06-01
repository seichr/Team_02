package com.backend.todo_tasker.database

import android.content.Context
import androidx.room.Room

class DatabaseCategoryClass(context: Context) {
    private var appContext = context

    fun createDb(): CategoryDatabase {
        return Room.databaseBuilder(
                appContext,
                CategoryDatabase::class.java, "category-database"
        ).build()
    }

    fun addToDb(db: CategoryDatabase, test_category: Category): Int {
        val categories: List<Category> = getAllDb(db)

        if(test_category.name.equals(null)) {
            test_category.name = "Inbox"
        }

        if(test_category.color == null && test_category.parentCategory == null) {
            test_category.color = 0x4DB4D7
        }

        val uids: MutableList<Int> = arrayListOf()
        for (category in categories) {
            uids.add(category.uid)
        }

        if (uids.contains(test_category.uid)) {
            return -1
        }

        db.categoryDao().insert(test_category)
        return 0
    }

    fun getAllDb(db: CategoryDatabase): List<Category> {
        return db.categoryDao().getAll()
    }

    fun deleteDBEntries(db: CategoryDatabase) {
        val categoryDao = db.categoryDao()
        categoryDao.deleteAll()
    }

    fun getLastEntry(db: CategoryDatabase): Category {
        return db.categoryDao().getLastEntry()
    }

    fun deleteDBSingleEntry(db: CategoryDatabase, uid: Int) {
        db.categoryDao().deleteSingle(uid)
    }

    fun duplicateDBEntry(db: CategoryDatabase, uid: Int):Int {
        val toDuplicate:Category= getSingleEntry(db, uid)
        val lastEntry: Category = getLastEntry(db)
        val nextId = lastEntry.uid + 1
        val toInsert = Category(nextId, toDuplicate.name, toDuplicate.color, toDuplicate.parentCategory)
        addToDb(db,toInsert)
        return nextId
    }

    fun updateEntry(db:CategoryDatabase, uid: Int, name:String, color:Int, parentCategory:Int){
        db.categoryDao().update(uid, name, color, parentCategory)
    }

    fun getSingleEntry(db: CategoryDatabase, uid:Int): Category {
        return db.categoryDao().getSingle(uid)
    }
}