package com.example.aplicaci_n_rick_y_morty_para_practica.data.db

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [LogEntity::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun logDao(): LogDao
}