package com.example.interprac.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.navArgument
import com.example.interprac.ui.state.AuthState
import com.example.interprac.ui.screens.*
import com.example.interprac.ui.viewmodel.AuthViewModel
import com.example.interprac.ui.viewmodel.RecipeViewModel
import com.example.interprac.ui.viewmodel.SettingsViewModel

@Composable
fun AppNavGraph(
    navController: NavHostController,
    settingsViewModel: SettingsViewModel,
    authViewModel: AuthViewModel,
    recipeViewModel: RecipeViewModel
) {
    val authState by authViewModel.authState.collectAsState()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    val isAuthenticated = authState is AuthState.Authenticated

    // Manejo de estado de autenticación
    LaunchedEffect(isAuthenticated) {
        if (isAuthenticated) {
            val current = navController.currentDestination?.route
            if (current == Routes.LOGIN || current == Routes.REGISTER || current == null) {
                navController.navigate(Routes.RECIPES) {
                    popUpTo(0) { inclusive = true }
                }
            }
        } else if (authState is AuthState.Unauthenticated) {
            navController.navigate(Routes.LOGIN) {
                popUpTo(0) { inclusive = true }
            }
        }
    }

    val showBottomNav = isAuthenticated &&
            currentRoute in listOf(Routes.RECIPES, Routes.SETTINGS, Routes.ADMIN)

    val bottomNavItems = buildList {
        add(BottomNavItem.Recipes)
        add(BottomNavItem.Settings)
        if (authViewModel.isAdmin) {
            add(BottomNavItem.Admin)
        }
    }

    Scaffold(
        bottomBar = {
            if (showBottomNav) {
                NavigationBar {
                    bottomNavItems.forEach { item ->
                        NavigationBarItem(
                            icon = { Icon(item.icon, contentDescription = item.label) },
                            label = { Text(item.label) },
                            selected = currentRoute == item.route,
                            onClick = {
                                if (currentRoute != item.route) {
                                    navController.navigate(item.route) {
                                        popUpTo(Routes.RECIPES) { saveState = true }
                                        launchSingleTop = true
                                        restoreState = true
                                    }
                                }
                            }
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Routes.LOGIN,
            modifier = Modifier.padding(innerPadding)
        ) {
            // Pantalla de Login
            composable(Routes.LOGIN) {
                LoginScreen(
                    authViewModel = authViewModel,
                    onNavigateToRegister = { navController.navigate(Routes.REGISTER) },
                    onLoginSuccess = {
                        navController.navigate(Routes.RECIPES) {
                            popUpTo(Routes.LOGIN) { inclusive = true }
                        }
                    }
                )
            }

            // Pantalla de Registro
            composable(Routes.REGISTER) {
                RegisterScreen(
                    authViewModel = authViewModel,
                    onNavigateToLogin = { navController.popBackStack() },
                    onRegisterSuccess = {
                        navController.navigate(Routes.RECIPES) {
                            popUpTo(Routes.LOGIN) { inclusive = true }
                        }
                    }
                )
            }

            // Pantalla de Lista de Recetas
            composable(Routes.RECIPES) {
                RecipeListScreen(
                    recipeViewModel = recipeViewModel,
                    authViewModel = authViewModel,
                    onAddRecipe = { navController.navigate(Routes.ADD_RECIPE) },
                    onEditRecipe = { recipeId -> navController.navigate(Routes.editRecipe(recipeId)) }
                )
            }

            // Pantalla de Añadir Receta
            composable(Routes.ADD_RECIPE) {
                RecipeFormScreen(
                    recipeViewModel = recipeViewModel,
                    authViewModel = authViewModel,
                    recipeId = null,
                    onBack = { navController.popBackStack() }
                )
            }

            // Pantalla de Editar Receta
            composable(
                route = Routes.EDIT_RECIPE,
                arguments = listOf(navArgument("recipeId") { type = NavType.IntType })
            ) { backStackEntry ->
                val recipeId = backStackEntry.arguments?.getInt("recipeId")
                RecipeFormScreen(
                    recipeViewModel = recipeViewModel,
                    authViewModel = authViewModel,
                    recipeId = recipeId,
                    onBack = { navController.popBackStack() }
                )
            }

            // Pantalla de Ajustes
            composable(Routes.SETTINGS) {
                SettingsScreen(
                    settingsViewModel = settingsViewModel,
                    username = authViewModel.currentUsername ?: "Usuario",
                    isAdmin = authViewModel.isAdmin,
                    onLogout = {
                        authViewModel.logout()
                        navController.navigate(Routes.LOGIN) {
                            popUpTo(0) { inclusive = true }
                        }
                    }
                )
            }

            // Pantalla de Administración
            composable(Routes.ADMIN) {
                AdminScreen(authViewModel = authViewModel)
            }
        }
    }
}

