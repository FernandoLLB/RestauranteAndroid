package com.example.interprac.ui.screens

import android.graphics.BitmapFactory
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.interprac.data.local.entity.RecipeEntity
import com.example.interprac.ui.state.UiState
import com.example.interprac.ui.viewmodel.AuthViewModel
import com.example.interprac.ui.viewmodel.RecipeViewModel

@Composable
fun RecipeListScreen(
    recipeViewModel: RecipeViewModel,
    authViewModel: AuthViewModel,
    onAddRecipe: () -> Unit,
    onEditRecipe: (Int) -> Unit
) {
    val recipesState by recipeViewModel.recipesState.collectAsState()
    var showDeleteDialog by remember { mutableStateOf<RecipeEntity?>(null) }

    LaunchedEffect(Unit) {
        recipeViewModel.currentUserId = authViewModel.currentUsername
        recipeViewModel.loadRecipes()
    }

    Column(
        Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Row(
            Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("MIS RECETAS", style = MaterialTheme.typography.headlineSmall)
            Button(onClick = onAddRecipe) { Text("Añadir") }
        }

        Row(
            Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            FilterChip(
                selected = !recipeViewModel.showOnlyMyRecipes,
                onClick = { recipeViewModel.updateShowOnlyMyRecipes(false) },
                label = { Text("Todas") }
            )
            FilterChip(
                selected = recipeViewModel.showOnlyMyRecipes,
                onClick = { recipeViewModel.updateShowOnlyMyRecipes(true) },
                label = { Text("Solo mías") }
            )
        }

        OutlinedTextField(
            value = recipeViewModel.searchQuery,
            onValueChange = { recipeViewModel.updateSearchQuery(it) },
            label = { Text("Buscar recetas") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        when (val state = recipesState) {
            is UiState.Loading -> {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }

            is UiState.Error -> {
                Column(
                    Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(state.message, color = MaterialTheme.colorScheme.error)
                    Button(onClick = { recipeViewModel.loadRecipes() }) {
                        Text("Reintentar")
                    }
                }
            }

            is UiState.Success -> {
                if (state.data.isEmpty()) {
                    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text("No hay recetas. ¡Añade la primera!")
                    }
                } else {
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(state.data, key = { it.id }) { recipe ->
                            RecipeCard(
                                recipe = recipe,
                                currentUserId = authViewModel.currentUsername,
                                onEdit = { onEditRecipe(recipe.id) },
                                onDelete = { showDeleteDialog = recipe }
                            )
                        }
                    }
                }
            }

            is UiState.Idle -> {}
        }
    }

    showDeleteDialog?.let { recipe ->
        AlertDialog(
            onDismissRequest = { showDeleteDialog = null },
            title = { Text("Eliminar receta") },
            text = { Text("¿Eliminar \"${recipe.title}\"?") },
            confirmButton = {
                TextButton(onClick = {
                    recipeViewModel.deleteRecipe(recipe)
                    showDeleteDialog = null
                }) { Text("Eliminar") }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteDialog = null }) { Text("Cancelar") }
            }
        )
    }
}

@Composable
fun RecipeCard(
    recipe: RecipeEntity,
    currentUserId: String?,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    val isOwner = recipe.userId == currentUserId
    val context = LocalContext.current

    ElevatedCard(Modifier.fillMaxWidth()) {
        Column(Modifier.padding(12.dp)) {


            if (!recipe.imageUri.isNullOrBlank()) {
                val loadedBitmap = remember(recipe.imageUri) {
                    try {
                        val uri = Uri.parse(recipe.imageUri)
                        val inputStream = context.contentResolver.openInputStream(uri)
                        BitmapFactory.decodeStream(inputStream)
                    } catch (e: Exception) {
                        null
                    }
                }

                if (loadedBitmap != null) {
                    Image(
                        bitmap = loadedBitmap.asImageBitmap(),
                        contentDescription = "Imagen de ${recipe.title}",
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(150.dp)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }

            Text(recipe.title, style = MaterialTheme.typography.titleMedium)
            Text("${recipe.cuisineType} • ${recipe.prepTimeMinutes} min • Dificultad: ${recipe.difficulty}/5")
            Text("Chef: ${recipe.chef}", style = MaterialTheme.typography.bodySmall)

            Spacer(modifier = Modifier.height(8.dp))
            Text(recipe.description, style = MaterialTheme.typography.bodySmall)

            if (isOwner) {
                Spacer(modifier = Modifier.height(8.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedButton(onClick = onEdit) { Text("Editar") }
                    Button(
                        onClick = onDelete,
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
                    ) { Text("Eliminar") }
                }
            }
        }
    }
}
