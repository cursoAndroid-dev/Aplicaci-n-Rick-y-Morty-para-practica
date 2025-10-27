package com.example.aplicaci_n_rick_y_morty_para_practica.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "character_log")
data class LogEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val characterId: Int, // ID del personaje consultado
    val characterName: String, // Nombre del personaje
    val queryTime: Long // La hora de la consulta (timestamp Long)
)