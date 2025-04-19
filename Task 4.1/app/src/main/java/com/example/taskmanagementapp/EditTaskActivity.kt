package com.example.taskmanagementapp

import android.app.DatePickerDialog
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.taskmanagementapp.data.Task
import com.example.taskmanagementapp.data.TaskDatabase
import com.google.android.material.textfield.TextInputLayout
import kotlinx.coroutines.launch
import java.util.*

class EditTaskActivity : AppCompatActivity() {

    private lateinit var taskTitle: TextInputLayout
    private lateinit var taskDescription: TextInputLayout
    private lateinit var taskDate: TextInputLayout
    private lateinit var saveTaskButton: Button
    private lateinit var deleteTaskButton: Button
    private lateinit var datePickerButton: Button // Use the button for date picker

    private var taskId: Int = 0
    private var taskDueDate: String = "" // Variable to store the task's due date

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContentView(R.layout.activity_edit_task)

        // Initialize views
        taskTitle = findViewById(R.id.taskTitle)
        taskDescription = findViewById(R.id.taskDescription)
        taskDate = findViewById(R.id.taskDate)
        datePickerButton = findViewById(R.id.datePickerButton) // Reference to the date picker button
        saveTaskButton = findViewById(R.id.saveTask)
        deleteTaskButton = findViewById(R.id.deleteTask)

        // Get the task data passed from MainActivity
        val intent = intent
        taskId = intent.getIntExtra("taskId", 0)
        val title = intent.getStringExtra("taskTitle") ?: ""
        val description = intent.getStringExtra("taskDescription") ?: ""
        taskDueDate = intent.getStringExtra("taskDate") ?: ""

        // Populate the input fields with the task data
        taskTitle.editText?.setText(title)
        taskDescription.editText?.setText(description)
        taskDate.editText?.setText(taskDueDate) // Set the task due date
        datePickerButton.text = taskDueDate

        // Set up Date Picker Dialog (same as in AddTaskActivity)
        val calendar = Calendar.getInstance()
        val dateSetListener = DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
            // Format the date as desired and set it to the button
            val selectedDate = "$dayOfMonth/${month + 1}/$year"
            datePickerButton.text = selectedDate // Update button text with selected date
            taskDate.editText?.setText(selectedDate) // Update the text field with the selected date
        }

        // Show Date Picker when clicked
        datePickerButton.setOnClickListener {
            DatePickerDialog(
                this@EditTaskActivity,
                dateSetListener,
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            ).show()
        }

        // Save the task after editing
        saveTaskButton.setOnClickListener {
            val updatedTitle = taskTitle.editText?.text.toString()
            val updatedDescription = taskDescription.editText?.text.toString()
            val date = datePickerButton.text.toString()

            if (updatedTitle.isEmpty() || updatedDescription.isEmpty() || date.isEmpty()) {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
            } else {
                val updatedTask = Task(
                    id = taskId,
                    title = updatedTitle,
                    description = updatedDescription,
                    dueDate = date,  // Ensure the due date is properly updated
                    isDone = false // This can be changed based on your logic
                )
                updateTaskInDatabase(updatedTask)
            }
        }

        // Delete the task
        deleteTaskButton.setOnClickListener {
            deleteTaskFromDatabase(taskId)
        }
    }

    // Function to update the task in the database
    private fun updateTaskInDatabase(task: Task) {
        val taskDatabase = TaskDatabase.getDatabase(applicationContext)
        lifecycleScope.launch {
            taskDatabase.taskDao().update(task)
            Toast.makeText(this@EditTaskActivity, "Task Updated!", Toast.LENGTH_SHORT).show()
            finish()  // Close the EditTaskActivity and return to MainActivity
        }
    }

    // Function to delete the task from the database
    private fun deleteTaskFromDatabase(taskId: Int) {
        val taskDatabase = TaskDatabase.getDatabase(applicationContext)
        lifecycleScope.launch {
            val task = taskDatabase.taskDao().getTaskById(taskId)
            task?.let {
                taskDatabase.taskDao().delete(it)
                Toast.makeText(this@EditTaskActivity, "Task Deleted!", Toast.LENGTH_SHORT).show()
                finish()  // Close the EditTaskActivity and return to MainActivity
            }
        }
    }
}
