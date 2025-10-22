package com.example.aplicaci_n_rick_y_morty_para_practica.data

import com.google.gson.annotations.SerializedName

// 1. Clase principal de la respuesta de la API
data class RickResponse(
    @SerializedName("info") val info: Info,
    @SerializedName("results") val results: List<Character> // Lista de personajes
)

// 2. Clase para la información de paginación
data class Info(
    @SerializedName("count") val count: Int,
    @SerializedName("pages") val pages: Int,
    @SerializedName("next") val next: String?, // URL de la siguiente página (clave para la paginación)
    @SerializedName("prev") val prev: String?  // URL de la página anterior
)

// 3. Clase para los detalles del personaje
data class Character(
    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String,
    @SerializedName("status") val status: String,
    @SerializedName("species") val species: String,
    @SerializedName("gender") val gender: String,
    @SerializedName("origin") val origin: NameOrigen,
    @SerializedName("location") val location: NameLocation,
    @SerializedName("image") val image: String
)

data class NameOrigen(
    @SerializedName("name") val name: String
)

data class NameLocation(
    @SerializedName("name") val name: String
)