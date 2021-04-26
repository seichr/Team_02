package com.backend.todo_tasker.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

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
