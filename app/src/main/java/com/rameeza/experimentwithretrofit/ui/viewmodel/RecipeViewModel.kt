package com.rameeza.experimentwithretrofit.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rameeza.experimentwithretrofit.data.model.Recipe
import com.rameeza.experimentwithretrofit.data.remote.ApiService
import com.rameeza.experimentwithretrofit.data.remote.RetrofitClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed class UiState {
    object Idle : UiState()
    object Loading : UiState()
    data class Success(val recipes: List<Recipe>) : UiState()
    data class Error(val message: String) : UiState()
    object Empty : UiState()
}

class RecipeViewModel(
    private val apiService: ApiService = RetrofitClient.apiService
) : ViewModel() {

    private val _uiState = MutableStateFlow<UiState>(UiState.Idle)
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    fun searchRecipes(query: String) {
        if (query.isBlank()) return
        
        viewModelScope.launch {
            _uiState.value = UiState.Loading
            try {
                val response = apiService.searchRecipes(query)
                if (response.isSuccessful) {
                    val meals = response.body()?.meals
                    if (meals.isNullOrEmpty()) {
                        _uiState.value = UiState.Empty
                    } else {
                        _uiState.value = UiState.Success(meals)
                    }
                } else {
                    _uiState.value = UiState.Error("Error: ${response.code()} ${response.message()}")
                }
            } catch (e: Exception) {
                _uiState.value = UiState.Error("Exception: ${e.localizedMessage}")
            }
        }
    }

    fun fetchRandomRecipe() {
        viewModelScope.launch {
            _uiState.value = UiState.Loading
            try {
                val response = apiService.getRandomRecipe()
                if (response.isSuccessful) {
                    val meals = response.body()?.meals
                    if (meals.isNullOrEmpty()) {
                        _uiState.value = UiState.Empty
                    } else {
                        _uiState.value = UiState.Success(meals)
                    }
                } else {
                    _uiState.value = UiState.Error("Error: ${response.code()}")
                }
            } catch (e: Exception) {
                _uiState.value = UiState.Error("Exception: ${e.localizedMessage}")
            }
        }
    }
}
