package com.example.interprac.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material.icons.filled.Settings
import androidx.compose.ui.graphics.vector.ImageVector

sealed class BottomNavItem(
    val route: String,
    val icon: ImageVector,
    val label: String
) {
    object Recipes : BottomNavItem(Routes.RECIPES, Icons.Default.Restaurant, "Recetas")
    object Settings : BottomNavItem(Routes.SETTINGS, Icons.Default.Settings, "Ajustes")
    object Admin : BottomNavItem(Routes.ADMIN, Icons.Default.AdminPanelSettings, "Admin")
}

