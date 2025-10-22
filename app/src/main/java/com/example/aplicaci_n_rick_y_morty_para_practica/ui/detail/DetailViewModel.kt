package com.example.aplicaci_n_rick_y_morty_para_practica.ui.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.aplicaci_n_rick_y_morty_para_practica.data.Character
import com.example.aplicaci_n_rick_y_morty_para_practica.data.RickApiServices
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val rickApiService: RickApiServices,
    // SavedStateHandle se usa para recibir argumentos, como el ID
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _character = MutableStateFlow<Character?>(null)
    val character: StateFlow<Character?> = _character

    // Variable para almacenar el ID del personaje
    private var characterId: Int = 0

    init {
        // 1. Obtener el ID del Intent/Bundle usando SavedStateHandle
        characterId = savedStateHandle.get<Int>("ID") ?: 0

        // 2. Si se encontró un ID válido, cargar el personaje
        if (characterId != 0) {
            loadCharacter(characterId)
        }
    }

    private fun loadCharacter(id: Int) {
        viewModelScope.launch {
            try {
                val selectedCharacter = rickApiService.getCharacterById(id)
                _character.value = selectedCharacter
            } catch (e: Exception) {
                // Manejo de errores
                _character.value = null
            }
        }
    }
}