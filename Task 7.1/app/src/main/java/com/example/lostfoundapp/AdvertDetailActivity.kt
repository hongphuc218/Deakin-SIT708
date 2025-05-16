package com.example.lostfoundapp

import Advert
import AppDatabase
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch

class AdvertDetailActivity : AppCompatActivity() {

    private lateinit var db: AppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContentView(R.layout.advert_detail_activity)

        db = AppDatabase.get(this)
        val advertId = intent.getIntExtra("advertId", -1)

        if (advertId == -1) {
            finish()
            return
        }

        lifecycleScope.launch {
            val advert = db.advertDao().getById(advertId)
            advert?.let {
                findViewById<TextView>(R.id.detailTitle).text = "Type: ${it.postType}"
                findViewById<TextView>(R.id.detailName).text = "Title: ${it.name}"
                findViewById<TextView>(R.id.detailPhone).text = "Phone: ${it.phone}"
                findViewById<TextView>(R.id.detailDescription).text = "Description: ${it.description}"
                findViewById<TextView>(R.id.detailLocation).text = "Location: ${it.location}"
                findViewById<TextView>(R.id.detailDate).text = "Date: ${it.date}"

                findViewById<Button>(R.id.deleteButton).setOnClickListener {
                    lifecycleScope.launch {
                        db.advertDao().delete(advert)
                        Toast.makeText(this@AdvertDetailActivity, "Deleted", Toast.LENGTH_SHORT).show()
                        finish()
                    }
                }
            }
        }
    }
}

