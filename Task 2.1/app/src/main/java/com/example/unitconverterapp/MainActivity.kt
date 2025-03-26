package com.example.unitconverterapp

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    // UI Components
    private lateinit var categorySpinner: Spinner
    private lateinit var fromUnitSpinner: Spinner
    private lateinit var toUnitSpinner: Spinner
    private lateinit var fromInput: EditText
    private lateinit var resultTextView: TextView
    private lateinit var convertButton: Button

    // Units for each category
    private val unitCategories = mapOf(
        "length" to listOf("cm", "inch", "foot", "yard", "mile"),
        "weight" to listOf("kg", "pound", "ounce", "ton"),
        "temperature" to listOf("Celsius", "Fahrenheit", "Kelvin")
    )

    private var category: String = "length"
    private var fromUnit: String = "cm"
    private var toUnit: String = "inch"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Link UI elements
        categorySpinner = findViewById(R.id.categorySpinner)
        fromUnitSpinner = findViewById(R.id.fromUnitSpinner)
        toUnitSpinner = findViewById(R.id.toUnitSpinner)
        fromInput = findViewById(R.id.fromInput)
        resultTextView = findViewById(R.id.resultTextView)
        convertButton = findViewById(R.id.button)

        // Setup category spinner
        val categoryAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, unitCategories.keys.toList())
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        categorySpinner.adapter = categoryAdapter

        // Initialize unit spinners based on the default category
        setupUnitSpinners("length")

        // Handle category selection
        categorySpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: android.view.View?, position: Int, id: Long) {
                category = unitCategories.keys.toList()[position]
                setupUnitSpinners(category)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        // Handle the unit change (without functionality for now)
        fromUnitSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: android.view.View?, position: Int, id: Long) {
                fromUnit = unitCategories[category]!!.get(position)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        toUnitSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: android.view.View?, position: Int, id: Long) {
                toUnit = unitCategories[category]!!.get(position)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        // Convert Button
        convertButton.setOnClickListener {
            val inputValue = fromInput.text.toString()
            if (inputValue.isNotEmpty()) {
                val convertedValue = convertValue(inputValue.toDouble(), fromUnit, toUnit, category)
                resultTextView.text = "Result: $convertedValue"
            } else {
                resultTextView.text = "Please enter a value"
            }
        }
    }

    // Setup unit spinners based on the category
    private fun setupUnitSpinners(category: String) {
        val units = unitCategories[category] ?: return
        val unitAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, units)
        unitAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        fromUnitSpinner.adapter = unitAdapter
        toUnitSpinner.adapter = unitAdapter  // Populate 'toUnitSpinner' with the same units
    }

    // Conversion logic
    private fun convertValue(value: Double, from: String, to: String, category: String): Double {
        return when (category) {
            "length" -> {
                val fromFactor = when (from) {
                    "cm" -> 1.0
                    "inch" -> 2.54
                    "foot" -> 30.48
                    "yard" -> 91.44
                    else -> 160934.0
                }
                val toFactor = when (to) {
                    "cm" -> 1.0
                    "inch" -> 2.54
                    "foot" -> 30.48
                    "yard" -> 91.44
                    else -> 160934.0
                }
                value * fromFactor / toFactor
            }
            "weight" -> {
                val fromFactor = when (from) {
                    "kg" -> 1.0
                    "pound" -> 0.453592
                    "ounce" -> 0.0283495
                    else -> 907.185
                }
                val toFactor = when (to) {
                    "kg" -> 1.0
                    "pound" -> 0.453592
                    "ounce" -> 0.0283495
                    else -> 907.185
                }
                value * fromFactor / toFactor
            }
            "temperature" -> {
                when (from) {
                    "Celsius" -> {
                        when (to) {
                            "Fahrenheit" -> (value * 9/5) + 32
                            "Kelvin" -> value + 273.15
                            else -> value
                        }
                    }
                    "Fahrenheit" -> {
                        when (to) {
                            "Celsius" -> (value - 32) * 5/9
                            "Kelvin" -> (value - 32) * 5/9 + 273.15
                            else -> value
                        }
                    }
                    "Kelvin" -> {
                        when (to) {
                            "Celsius" -> value - 273.15
                            "Fahrenheit" -> (value - 273.15) * 9/5 + 32
                            else -> value
                        }
                    }
                    else -> value // If no valid conversion, return the value itself
                }
            }
            else -> value // Return value for unsupported categories
        }
    }
}
