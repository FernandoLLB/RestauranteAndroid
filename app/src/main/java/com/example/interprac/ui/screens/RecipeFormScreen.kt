package com.example.interprac.ui.screens

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.example.interprac.data.local.entity.RecipeEntity
import com.example.interprac.ui.state.UiState
import com.example.interprac.ui.viewmodel.AuthViewModel
import com.example.interprac.ui.viewmodel.RecipeViewModel
import java.io.File
import java.io.FileOutputStream

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
    var photo by remember { mutableStateOf<Bitmap?>(null) }


    var hasCameraPermission by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA)
                    == PackageManager.PERMISSION_GRANTED
        )
    }


    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { granted ->
        hasCameraPermission = granted
    }


    val cameraLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.TakePicturePreview()
    ) { bitmap ->
        if (bitmap != null) {
            photo = bitmap
            val file = File(context.cacheDir, "recipe_photo_${System.currentTimeMillis()}.jpg")
            FileOutputStream(file).use { out ->
                bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out)
            }
            imageUri = Uri.fromFile(file).toString()
        }
    }


    val galleryLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            imageUri = it.toString()
            photo = null
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

        if (saveState is UiState.Error) {
            Text((saveState as UiState.Error).message, color = MaterialTheme.colorScheme.error)
        }


        if (photo != null) {

            Text("Foto de la receta:")
            Image(
                bitmap = photo!!.asImageBitmap(),
                contentDescription = "Foto de receta",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp)
            )
        } else if (!imageUri.isNullOrBlank()) {

            val loadedBitmap = remember(imageUri) {
                try {
                    val uri = Uri.parse(imageUri)
                    val inputStream = context.contentResolver.openInputStream(uri)
                    BitmapFactory.decodeStream(inputStream)
                } catch (e: Exception) {
                    null
                }
            }

            if (loadedBitmap != null) {
                Text("Foto de la receta:")
                Image(
                    bitmap = loadedBitmap.asImageBitmap(),
                    contentDescription = "Foto de receta",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(150.dp)
                )
            } else {
                Text("No se pudo cargar la imagen")
            }
        } else {
            Text("No hay imagen seleccionada")
        }

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            OutlinedButton(onClick = {
                if (hasCameraPermission) {
                    cameraLauncher.launch(null)
                } else {
                    permissionLauncher.launch(Manifest.permission.CAMERA)
                }
            }) { Text("Cámara") }
            OutlinedButton(onClick = { galleryLauncher.launch("image/*") }) { Text("Galería") }
        }


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

        Text("Tipo de cocina: $cuisineType")
        Row(
            Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            OutlinedButton(onClick = { cuisineType = "Mexicana" }) { Text("Mexicana") }
            OutlinedButton(onClick = { cuisineType = "Italiana" }) { Text("Italiana") }
            OutlinedButton(onClick = { cuisineType = "Española" }) { Text("Española") }
        }
        Row(
            Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            OutlinedButton(onClick = { cuisineType = "Asiática" }) { Text("Asiática") }
            OutlinedButton(onClick = { cuisineType = "Americana" }) { Text("Americana") }
            OutlinedButton(onClick = { cuisineType = "Otra" }) { Text("Otra") }
        }

        OutlinedTextField(
            value = prepTimeMinutes,
            onValueChange = { prepTimeMinutes = it.filter { c -> c.isDigit() } },
            label = { Text("Tiempo (minutos)") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

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
