package com.example.aplicaci_n_rick_y_morty_para_practica.ui.favorite

import android.content.Intent
import android.os.Bundle
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
import com.example.aplicaci_n_rick_y_morty_para_practica.databinding.ActivityFavoriteBinding
import com.example.aplicaci_n_rick_y_morty_para_practica.ui.adapter.RickAdapter
import com.example.aplicaci_n_rick_y_morty_para_practica.ui.detail.DetailActivity
import com.example.aplicaci_n_rick_y_morty_para_practica.util.FavoriteManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class FavoriteActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFavoriteBinding
    private val favoritesViewModel: FavoritesViewModel by viewModels()
    private lateinit var rickAdapter: RickAdapter
    @Inject
    lateinit var favoriteManager: FavoriteManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityFavoriteBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        setupRecyclerView()
        observeFavorites()
    }

    override fun onResume() {
        super.onResume()
        // Cargar favoritos cada vez que la Activity vuelve a primer plano
        favoritesViewModel.loadFavorites()
    }

    private fun setupRecyclerView() {
        // Nota: Pasar el manager y el callback de favorito/detalle
        rickAdapter = RickAdapter(
            favoriteManager = favoriteManager,
            onItemClick = { id -> navigateToDetail(id) },
            onFavoriteClick = { id ->
                favoriteManager.toggleFavorite(id) // Persistencia
                favoritesViewModel.loadFavorites() // Recargar la lista tras el cambio
            }
        )
        binding.rvFavorite.apply {
            adapter = rickAdapter
            layoutManager = GridLayoutManager(this@FavoriteActivity, 2)
        }
    }

    private fun observeFavorites() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED){
                favoritesViewModel.characters.collect {
                    rickAdapter.submitList(it)
                }
            }
        }
    }

    private fun navigateToDetail(id: Int) {
        val intent = Intent(this, DetailActivity::class.java)
        intent.putExtra("ID", id)
        startActivity(intent)
    }
}