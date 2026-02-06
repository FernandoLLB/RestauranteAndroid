package com.example.interprac.ui.screens

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.example.interprac.data.local.entity.RecipeEntity
import com.example.interprac.ui.state.UiState
import com.example.interprac.ui.viewmodel.AuthViewModel
import com.example.interprac.ui.viewmodel.RecipeViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecipeFormScreen(
    recipeViewModel: RecipeViewModel,
    authViewModel: AuthViewModel,
    recipeId: Int? = null,
    onBack: () -> Unit
) {
    val context = LocalContext.current
    val saveState by recipeViewModel.saveState.collectAsState()
    val detailState by recipeViewModel.recipeDetailState.collectAsState()

    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var ingredients by remember { mutableStateOf("") }
    var cuisineType by remember { mutableStateOf("Otra") }
    var prepTimeMinutes by remember { mutableStateOf("30") }
    var difficulty by remember { mutableStateOf(3) }
    var chef by remember { mutableStateOf("") }
    var imageUri by remember { mutableStateOf<String?>(null) }
    var photoBitmap by remember { mutableStateOf<Bitmap?>(null) }

    var hasCameraPermission by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA)
                    == PackageManager.PERMISSION_GRANTED
        )
    }

    val cuisineOptions = listOf("Mexicana", "Italiana", "Española", "Asiática", "Americana", "Francesa", "Mediterránea", "Vegetariana", "Otra")

    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted -> hasCameraPermission = granted }

    val cameraLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.TakePicturePreview()
    ) { bitmap ->
        if (bitmap != null) {
            photoBitmap = bitmap
            imageUri = null
        }
    }

    val galleryLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            imageUri = it.toString()
            photoBitmap = null
        }
    }

    LaunchedEffect(recipeId) {
        if (recipeId != null) {
            recipeViewModel.loadRecipeById(recipeId)
        }
    }

    LaunchedEffect(detailState) {
        if (detailState is UiState.Success) {
            val recipe = (detailState as UiState.Success<RecipeEntity>).data
            title = recipe.title
            description = recipe.description
            ingredients = recipe.ingredients
            cuisineType = recipe.cuisineType
            prepTimeMinutes = recipe.prepTimeMinutes.toString()
            difficulty = recipe.difficulty
            chef = recipe.chef
            imageUri = recipe.imageUri
        }
    }

    LaunchedEffect(saveState) {
        if (saveState is UiState.Success) {
            recipeViewModel.resetSaveState()
            recipeViewModel.resetDetailState()
            onBack()
        }
    }

    val isEditing = recipeId != null

    Column(
        Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Header
        Row(
            Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                if (isEditing) "EDITAR RECETA" else "NUEVA RECETA",
                style = MaterialTheme.typography.headlineSmall
            )
            Button(onClick = {
                recipeViewModel.resetSaveState()
                recipeViewModel.resetDetailState()
                onBack()
            }) { Text("Volver") }
        }

        // Error
        if (saveState is UiState.Error) {
            Text((saveState as UiState.Error).message, color = MaterialTheme.colorScheme.error)
        }

        // Imagen
        if (photoBitmap != null) {
            Image(
                bitmap = photoBitmap!!.asImageBitmap(),
                contentDescription = "Foto",
                modifier = Modifier.fillMaxWidth().height(150.dp)
            )
        }

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            OutlinedButton(onClick = {
                if (hasCameraPermission) cameraLauncher.launch(null)
                else permissionLauncher.launch(Manifest.permission.CAMERA)
            }) { Text("Cámara") }
            OutlinedButton(onClick = { galleryLauncher.launch("image/*") }) { Text("Galería") }
        }

        // Campos del formulario
        OutlinedTextField(
            value = title,
            onValueChange = { title = it },
            label = { Text("Nombre de la receta") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = chef,
            onValueChange = { chef = it },
            label = { Text("Chef / Autor") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        // Tipo de cocina dropdown
        var cuisineExpanded by remember { mutableStateOf(false) }
        ExposedDropdownMenuBox(
            expanded = cuisineExpanded,
            onExpandedChange = { cuisineExpanded = it }
        ) {
            OutlinedTextField(
                value = cuisineType,
                onValueChange = {},
                label = { Text("Tipo de cocina") },
                readOnly = true,
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = cuisineExpanded) },
                modifier = Modifier.fillMaxWidth().menuAnchor(MenuAnchorType.PrimaryNotEditable, enabled = true)
            )
            ExposedDropdownMenu(
                expanded = cuisineExpanded,
                onDismissRequest = { cuisineExpanded = false }
            ) {
                cuisineOptions.forEach { option ->
                    DropdownMenuItem(
                        text = { Text(option) },
                        onClick = {
                            cuisineType = option
                            cuisineExpanded = false
                        }
                    )
                }
            }
        }

        OutlinedTextField(
            value = prepTimeMinutes,
            onValueChange = { prepTimeMinutes = it.filter { c -> c.isDigit() } },
            label = { Text("Tiempo (minutos)") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        // Dificultad
        Text("Dificultad: $difficulty/5")
        Slider(
            value = difficulty.toFloat(),
            onValueChange = { difficulty = it.toInt() },
            valueRange = 1f..5f,
            steps = 3
        )

        OutlinedTextField(
            value = ingredients,
            onValueChange = { ingredients = it },
            label = { Text("Ingredientes") },
            minLines = 2,
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = description,
            onValueChange = { description = it },
            label = { Text("Instrucciones") },
            minLines = 3,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Guardar
        Button(
            onClick = {
                if (title.isNotBlank() && chef.isNotBlank() && ingredients.isNotBlank() && description.isNotBlank()) {
                    val prepTime = prepTimeMinutes.toIntOrNull() ?: 30

                    if (isEditing && detailState is UiState.Success) {
                        val existingRecipe = (detailState as UiState.Success<RecipeEntity>).data
                        recipeViewModel.updateRecipe(
                            existingRecipe.copy(
                                title = title,
                                description = description,
                                ingredients = ingredients,
                                imageUri = imageUri,
                                cuisineType = cuisineType,
                                prepTimeMinutes = prepTime,
                                difficulty = difficulty,
                                chef = chef
                            )
                        )
                    } else {
                        recipeViewModel.createRecipe(
                            title = title,
                            description = description,
                            ingredients = ingredients,
                            imageUri = imageUri,
                            cuisineType = cuisineType,
                            prepTimeMinutes = prepTime,
                            difficulty = difficulty,
                            chef = chef,
                            userId = authViewModel.currentUsername ?: "unknown"
                        )
                    }
                }
            },
            modifier = Modifier.fillMaxWidth(),
            enabled = saveState !is UiState.Loading
        ) {
            if (saveState is UiState.Loading) {
                CircularProgressIndicator(modifier = Modifier.size(20.dp), color = MaterialTheme.colorScheme.onPrimary)
            } else {
                Text(if (isEditing) "Guardar cambios" else "Crear receta")
            }
        }
    }
}

