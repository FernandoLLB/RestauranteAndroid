package com.example.interprac.ui.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.interprac.data.repository.SettingsRepository
import kotlinx.coroutines.launch

class SettingsViewModel(private val settingsRepository: SettingsRepository):
    ViewModel() {

    var darkMode by mutableStateOf(false)
        private set

    init {
        viewModelScope.launch {
            settingsRepository.observeDarkMode().collect { enable ->
                darkMode = enable
            }
        }
    }

    fun setterDarkMode(enable: Boolean) {
        viewModelScope.launch {
            settingsRepository.setDarkMode(enable)
        }
    }
}