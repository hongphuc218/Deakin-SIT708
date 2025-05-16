package com.example.lostfoundapp

import AppDatabase
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.launch

class ItemListActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var db: AppDatabase
    private lateinit var backButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContentView(R.layout.item_list_activity)

        recyclerView = findViewById(R.id.itemRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        backButton = findViewById(R.id.back)
        backButton.setOnClickListener {
            finish()
        }

        db = AppDatabase.get(this)
    }

    override fun onResume() {
        super.onResume()
        loadAdverts()
    }

    private fun loadAdverts() {
        lifecycleScope.launch {
            val adverts = db.advertDao().getAll()
            recyclerView.adapter = ItemAdapter(adverts) { advert ->
                val intent = Intent(this@ItemListActivity, AdvertDetailActivity::class.java)
                intent.putExtra("advertId", advert.id)
                startActivity(intent)
            }
        }
    }
}
