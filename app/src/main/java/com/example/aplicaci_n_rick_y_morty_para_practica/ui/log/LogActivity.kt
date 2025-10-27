package com.example.aplicaci_n_rick_y_morty_para_practica.ui.log

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.aplicaci_n_rick_y_morty_para_practica.R
// Asume que tienes un binding llamado ActivityLogBinding
import com.example.aplicaci_n_rick_y_morty_para_practica.databinding.ActivityLogBinding
import com.example.aplicaci_n_rick_y_morty_para_practica.data.db.LogEntity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LogActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLogBinding // Asegúrate de tener este binding
    private val logViewModel: LogViewModel by viewModels()
    private lateinit var logAdapter: LogAdapter // Asegúrate de crear este Adapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityLogBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        setupRecyclerView()
        observeLogs()
    }

    private fun setupRecyclerView() {
        // Inicializa y configura tu LogAdapter
        logAdapter = LogAdapter { logEntity -> showDeleteConfirmationDialog(logEntity) }
        binding.rvLog.layoutManager = LinearLayoutManager(this)
        binding.rvLog.adapter = logAdapter
        // Usa un LinearLayoutManager o similar
    }

    private fun observeLogs() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                logViewModel.logList.collect { logList ->
                    logAdapter.submitList(logList)
                }
            }
        }
    }

    // alert
    private fun showDeleteConfirmationDialog(log: LogEntity) {
        AlertDialog.Builder(this)
            .setTitle("Confirmar eliminación")
            .setMessage("¿Estás seguro de que quieres eliminar el registro de ${log.characterName} de las ${formatTime(log.queryTime)}?")
            .setPositiveButton("Eliminar") { dialog, _ ->
                logViewModel.deleteLog(log)
                dialog.dismiss()
            }
            .setNegativeButton("Cancelar") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

    // Función de ayuda simple para formatear la hora (necesitarás más lógica para un formato bonito)
    private fun formatTime(timestamp: Long): String {
        // En una app real usarías SimpleDateFormat o LocalDateTime
        return java.text.DateFormat.getTimeInstance(java.text.DateFormat.SHORT).format(java.util.Date(timestamp))
    }
}