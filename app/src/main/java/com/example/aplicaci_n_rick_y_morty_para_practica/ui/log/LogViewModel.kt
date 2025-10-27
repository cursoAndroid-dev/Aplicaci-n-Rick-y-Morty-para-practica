package com.example.aplicaci_n_rick_y_morty_para_practica.ui.log

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.aplicaci_n_rick_y_morty_para_practica.data.db.LogDao
import com.example.aplicaci_n_rick_y_morty_para_practica.data.db.LogEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LogViewModel @Inject constructor(
    private val logDao: LogDao
) : ViewModel() {

    // Exponer la lista de registros como StateFlow
    val logList: StateFlow<List<LogEntity>> = logDao.getAllLogs()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun deleteLog(log: LogEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            logDao.deleteLog(log)
        }
    }
}