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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.material3.MenuAnchorType
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import coil.compose.AsyncImage
import com.example.interprac.data.local.entity.RecipeEntity
import com.example.interprac.data.uiState.UiState
import com.example.interprac.ui.viewmodel.AuthViewModel
import com.example.interprac.ui.viewmodel.RecipeViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecipeFormScreen(
    recipeViewModel: RecipeViewModel,
    authViewModel: AuthViewModel,
    recipeId: Int? = null, // null = create, non-null = edit
    onBack: () -> Unit
) {
    val context = LocalContext.current
    val saveState by recipeViewModel.saveState.collectAsState()
    val detailState by recipeViewModel.recipeDetailState.collectAsState()

    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var ingredients by remember { mutableStateOf("") }
    var cuisineType by remember { mutableStateOf("") }
    var prepTimeMinutes by remember { mutableStateOf("") }
    var difficulty by remember { mutableStateOf(3) }
    var chef by remember { mutableStateOf("") }
    var imageUri by remember { mutableStateOf<String?>(null) }
    var photoBitmap by remember { mutableStateOf<Bitmap?>(null) }

    var titleError by remember { mutableStateOf<String?>(null) }
    var descriptionError by remember { mutableStateOf<String?>(null) }
    var ingredientsError by remember { mutableStateOf<String?>(null) }
    var cuisineTypeError by remember { mutableStateOf<String?>(null) }
    var prepTimeError by remember { mutableStateOf<String?>(null) }
    var chefError by remember { mutableStateOf<String?>(null) }

    var hasCameraPermission by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA)
                    == PackageManager.PERMISSION_GRANTED
        )
    }

    val cuisineOptions = listOf("Mexicana", "Italiana", "Española", "Asiática", "Americana", "Francesa", "Mediterránea", "Vegetariana", "Otra")

    // Permission launcher
    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted -> hasCameraPermission = granted }

    // Camera launcher
    val cameraLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.TakePicturePreview()
    ) { bitmap ->
        if (bitmap != null) {
            photoBitmap = bitmap
            imageUri = null // Clear gallery uri when using camera
        }
    }

    // Gallery launcher
    val galleryLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            imageUri = it.toString()
            photoBitmap = null // Clear camera bitmap when using gallery
        }
    }

    // Load existing recipe if editing
    LaunchedEffect(recipeId) {
        if (recipeId != null) {
            recipeViewModel.loadRecipeById(recipeId)
        }
    }

    // Populate form when recipe is loaded
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

    // Handle save success
    LaunchedEffect(saveState) {
        if (saveState is UiState.Success) {
            recipeViewModel.resetSaveState()
            recipeViewModel.resetDetailState()
            onBack()
        }
    }

    val isEditing = recipeId != null

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (isEditing) "Editar Receta" else "Nueva Receta") },
                navigationIcon = {
                    IconButton(onClick = {
                        recipeViewModel.resetSaveState()
                        recipeViewModel.resetDetailState()
                        onBack()
                    }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            // Image section
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
            ) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    when {
                        photoBitmap != null -> {
                            Image(
                                bitmap = photoBitmap!!.asImageBitmap(),
                                contentDescription = "Foto de la receta",
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.Crop
                            )
                        }
                        imageUri != null -> {
                            AsyncImage(
                                model = imageUri,
                                contentDescription = "Imagen de la receta",
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.Crop
                            )
                        }
                        else -> {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Icon(
                                    Icons.Default.AddAPhoto,
                                    contentDescription = null,
                                    modifier = Modifier.size(48.dp),
                                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    "Añadir imagen",
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Image buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedButton(
                    onClick = {
                        if (hasCameraPermission) {
                            cameraLauncher.launch(null)
                        } else {
                            permissionLauncher.launch(Manifest.permission.CAMERA)
                        }
                    },
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(Icons.Default.CameraAlt, contentDescription = null)
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Cámara")
                }

                OutlinedButton(
                    onClick = { galleryLauncher.launch("image/*") },
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(Icons.Default.PhotoLibrary, contentDescription = null)
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Galería")
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Title
            OutlinedTextField(
                value = title,
                onValueChange = { title = it; titleError = null },
                label = { Text("Nombre de la receta *") },
                leadingIcon = { Icon(Icons.Default.Restaurant, contentDescription = null) },
                isError = titleError != null,
                supportingText = titleError?.let { { Text(it) } },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Chef
            OutlinedTextField(
                value = chef,
                onValueChange = { chef = it; chefError = null },
                label = { Text("Chef / Autor *") },
                leadingIcon = { Icon(Icons.Default.Person, contentDescription = null) },
                isError = chefError != null,
                supportingText = chefError?.let { { Text(it) } },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Cuisine type dropdown
            var cuisineExpanded by remember { mutableStateOf(false) }
            ExposedDropdownMenuBox(
                expanded = cuisineExpanded,
                onExpandedChange = { cuisineExpanded = it }
            ) {
                OutlinedTextField(
                    value = cuisineType,
                    onValueChange = { cuisineType = it; cuisineTypeError = null },
                    label = { Text("Tipo de cocina *") },
                    leadingIcon = { Icon(Icons.Default.Public, contentDescription = null) },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = cuisineExpanded) },
                    isError = cuisineTypeError != null,
                    supportingText = cuisineTypeError?.let { { Text(it) } },
                    readOnly = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor(MenuAnchorType.PrimaryNotEditable, enabled = true)
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

            Spacer(modifier = Modifier.height(12.dp))

            // Prep time
            OutlinedTextField(
                value = prepTimeMinutes,
                onValueChange = { prepTimeMinutes = it.filter { char -> char.isDigit() }; prepTimeError = null },
                label = { Text("Tiempo de preparación (minutos) *") },
                leadingIcon = { Icon(Icons.Default.Schedule, contentDescription = null) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                isError = prepTimeError != null,
                supportingText = prepTimeError?.let { { Text(it) } },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Difficulty
            Text("Dificultad: $difficulty/5", style = MaterialTheme.typography.labelLarge)
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                (1..5).forEach { level ->
                    IconButton(onClick = { difficulty = level }) {
                        Icon(
                            imageVector = if (level <= difficulty) Icons.Default.Star else Icons.Default.StarBorder,
                            contentDescription = "Dificultad $level",
                            tint = if (level <= difficulty) MaterialTheme.colorScheme.tertiary else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Ingredients
            OutlinedTextField(
                value = ingredients,
                onValueChange = { ingredients = it; ingredientsError = null },
                label = { Text("Ingredientes *") },
                placeholder = { Text("Ej: 2 huevos, 100g harina, 50ml leche...") },
                isError = ingredientsError != null,
                supportingText = ingredientsError?.let { { Text(it) } },
                minLines = 3,
                maxLines = 5,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Description/Instructions
            OutlinedTextField(
                value = description,
                onValueChange = { description = it; descriptionError = null },
                label = { Text("Instrucciones de preparación *") },
                placeholder = { Text("Describe los pasos para preparar la receta...") },
                isError = descriptionError != null,
                supportingText = descriptionError?.let { { Text(it) } },
                minLines = 5,
                maxLines = 10,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Error message
            if (saveState is UiState.Error) {
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.errorContainer
                    ),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = (saveState as UiState.Error).message,
                        modifier = Modifier.padding(12.dp),
                        color = MaterialTheme.colorScheme.onErrorContainer
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))
            }

            // Save button
            Button(
                onClick = {
                    var hasError = false

                    if (title.isBlank()) {
                        titleError = "El nombre es obligatorio"
                        hasError = true
                    }

                    if (chef.isBlank()) {
                        chefError = "El chef es obligatorio"
                        hasError = true
                    }

                    if (cuisineType.isBlank()) {
                        cuisineTypeError = "Selecciona un tipo de cocina"
                        hasError = true
                    }

                    val prepTime = prepTimeMinutes.toIntOrNull()
                    if (prepTime == null || prepTime <= 0) {
                        prepTimeError = "Introduce un tiempo válido"
                        hasError = true
                    }

                    if (ingredients.isBlank()) {
                        ingredientsError = "Los ingredientes son obligatorios"
                        hasError = true
                    }

                    if (description.isBlank()) {
                        descriptionError = "Las instrucciones son obligatorias"
                        hasError = true
                    }

                    if (!hasError) {
                        val finalImageUri = imageUri // For now, camera photos would need to be saved to file

                        if (isEditing && detailState is UiState.Success) {
                            val existingRecipe = (detailState as UiState.Success<RecipeEntity>).data
                            recipeViewModel.updateRecipe(
                                existingRecipe.copy(
                                    title = title,
                                    description = description,
                                    ingredients = ingredients,
                                    imageUri = finalImageUri,
                                    cuisineType = cuisineType,
                                    prepTimeMinutes = prepTime!!,
                                    difficulty = difficulty,
                                    chef = chef
                                )
                            )
                        } else {
                            recipeViewModel.createRecipe(
                                title = title,
                                description = description,
                                ingredients = ingredients,
                                imageUri = finalImageUri,
                                cuisineType = cuisineType,
                                prepTimeMinutes = prepTime!!,
                                difficulty = difficulty,
                                chef = chef,
                                userId = authViewModel.currentUsername ?: "unknown"
                            )
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                enabled = saveState !is UiState.Loading
            ) {
                if (saveState is UiState.Loading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                } else {
                    Icon(Icons.Default.Save, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(if (isEditing) "Guardar cambios" else "Crear receta")
                }
            }

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

