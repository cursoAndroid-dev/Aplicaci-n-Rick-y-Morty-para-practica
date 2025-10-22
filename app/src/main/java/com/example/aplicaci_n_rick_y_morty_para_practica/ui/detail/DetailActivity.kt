package com.example.aplicaci_n_rick_y_morty_para_practica.ui.detail


import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.aplicaci_n_rick_y_morty_para_practica.R
import com.example.aplicaci_n_rick_y_morty_para_practica.databinding.ActivityDetailBinding
import dagger.hilt.android.AndroidEntryPoint
import com.example.aplicaci_n_rick_y_morty_para_practica.data.Character
import com.squareup.picasso.Picasso
import kotlinx.coroutines.launch

@AndroidEntryPoint
class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding

    private val detailViewModel: DetailViewModel by viewModels()
    private lateinit var id: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        initUI()
    }

    private fun initUI(){

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED){
                detailViewModel.character.collect { character ->
                    // El valor puede ser null si hay un error o aún está cargando
                    character?.let { renderCharacter(it) }
                }
            }
        }
    }

    private fun renderCharacter(character: Character) {
        binding.tvNameDetail.text = character.name
        binding.tvStatusDetail.text = "Status: ${character.status}"
        binding.tvSpeciesDetail.text = "Species: ${character.species}"
        binding.tvGenderDetail.text = "Gender: ${character.gender}"
        binding.tvOriginDetail.text = "Origin: ${character.origin.name}"
        binding.tvLocationDetail.text = "Location: ${character.location.name}"

        Picasso.get().load(character.image).into(binding.ivDetail)
    }
}