package com.example.aplicaci_n_rick_y_morty_para_practica.ui

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import androidx.appcompat.widget.SearchView
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.GridLayoutManager
import com.example.aplicaci_n_rick_y_morty_para_practica.R
import com.example.aplicaci_n_rick_y_morty_para_practica.databinding.ActivityMainBinding
import com.example.aplicaci_n_rick_y_morty_para_practica.ui.adapter.RickAdapter
import com.example.aplicaci_n_rick_y_morty_para_practica.ui.detail.DetailActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import kotlin.getValue
@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    private val mainViewModel: MainViewModel by viewModels()

    private lateinit var rickAdapter: RickAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        iniciarAdapter()
        observarFlow()
        iniciarNav()
    }

    private fun iniciarNav() {
        // 1. Conectar acciones a los botones
        binding.bNextMain.setOnClickListener { mainViewModel.loadNextPage() }
        binding.bPrevMain.setOnClickListener { mainViewModel.loadPrevPage() }

        // 2. Observar el estado para habilitar/deshabilitar los botones
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                // Observar el botón Siguiente
                launch {
                    mainViewModel.canGoNext.collect { canGoNext ->
                        binding.bNextMain.isEnabled = canGoNext
                    }
                }
                // Observar el botón Anterior
                launch {
                    mainViewModel.canGoPrev.collect { canGoPrev ->
                        binding.bPrevMain.isEnabled = canGoPrev
                    }
                }
            }
        }
    }

    private fun iniciarAdapter(){
        rickAdapter = RickAdapter() {pasar(it)}

        binding.rvMain.apply {
            adapter = rickAdapter
            layoutManager = GridLayoutManager(this@MainActivity, 2)
        }
    }

    private fun observarFlow(){

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED){
                mainViewModel.characters.collect {
                    rickAdapter.submitList(it)
                }
            }
        }
    }

    private fun pasar(id: Int){
        val intent = Intent(this, DetailActivity::class.java)
        intent.putExtra("ID",id)
        startActivity(intent)
    }

    // 1. Inflar el Menú y configurar el SearchView
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Carga el XML del menú
        menuInflater.inflate(R.menu.main_menu, menu)

        val searchItem = menu.findItem(R.id.action_search)
        val searchView = searchItem.actionView as SearchView

        // 2. Configurar el Listener para manejar la consulta de búsqueda
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {

            // Se llama cuando el usuario presiona Enter/Buscar
            override fun onQueryTextSubmit(query: String?): Boolean {
                val searchTerm = query?.trim()
                if (!searchTerm.isNullOrBlank()) {
                    // Llama al ViewModel para iniciar la búsqueda
                    mainViewModel.searchCharacters(searchTerm)
                } else {
                    // Si está vacío, puede que quieras volver a mostrar todos los personajes
                    mainViewModel.searchCharacters("")
                }
                searchView.clearFocus() // Ocultar el teclado tras la búsqueda
                return true // Indicamos que hemos manejado la acción
            }

            // Se llama con cada cambio de texto (útil para sugerencias, pero aquí no la usamos)
            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
        })
        return true
    }
}