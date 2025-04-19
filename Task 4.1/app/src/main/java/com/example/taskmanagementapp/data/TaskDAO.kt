package com.example.taskmanagementapp.data

import androidx.room.*

@Dao
interface TaskDao {

    @Insert
    suspend fun insert(task: Task)

    @Update
    suspend fun update(task: Task)

    @Delete
    suspend fun delete(task: Task)

    // Modify the query to sort by due date in ascending order (nearest due date first)
    @Query("SELECT * FROM tasks ORDER BY dueDate ASC")
    suspend fun getAllTasksSortedByDate(): List<Task>

    @Query("SELECT * FROM tasks WHERE id = :taskId")
    suspend fun getTaskById(taskId: Int): Task?
}
