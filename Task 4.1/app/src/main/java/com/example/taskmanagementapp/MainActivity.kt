package com.example.taskmanagementapp

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.taskmanagementapp.data.Task
import com.example.taskmanagementapp.data.TaskDatabase
import com.google.android.material.card.MaterialCardView
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var addTaskButton: Button
    private lateinit var cardContainer: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContentView(R.layout.activity_main)

        // Initialize views
        addTaskButton = findViewById(R.id.addTask)
        cardContainer = findViewById(R.id.cardContainer)

        // Set the onClickListener for Add Task button
        addTaskButton.setOnClickListener {
            // Launch AddTaskActivity when the Add Task button is clicked
            val intent = Intent(this, AddTaskActivity::class.java)
            startActivity(intent)
        }

        // Display the tasks from the database
        displayTasks()
    }

    override fun onResume() {
        super.onResume()
        // Refresh tasks when returning to the main page
        displayTasks()
    }

    private fun displayTasks() {
        val taskDatabase = TaskDatabase.getDatabase(applicationContext)

        lifecycleScope.launch {
            // Fetch tasks from the database
            val tasks = taskDatabase.taskDao().getAllTasksSortedByDate()

            // Clear any existing cards
            cardContainer.removeAllViews()

            // Get today's date for comparison
            val currentDate = Calendar.getInstance().time
            val dateFormatter = SimpleDateFormat("dd/mm/yyyy", Locale.getDefault())

            // Loop through the tasks and add each one as a card
            for (task in tasks) {
                // Inflate a new MaterialCardView for each task
                val cardView = layoutInflater.inflate(R.layout.card_item, cardContainer, false) as MaterialCardView

                // Find the views in the card
                val titleTextView: TextView = cardView.findViewById(R.id.cardTitle)
                val descriptionTextView: TextView = cardView.findViewById(R.id.cardDescription)
                val dateTextView: TextView = cardView.findViewById(R.id.cardDate)
                val checkBox: CheckBox = cardView.findViewById(R.id.cardCheckBox)

                // Set the values dynamically for each task
                titleTextView.text = task.title
                descriptionTextView.text = task.description
                dateTextView.text = "Due date: ${task.dueDate}"
                checkBox.isChecked = task.isDone


                // Set onClickListener for the card
                cardView.setOnClickListener {
                    val intent = Intent(this@MainActivity, EditTaskActivity::class.java).apply {
                        putExtra("taskId", task.id)
                        putExtra("taskTitle", task.title)
                        putExtra("taskDescription", task.description)
                        putExtra("taskDate", task.dueDate)
                        putExtra("taskIsDone", task.isDone) // Pass the checkbox state as well
                    }
                    startActivity(intent)
                }

                // Set up the checkbox to save its state to the database when changed
                checkBox.setOnCheckedChangeListener { _, isChecked ->
                    // Update the task's isDone state
                    task.isDone = isChecked
                    updateTaskInDatabase(task) // Save the updated task state in the database
                }

                // Add the card to the parent container
                cardContainer.addView(cardView)
            }
        }
    }
    private fun updateTaskInDatabase(task: Task) {
        val taskDatabase = TaskDatabase.getDatabase(applicationContext)
        lifecycleScope.launch {
            taskDatabase.taskDao().update(task)
        }
    }
}
