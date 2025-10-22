package com.example.aplicaci_n_rick_y_morty_para_practica.ui

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.aplicaci_n_rick_y_morty_para_practica.data.RickApiServices
import com.example.aplicaci_n_rick_y_morty_para_practica.data.Character
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val rickApiService: RickApiServices) : ViewModel() {

    private val _characters = MutableStateFlow<List<Character>>(emptyList())
    val characters: StateFlow<List<Character>> = _characters

    // --- ESTADO INTERNO DE PAGINACIÓN ---
    private var currentSearchTerm: String? = null
    private var currentPageNumber: Int = 1 // Rastrea la página que estamos mostrando actualmente
    private var maxPages: Int = 1         // Total de páginas devueltas por la API (desde RickResponse.info.pages)

    // --- ESTADO EXPUESTO PARA LOS BOTONES DE LA UI ---
    // Indica si el botón "Siguiente" debe estar habilitado
    private val _canGoNext = MutableStateFlow(false)
    val canGoNext: StateFlow<Boolean> = _canGoNext

    // Indica si el botón "Anterior" debe estar habilitado
    private val _canGoPrev = MutableStateFlow(false)
    val canGoPrev: StateFlow<Boolean> = _canGoPrev
    // ------------------------------------------------

    init {
        // Carga la primera página al inicializar.
        loadPage(1)
    }

    // ===================================================
    // ACCIONES PÚBLICAS DE NAVEGACIÓN (Para llamar desde los botones de la Activity)
    // ===================================================

    fun loadNextPage() {
        if (currentPageNumber < maxPages) {
            val nextPage = currentPageNumber + 1
            loadPage(nextPage)
        } else {
            Log.d("VM", "Ya estás en la última página ($maxPages).")
        }
    }

    fun loadPrevPage() {
        if (currentPageNumber > 1) {
            val prevPage = currentPageNumber - 1
            loadPage(prevPage)
        } else {
            Log.d("VM", "Ya estás en la primera página.")
        }
    }

    // ===================================================
    // LÓGICA DE CARGA CENTRAL
    // ===================================================

    /**
     * Carga una página específica de la API.
     */
    private fun loadPage(page: Int) {
        // Aseguramos que la página a cargar está dentro del rango conocido (o es la primera carga)
        if (page < 1 || page > maxPages && maxPages > 1) {
            Log.w("VM", "Intento de cargar página fuera de rango: $page. Max páginas: $maxPages")
            return
        }

        // Asignamos la página ANTES de la llamada por si falla.
        currentPageNumber = page

        viewModelScope.launch {
            try {
                // Hacemos la llamada a la API con la página requerida y el término de búsqueda actual
                val response = rickApiService.getCharacters(page = page, name = currentSearchTerm)

                // 1. Actualiza el StateFlow (los datos se pintarán en el RecyclerView)
                _characters.value = response.results

                // 2. Actualiza el total de páginas (MaxPages)
                maxPages = response.info.pages

                // 3. Actualiza el estado de los botones
                updateNavigationState()

                Log.i("PRUEBA", "Página $currentPageNumber de $maxPages cargada con éxito. Personajes: ${response.results.size}")

            } catch (e: Exception) {
                Log.e("PRUEBA_ERROR", "Error al cargar la página $page", e)
                _characters.value = emptyList() // Vaciamos la lista en caso de error

                // Si la primera carga falla, aseguramos un estado seguro
                if (page == 1) maxPages = 1
                updateNavigationState()
            }
        }
    }

    // ===================================================
    // LÓGICA DE ESTADO
    // ===================================================

    /**
     * Actualiza los StateFlows que controlan la visibilidad/habilitación de los botones.
     */
    private fun updateNavigationState() {
        // Habilitar Anterior si no estamos en la primera página
        _canGoPrev.value = currentPageNumber > 1

        // Habilitar Siguiente si no estamos en la última página
        _canGoNext.value = currentPageNumber < maxPages
    }
}