package com.example.aplicaci_n_rick_y_morty_para_practica.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.aplicaci_n_rick_y_morty_para_practica.R
import com.example.aplicaci_n_rick_y_morty_para_practica.data.Character
import com.example.aplicaci_n_rick_y_morty_para_practica.databinding.PreviewItemBinding
import com.example.aplicaci_n_rick_y_morty_para_practica.util.FavoriteManager
import com.squareup.picasso.Picasso

class RickAdapter(private var lista: List<Character> = emptyList(),
                  private val favoriteManager: FavoriteManager,
                  private val onItemClick: (Int) -> Unit,
                  private val onFavoriteClick: (Int) -> Unit
): RecyclerView.Adapter<RickViewHolder>() {

    fun submitList(newList: List<Character>) {
        this.lista = newList
        notifyDataSetChanged()
    }
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RickViewHolder {
        val binding = PreviewItemBinding.inflate(
            LayoutInflater.from(parent.context), // Usamos el contexto del padre
            parent,
            false // No adjuntar al padre inmediatamente
        )
        return RickViewHolder(binding,favoriteManager,onFavoriteClick)
    }

    override fun onBindViewHolder(
        holder: RickViewHolder,
        position: Int
    ) {
        holder.render(lista[position],onItemClick)
    }

    override fun getItemCount(): Int {
        return lista.size
    }
}

class RickViewHolder(private val binding: PreviewItemBinding,
                     private val favoriteManager: FavoriteManager,
                     private val onFavoriteClick: (Int) -> Unit): RecyclerView.ViewHolder(binding.root){

    fun render(character: Character,onItemClick: (Int) -> Unit){

        Picasso.get().load(character.image).into(binding.ivPreviewItem)
        binding.tvPreviewItem.text = character.name

        binding.previewConstrain.setOnClickListener { onItemClick(character.id) }

        // Lógica del icono de favorito:
        // 1. Establecer el icono correcto
        val isFav = favoriteManager.isFavorite(character.id)
        binding.favoritePreviewItem.setImageResource(
            if (isFav) R.drawable.ic_favorite_selected else R.drawable.ic_favorite
        )

        // 2. Manejar el click del icono
        binding.favoritePreviewItem.setOnClickListener {
            // Esto llamará a la Activity/ViewModel para manejar el cambio en SharedPreferences
            onFavoriteClick(character.id)

            // Nota: El adapter no debe manejar la persistencia. La Activity/ViewModel
            // lo hará, y luego actualizará la lista. Por ahora, solo llama al callback.
        }
    }
}