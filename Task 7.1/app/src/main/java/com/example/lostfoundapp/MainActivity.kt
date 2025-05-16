package com.example.lostfoundapp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContentView(R.layout.activity_main)

        val newAdvertButton = findViewById<Button>(R.id.newAdvert)
        val showItemsButton = findViewById<Button>(R.id.showItems)

        newAdvertButton.setOnClickListener {
            val intent = Intent(this, NewAdvertActivity::class.java)
            startActivity(intent)
        }

        showItemsButton.setOnClickListener {
            val intent = Intent(this, ItemListActivity::class.java)
            startActivity(intent)
        }

    }
}



