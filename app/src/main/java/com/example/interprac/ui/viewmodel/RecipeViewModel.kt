package com.example.interprac.ui.viewmodel

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.interprac.data.local.entity.RecipeEntity
import com.example.interprac.data.repository.RecipeRepository
import com.example.interprac.data.uiState.UiState
import com.example.interprac.notifications.NotificationsHelper
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class RecipeViewModel(
    private val recipeRepository: RecipeRepository,
    private val context: Context
) : ViewModel() {

    private val _recipesState = MutableStateFlow<UiState<List<RecipeEntity>>>(UiState.Idle)
    val recipesState: StateFlow<UiState<List<RecipeEntity>>> = _recipesState.asStateFlow()

    private val _recipeDetailState = MutableStateFlow<UiState<RecipeEntity>>(UiState.Idle)
    val recipeDetailState: StateFlow<UiState<RecipeEntity>> = _recipeDetailState.asStateFlow()

    private val _saveState = MutableStateFlow<UiState<Unit>>(UiState.Idle)
    val saveState: StateFlow<UiState<Unit>> = _saveState.asStateFlow()

    var showOnlyMyRecipes by mutableStateOf(false)
        private set

    var searchQuery by mutableStateOf("")
        private set

    var currentUserId: String? = null

    fun updateShowOnlyMyRecipes(value: Boolean) {
        showOnlyMyRecipes = value
        loadRecipes()
    }

    fun updateSearchQuery(query: String) {
        searchQuery = query
        loadRecipes()
    }

    fun loadRecipes() {
        viewModelScope.launch {
            _recipesState.value = UiState.Loading

            try {
                val flow = when {
                    searchQuery.isNotBlank() -> recipeRepository.searchRecipes(searchQuery)
                    showOnlyMyRecipes && currentUserId != null -> recipeRepository.getRecipesByUser(currentUserId!!)
                    else -> recipeRepository.getAllRecipes()
                }

                flow.collect { recipes ->
                    _recipesState.value = UiState.Success(recipes)
                }
            } catch (e: Exception) {
                _recipesState.value = UiState.Error(e.message ?: "Error al cargar recetas")
            }
        }
    }

    fun loadRecipeById(id: Int) {
        viewModelScope.launch {
            _recipeDetailState.value = UiState.Loading

            try {
                val recipe = recipeRepository.getRecipeById(id)
                if (recipe != null) {
                    _recipeDetailState.value = UiState.Success(recipe)
                } else {
                    _recipeDetailState.value = UiState.Error("Receta no encontrada")
                }
            } catch (e: Exception) {
                _recipeDetailState.value = UiState.Error(e.message ?: "Error al cargar receta")
            }
        }
    }

    fun createRecipe(
        title: String,
        description: String,
        ingredients: String,
        imageUri: String?,
        cuisineType: String,
        prepTimeMinutes: Int,
        difficulty: Int,
        chef: String,
        userId: String
    ) {
        viewModelScope.launch {
            _saveState.value = UiState.Loading

            try {
                val recipe = RecipeEntity(
                    title = title,
                    description = description,
                    ingredients = ingredients,
                    imageUri = imageUri,
                    cuisineType = cuisineType,
                    prepTimeMinutes = prepTimeMinutes,
                    difficulty = difficulty,
                    chef = chef,
                    userId = userId
                )
                recipeRepository.insertRecipe(recipe)
                _saveState.value = UiState.Success(Unit)

                NotificationsHelper.sendRecipeNotification(
                    context = context,
                    title = "Receta creada",
                    message = "Se ha creado la receta: $title"
                )

                loadRecipes()
            } catch (e: Exception) {
                _saveState.value = UiState.Error(e.message ?: "Error al crear receta")
            }
        }
    }

    fun updateRecipe(recipe: RecipeEntity) {
        viewModelScope.launch {
            _saveState.value = UiState.Loading

            try {
                recipeRepository.updateRecipe(recipe)
                _saveState.value = UiState.Success(Unit)

                NotificationsHelper.sendRecipeNotification(
                    context = context,
                    title = "Receta actualizada",
                    message = "Se ha actualizado la receta: ${recipe.title}"
                )

                loadRecipes()
            } catch (e: Exception) {
                _saveState.value = UiState.Error(e.message ?: "Error al actualizar receta")
            }
        }
    }

    fun deleteRecipe(recipe: RecipeEntity) {
        viewModelScope.launch {
            _saveState.value = UiState.Loading

            try {
                recipeRepository.deleteRecipe(recipe)
                _saveState.value = UiState.Success(Unit)

                NotificationsHelper.sendRecipeNotification(
                    context = context,
                    title = "Receta eliminada",
                    message = "Se ha eliminado la receta: ${recipe.title}"
                )

                loadRecipes()
            } catch (e: Exception) {
                _saveState.value = UiState.Error(e.message ?: "Error al eliminar receta")
            }
        }
    }

    fun resetSaveState() {
        _saveState.value = UiState.Idle
    }

    fun resetDetailState() {
        _recipeDetailState.value = UiState.Idle
    }
}

