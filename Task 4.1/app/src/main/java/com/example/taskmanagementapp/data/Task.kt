package com.example.taskmanagementapp.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tasks")
data class Task(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,  // auto-generated ID
    val title: String,
    val description: String,
    val dueDate: String,
    var isDone: Boolean = false
)
