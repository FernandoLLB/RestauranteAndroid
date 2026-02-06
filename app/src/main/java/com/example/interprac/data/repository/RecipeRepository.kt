package com.example.interprac.data.repository

import com.example.interprac.data.local.dao.RecipeDao
import com.example.interprac.data.local.entity.RecipeEntity
import kotlinx.coroutines.flow.Flow

class RecipeRepository(private val recipeDao: RecipeDao) {

    fun getAllRecipes(): Flow<List<RecipeEntity>> = recipeDao.getAllRecipes()

    fun getRecipesByUser(userId: String): Flow<List<RecipeEntity>> = recipeDao.getRecipesByUser(userId)

    fun searchRecipes(query: String): Flow<List<RecipeEntity>> = recipeDao.searchRecipes(query)

    suspend fun getRecipeById(id: Int): RecipeEntity? = recipeDao.getRecipeById(id)

    suspend fun insertRecipe(recipe: RecipeEntity): Long = recipeDao.insertRecipe(recipe)

    suspend fun updateRecipe(recipe: RecipeEntity) = recipeDao.updateRecipe(recipe)

    suspend fun deleteRecipe(recipe: RecipeEntity) = recipeDao.deleteRecipe(recipe)

    suspend fun deleteRecipeById(id: Int) = recipeDao.deleteRecipeById(id)
}

