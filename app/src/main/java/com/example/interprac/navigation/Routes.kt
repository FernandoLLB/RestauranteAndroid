package com.example.interprac.navigation

object Routes {
    // Auth
    const val LOGIN = "login"
    const val REGISTER = "register"

    // Main app
    const val RECIPES = "recipes"
    const val ADD_RECIPE = "add_recipe"
    const val EDIT_RECIPE = "edit_recipe/{recipeId}"
    const val SETTINGS = "settings"
    const val ADMIN = "admin"

    // Legacy (can be removed or kept for extra features)
    const val HOME = "home"
    const val CAMERA = "camera"
    const val MAP = "map"
    const val FILES = "files"
    const val NOTIFICATIONS = "notifications"

    fun editRecipe(recipeId: Int) = "edit_recipe/$recipeId"
}