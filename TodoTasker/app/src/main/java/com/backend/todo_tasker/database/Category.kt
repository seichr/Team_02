package com.backend.todo_tasker.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

// Database Category Structure:
//   - UID
//   - Name
//   - Color
@Entity
data class Category(
        @PrimaryKey val uid: Int,
        @ColumnInfo(name = "name") var name: String?,
        @ColumnInfo(name = "color") var color: Int?,
        @ColumnInfo(name = "parentCategory") var parentCategory: Int?
)