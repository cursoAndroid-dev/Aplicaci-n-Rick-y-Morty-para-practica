package com.example.aplicaci_n_rick_y_morty_para_practica.data

import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface RickApiServices {

    @GET("character")
    suspend fun getCharacters(
        @Query("page") page: Int? = null,
        @Query("name") name: String? = null
    ): RickResponse

    // NUEVO: Método para obtener un personaje por su ID
    @GET("character/{id}")
    suspend fun getCharacterById(
        @Path("id") id: Int // El ID se inyecta en la URL
    ): Character // Retorna directamente el objeto Character
}