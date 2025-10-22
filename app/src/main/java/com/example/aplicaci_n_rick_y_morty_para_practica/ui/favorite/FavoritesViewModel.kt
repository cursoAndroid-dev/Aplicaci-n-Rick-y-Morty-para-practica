package com.example.aplicaci_n_rick_y_morty_para_practica.ui.favorite

// ... (Importaciones necesarias)
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.aplicaci_n_rick_y_morty_para_practica.data.Character
import com.example.aplicaci_n_rick_y_morty_para_practica.data.RickApiServices
import com.example.aplicaci_n_rick_y_morty_para_practica.util.FavoriteManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class FavoritesViewModel @Inject constructor(
    private val apiService: RickApiServices,
    private val favoriteManager: FavoriteManager
) : ViewModel() {

    private val _characters = MutableStateFlow<List<Character>>(emptyList())
    val characters: StateFlow<List<Character>> = _characters

    init {
        loadFavorites()
    }

    fun loadFavorites() {
        viewModelScope.launch {
            val favoriteIds = favoriteManager.getFavorites()
                .mapNotNull { it.toIntOrNull() } // Convertir Set<String> a List<Int>

            if (favoriteIds.isEmpty()) {
                _characters.value = emptyList()
                return@launch
            }

            // La API de Rick y Morty tiene un endpoint para IDs múltiples
            // Llama a la API para obtener todos los personajes a la vez
            val idListString = favoriteIds.joinToString(",")

            try {
                // NOTA: Si el número de IDs es grande, este endpoint podría fallar o devolver un Array en lugar de un objeto.
                // Para este ejemplo, asumimos que devuelve List<Character> para IDs múltiples.
                // Si la API no lo soporta, tendrías que hacer N llamadas (un 'map' con un 'async' dentro).
                // **¡Para simplificar, vamos a forzar N llamadas si no hay endpoint de lista!**

                val loadedCharacters = withContext(Dispatchers.IO) {
                    favoriteIds.mapNotNull { id ->
                        try {
                            apiService.getCharacterById(id)
                        } catch (e: Exception) {
                            null // Ignorar si un ID falla
                        }
                    }
                }

                _characters.value = loadedCharacters

            } catch (e: Exception) {
                // Manejo de errores de red
                _characters.value = emptyList()
            }
        }
    }
}