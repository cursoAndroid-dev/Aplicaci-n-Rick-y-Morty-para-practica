package com.example.aplicaci_n_rick_y_morty_para_practica.ui.log

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.aplicaci_n_rick_y_morty_para_practica.data.db.LogEntity
// Asume que has creado un binding para el ítem de registro
import com.example.aplicaci_n_rick_y_morty_para_practica.databinding.LogItemBinding

// Usamos ListAdapter para un mejor rendimiento con DiffUtil
class LogAdapter(
    // Callback para manejar la eliminación
    private val onDeleteClick: (LogEntity) -> Unit
) : ListAdapter<LogEntity, LogViewHolder>(LogDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LogViewHolder {
        val binding = LogItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return LogViewHolder(binding, onDeleteClick)
    }

    override fun onBindViewHolder(holder: LogViewHolder, position: Int) {
        holder.render(getItem(position))
    }
}

class LogViewHolder(
    private val binding: LogItemBinding,
    private val onDeleteClick: (LogEntity) -> Unit
) : RecyclerView.ViewHolder(binding.root) {

    private val timeFormatter = java.text.DateFormat.getDateTimeInstance(
        java.text.DateFormat.SHORT,
        java.text.DateFormat.SHORT
    )

    fun render(log: LogEntity) {
        // Muestra el nombre del personaje
        binding.tvCharacterNameLog.text = log.characterName

        // Formatea y muestra la hora de la consulta
        val formattedTime = timeFormatter.format(java.util.Date(log.queryTime))
        binding.tvQueryTimeLog.text = "Consultado el: $formattedTime"

        // Asigna el listener de eliminación
        binding.ivDeleteLog.setOnClickListener {
            onDeleteClick(log)
        }
    }
}

// Clase para optimizar la actualización de la lista
class LogDiffCallback : DiffUtil.ItemCallback<LogEntity>() {
    override fun areItemsTheSame(oldItem: LogEntity, newItem: LogEntity): Boolean {
        // Los ítems son el mismo si tienen el mismo ID de DB
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: LogEntity, newItem: LogEntity): Boolean {
        // Comparamos el contenido completo de la Entidad
        return oldItem == newItem
    }
}