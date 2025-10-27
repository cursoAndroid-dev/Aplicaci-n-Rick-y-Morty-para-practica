package com.example.aplicaci_n_rick_y_morty_para_practica.data.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface LogDao {

    // Insertar un nuevo registro de consulta
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLog(log: LogEntity)

    // Obtener todos los registros, ordenados por hora descendente (el más reciente primero)
    @Query("SELECT * FROM character_log ORDER BY queryTime DESC")
    fun getAllLogs(): Flow<List<LogEntity>>

    // Eliminar un registro específico
    @Delete
    suspend fun deleteLog(log: LogEntity)

    // Opcional: Eliminar todos los registros
    @Query("DELETE FROM character_log")
    suspend fun deleteAllLogs()
}