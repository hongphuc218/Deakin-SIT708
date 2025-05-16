package com.example.lostfoundapp

import Advert
import android.app.DatePickerDialog
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.room.Room
import com.google.android.material.textfield.TextInputLayout
import kotlinx.coroutines.launch
import java.util.Calendar

class NewAdvertActivity : AppCompatActivity() {

    private lateinit var saveTask: Button
    private lateinit var cancelTask: Button
    private lateinit var locationSpinner: Spinner
    private lateinit var postTypeGroup: RadioGroup
    private lateinit var datePickerButton: Button // Use the button for date picker

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContentView(R.layout.new_advert_activity)

        // View bindings
        saveTask = findViewById(R.id.saveTask)
        cancelTask = findViewById(R.id.cancelTask)
        datePickerButton = findViewById(R.id.datePickerButton) // Reference to the date picker button
        locationSpinner = findViewById(R.id.locationSpinner)
        postTypeGroup = findViewById(R.id.postTypeGroup)

        setupLocationSpinner()
        setupDatePicker()
        setupSaveButton()
        setupCancelButton()
    }

    private fun setupLocationSpinner() {
        val adapter = ArrayAdapter.createFromResource(
            this,
            R.array.location_array,
            android.R.layout.simple_spinner_item
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        locationSpinner.adapter = adapter
    }

    private fun setupSaveButton() {
        saveTask.setOnClickListener {
            val selectedPostTypeId = postTypeGroup.checkedRadioButtonId
            if (selectedPostTypeId == -1) {
                Toast.makeText(this, "Please select Post Type: Lost or Found", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val selectedLocation = locationSpinner.selectedItem.toString()
            if (selectedLocation == "Select location") {
                Toast.makeText(this, "Please select a valid location.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val selectedPostType = findViewById<RadioButton>(selectedPostTypeId).text.toString()

            val name = findViewById<TextInputLayout>(R.id.nameInput).editText?.text.toString()
            val phone = findViewById<TextInputLayout>(R.id.phoneInput).editText?.text.toString()
            val description = findViewById<TextInputLayout>(R.id.descriptionInput).editText?.text.toString()
            val date = findViewById<Button>(R.id.datePickerButton).text.toString()

            val advert = Advert(
                postType = selectedPostType,
                phone = phone,
                name = name,
                description = description,
                location = selectedLocation,
                date = date
            )

            val db = AppDatabase.get(this)

            lifecycleScope.launch {
                db.advertDao().insert(advert)
                Toast.makeText(this@NewAdvertActivity, "Advert saved!", Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }

    private fun setupDatePicker() {
        val calendar = Calendar.getInstance()

        val dateSetListener = DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
            datePickerButton.text = "$dayOfMonth/${month + 1}/$year"
        }

        datePickerButton.setOnClickListener {
            DatePickerDialog(
                this@NewAdvertActivity,
                dateSetListener,
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            ).show()
        }
    }



    private fun setupCancelButton() {
        cancelTask.setOnClickListener {
            finish()
        }
    }
}
