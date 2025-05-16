package com.example.lostfoundapp.data

import Advert
import androidx.room.*

@Dao
interface AdvertDao {
    @Insert suspend fun insert(advert: Advert)
    @Query("SELECT * FROM adverts") suspend fun getAll(): List<Advert>

    @Query("SELECT * FROM adverts WHERE id = :id")
    suspend fun getById(id: Int): Advert?

    @Delete
    suspend fun delete(advert: Advert)
}

