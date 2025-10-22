package com.example.aplicaci_n_rick_y_morty_para_practica.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.aplicaci_n_rick_y_morty_para_practica.data.Character
import com.example.aplicaci_n_rick_y_morty_para_practica.databinding.PreviewItemBinding
import com.squareup.picasso.Picasso

class RickAdapter(private var lista: List<Character> = emptyList(),
                  private val onItemClick:(Int) -> Unit): RecyclerView.Adapter<RickViewHolder>() {

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
        return RickViewHolder(binding)
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

class RickViewHolder(private val binding: PreviewItemBinding): RecyclerView.ViewHolder(binding.root){

    fun render(character: Character,onItemClick: (Int) -> Unit){

        Picasso.get().load(character.image).into(binding.ivPreviewItem)
        binding.tvPreviewItem.text = character.name

        binding.previewConstrain.setOnClickListener { onItemClick(character.id) }
    }
}