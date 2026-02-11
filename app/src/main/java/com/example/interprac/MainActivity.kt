package com.example.interprac

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.example.interprac.data.local.database.AppDatabase
import com.example.interprac.data.repository.AuthRepository
import com.example.interprac.data.repository.RecipeRepository
import com.example.interprac.data.repository.SettingsRepository
import com.example.interprac.navigation.AppNavGraph
import com.example.interprac.notifications.NotificationsHelper
import com.example.interprac.ui.theme.InterPracTheme
import com.example.interprac.ui.viewmodel.AuthViewModel
import com.example.interprac.ui.viewmodel.RecipeViewModel
import com.example.interprac.ui.viewmodel.SettingsViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        NotificationsHelper.createChannelIfNeeded(this)

        val settingsRepository = SettingsRepository(applicationContext)
        val authRepository = AuthRepository(applicationContext)
        val database = AppDatabase.getDatabase(applicationContext)
        val recipeRepository = RecipeRepository(database.recipeDao())


        val settingsViewModel = SettingsViewModel(settingsRepository)
        val authViewModel = AuthViewModel(authRepository, applicationContext)
        val recipeViewModel = RecipeViewModel(recipeRepository, applicationContext)

        setContent {
            InterPracTheme(settingsViewModel.darkMode) {
                val navController = rememberNavController()
                Scaffold(
                    Modifier
                        .systemBarsPadding()
                        .fillMaxSize()
                ) { innerPadding ->
                    Box(Modifier.padding(innerPadding)) {
                        AppNavGraph(
                            navController = navController,
                            settingsViewModel = settingsViewModel,
                            authViewModel = authViewModel,
                            recipeViewModel = recipeViewModel
                        )
                    }
                }
            }
        }
    }
}
