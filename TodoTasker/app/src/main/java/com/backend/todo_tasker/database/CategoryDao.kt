package com.backend.todo_tasker.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

// Database Category Operations
@Dao
interface CategoryDao {
    @Insert
    fun insertAll(vararg categories: Category)

    @Insert
    fun insert(vararg category: Category)

    @Query("SELECT * FROM category")
    fun getAll(): List<Category>

    @Query("DELETE FROM category")
    fun deleteAll()

    @Query("SELECT * FROM category ORDER BY uid DESC LIMIT 1")
    fun getLastEntry(): Category

    @Query("DELETE FROM category WHERE uid = :uid")
    fun deleteSingle(uid: Int)

    @Query("Select * FROM category WHERE uid = :uid")
    fun getSingle(uid: Int) : Category

    @Query("Update category set name = :name, color = :color, parentCategory = :parentCategory where uid = :uid")
    fun update(uid: Int, name:String, color:Int, parentCategory:Int)
}