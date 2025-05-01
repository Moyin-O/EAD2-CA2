package com.example.fightbase

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {
    private val _fighters = MutableStateFlow<List<FighterResponse>>(emptyList())
    val fighters: StateFlow<List<FighterResponse>> = _fighters

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    fun loadFighters() {
        _isLoading.value = true
        _error.value = null
        viewModelScope.launch {
            try {
                _fighters.value = ApiClient.fighterService.getFighters()
            } catch (e: Exception) {
                _error.value = e.message
            } finally {
                _isLoading.value = false
            }
        }
    }
}
