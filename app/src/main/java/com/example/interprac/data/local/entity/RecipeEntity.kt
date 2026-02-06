package com.example.interprac.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "recipes")
data class RecipeEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val title: String,
    val description: String,
    val ingredients: String,
    val imageUri: String?,
    val cuisineType: String,
    val prepTimeMinutes: Int,
    val difficulty: Int,
    val chef: String,
    val createdAt: Long = System.currentTimeMillis(),
    val userId: String
)


