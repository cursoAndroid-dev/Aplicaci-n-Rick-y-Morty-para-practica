package com.example.aplicaci_n_rick_y_morty_para_practica.util

import android.content.Context
import androidx.core.content.edit

class FavoriteManager(context: Context) {

    private val prefs = context.getSharedPreferences("FavoritePrefs", Context.MODE_PRIVATE)
    private val KEY_FAVORITES = "favorite_character_ids"

    // Set que almacena los IDs de favoritos (String para SharedPreferences)
    fun getFavorites(): Set<String> {
        // Devuelve el Set de IDs o un Set vacío si no hay nada guardado
        return prefs.getStringSet(KEY_FAVORITES, emptySet()) ?: emptySet()
    }

    // Comprueba si un ID está en favoritos
    fun isFavorite(id: Int): Boolean {
        return getFavorites().contains(id.toString())
    }

    // Añade o elimina un personaje de favoritos
    fun toggleFavorite(id: Int) {
        val currentFavorites = getFavorites().toMutableSet()
        val idString = id.toString()

        if (currentFavorites.contains(idString)) {
            currentFavorites.remove(idString) // Eliminar
        } else {
            currentFavorites.add(idString) // Añadir
        }

        // Guardar el Set actualizado
        prefs.edit {
            putStringSet(KEY_FAVORITES, currentFavorites)
        }
    }
}