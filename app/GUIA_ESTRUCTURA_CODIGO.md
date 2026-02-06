# 📖 GUÍA COMPLETA DE ESTRUCTURA DE CÓDIGO
## Proyecto Android con Jetpack Compose - InterPrac

---

## 📋 ÍNDICE

1. [Visión General del Proyecto](#1-visión-general-del-proyecto)
2. [Estructura de Carpetas](#2-estructura-de-carpetas)
3. [Configuración de Gradle](#3-configuración-de-gradle)
4. [Arquitectura del Proyecto](#4-arquitectura-del-proyecto)
5. [Capa de Datos (Data Layer)](#5-capa-de-datos-data-layer)
6. [Capa de UI (Presentación)](#6-capa-de-ui-presentación)
7. [Navegación](#7-navegación)
8. [ViewModels](#8-viewmodels)
9. [Tema y Estilos](#9-tema-y-estilos)
10. [Patrones de Código Comunes](#10-patrones-de-código-comunes)
11. [Manejo de Permisos](#11-manejo-de-permisos)
12. [Ejemplos de Código Completos](#12-ejemplos-de-código-completos)

---

## 1. VISIÓN GENERAL DEL PROYECTO

### 1.1 Tecnologías Utilizadas

| Tecnología | Versión | Propósito |
|------------|---------|-----------|
| Kotlin | 2.0.21 | Lenguaje principal |
| Jetpack Compose | BOM 2024.09.00 | UI declarativa |
| Material 3 | (incluido en BOM) | Componentes de diseño |
| Navigation Compose | 2.7.7 / 2.9.6 | Navegación entre pantallas |
| DataStore Preferences | 1.1.1 | Persistencia de preferencias |
| Play Services Location | 21.3.0 | Servicios de ubicación |
| Android Gradle Plugin | 8.13.2 | Compilación |

### 1.2 Configuración de SDK

```
minSdk = 24
targetSdk = 36
compileSdk = 36
jvmTarget = 11
```

### 1.3 Características Habilitadas

- **Jetpack Compose**: UI declarativa moderna
- **Edge-to-Edge**: Diseño que aprovecha toda la pantalla
- **DataStore**: Persistencia reactiva de preferencias

---

## 2. ESTRUCTURA DE CARPETAS

```
app/src/main/java/com/example/interprac/
│
├── MainActivity.kt                    # Punto de entrada de la app
│
├── data/                              # CAPA DE DATOS
│   ├── local/                         # Almacenamiento local
│   │   ├── PreferencesKeys.kt         # Claves para DataStore
│   │   └── SettingsDataStore.kt       # Configuración de DataStore
│   │
│   └── repository/                    # Repositorios
│       └── SettingsRepository.kt      # Repositorio de configuraciones
│
├── navigation/                        # CAPA DE NAVEGACIÓN
│   ├── Routes.kt                      # Definición de rutas
│   └── AppNavGraph.kt                 # Grafo de navegación
│
├── notifications/                     # UTILIDADES DE NOTIFICACIONES
│   └── NotificationsHelper.kt         # Helper para notificaciones
│
└── ui/                                # CAPA DE PRESENTACIÓN
    ├── screens/                       # Pantallas (Composables)
    │   ├── HomeScreen.kt
    │   ├── CameraScreen.kt
    │   ├── MapScreen.kt
    │   ├── FilePickerScreen.kt
    │   ├── NotificationScreen.kt
    │   └── SettingsScreen.kt
    │
    ├── theme/                         # Tema de la aplicación
    │   ├── Color.kt                   # Definición de colores
    │   ├── Theme.kt                   # Configuración del tema
    │   └── Type.kt                    # Tipografía
    │
    └── viewmodel/                     # ViewModels
        ├── MapViewModel.kt
        └── SettingsViewModel.kt
```

### 2.1 Principios de Organización

1. **Separación por capas**: `data/`, `ui/`, `navigation/`
2. **Separación por funcionalidad**: `screens/`, `viewmodel/`, `theme/`
3. **Un archivo por clase/funcionalidad principal**
4. **Archivos pequeños y focalizados** (máximo ~100-150 líneas)

---

## 3. CONFIGURACIÓN DE GRADLE

### 3.1 Version Catalog (`gradle/libs.versions.toml`)

```toml
[versions]
agp = "8.13.2"
coreKtx = "1.10.1"
datastorePreferences = "1.1.1"
junit = "4.13.2"
junitVersion = "1.1.5"
espressoCore = "3.5.1"
lifecycleRuntimeKtx = "2.6.1"
activityCompose = "1.8.0"
kotlin = "2.0.21"
composeBom = "2024.09.00"
navigationCompose = "2.7.7"
playServicesLocation = "21.3.0"
navigationRuntimeKtx = "2.9.6"
navigationComposeVersion = "2.9.6"
coreKtxVersion = "1.17.0"

[libraries]
androidx-core-ktx = { group = "androidx.core", name = "core-ktx", version.ref = "coreKtx" }
androidx-datastore-preferences = { module = "androidx.datastore:datastore-preferences", version.ref = "datastorePreferences" }
androidx-navigation-compose = { module = "androidx.navigation:navigation-compose", version.ref = "navigationCompose" }
# ... más librerías

[plugins]
android-application = { id = "com.android.application", version.ref = "agp" }
kotlin-compose = { id = "org.jetbrains.kotlin.plugin.compose", version.ref = "kotlin" }
kotlin-android = { id = "org.jetbrains.kotlin.android", version.ref = "kotlin" }
```

### 3.2 Build.gradle.kts del módulo App

```kotlin
plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.kotlin.android)
}

android {
    namespace = "com.example.interprac"
    compileSdk {
        version = release(36)
    }

    defaultConfig {
        applicationId = "com.example.interprac"
        minSdk = 24
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    
    buildFeatures {
        compose = true
    }
    
    kotlinOptions {
        jvmTarget = "11"
    }
}

dependencies {
    // Navegación
    implementation(libs.androidx.navigation.compose)
    
    // DataStore
    implementation(libs.androidx.datastore.preferences)
    
    // Location Services
    implementation(libs.play.services.location)

    // Core
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    
    // Compose BOM (gestiona versiones automáticamente)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.material3)
    
    // Testing
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    
    // Debug
    debugImplementation(libs.androidx.compose.ui.tooling)
    debugImplementation(libs.androidx.compose.ui.test.manifest)
}
```

---

## 4. ARQUITECTURA DEL PROYECTO

### 4.1 Patrón Arquitectónico: MVVM Simplificado

```
┌─────────────────────────────────────────────────────────────┐
│                        UI LAYER                              │
│  ┌─────────────────┐    ┌─────────────────────────────────┐ │
│  │    Screens      │◄───│         ViewModels              │ │
│  │  (Composables)  │    │  (Estado + Lógica de negocio)   │ │
│  └─────────────────┘    └─────────────────────────────────┘ │
│           │                          │                       │
└───────────│──────────────────────────│───────────────────────┘
            │                          │
            ▼                          ▼
┌─────────────────────────────────────────────────────────────┐
│                       DATA LAYER                             │
│  ┌─────────────────────────────────────────────────────────┐│
│  │                    Repositories                          ││
│  │            (Abstracción de fuentes de datos)             ││
│  └─────────────────────────────────────────────────────────┘│
│                          │                                   │
│                          ▼                                   │
│  ┌─────────────────────────────────────────────────────────┐│
│  │              Local Data Sources                          ││
│  │           (DataStore, SharedPreferences)                 ││
│  └─────────────────────────────────────────────────────────┘│
└─────────────────────────────────────────────────────────────┘
```

### 4.2 Flujo de Datos

1. **UI → ViewModel**: Eventos del usuario (clicks, inputs)
2. **ViewModel → Repository**: Peticiones de datos o acciones
3. **Repository → DataStore**: Operaciones de lectura/escritura
4. **DataStore → Repository → ViewModel → UI**: Datos reactivos (Flow)

---

## 5. CAPA DE DATOS (DATA LAYER)

### 5.1 DataStore - Definición de Claves (`PreferencesKeys.kt`)

**Propósito**: Centralizar todas las claves de preferencias en un objeto.

```kotlin
package com.example.interprac.data.local

import androidx.datastore.preferences.core.booleanPreferencesKey

object PreferencesKeys {
    val DARK_MODE = booleanPreferencesKey("dark_mode")
    // Agregar más claves aquí según necesidad:
    // val USER_NAME = stringPreferencesKey("user_name")
    // val NOTIFICATION_ENABLED = booleanPreferencesKey("notification_enabled")
}
```

**Patrón utilizado**:
- `object` para singleton
- Nombres de claves en `SCREAMING_SNAKE_CASE`
- Uso de funciones tipadas (`booleanPreferencesKey`, `stringPreferencesKey`, etc.)

### 5.2 DataStore - Configuración (`SettingsDataStore.kt`)

**Propósito**: Crear la instancia de DataStore como extensión del Context.

```kotlin
package com.example.interprac.data.local

import android.content.Context
import androidx.datastore.preferences.preferencesDataStore

val Context.dataStore by preferencesDataStore("settings")
```

**Patrón utilizado**:
- **Extension property**: `Context.dataStore`
- **Delegated property**: `by preferencesDataStore(...)`
- Nombre del archivo de preferencias: `"settings"`

### 5.3 Repository (`SettingsRepository.kt`)

**Propósito**: Abstraer el acceso a datos y proporcionar una API limpia.

```kotlin
package com.example.interprac.data.repository

import android.content.Context
import androidx.datastore.preferences.core.edit
import com.example.interprac.data.local.PreferencesKeys
import com.example.interprac.data.local.dataStore
import kotlinx.coroutines.flow.map

class SettingsRepository(private val context: Context) {

    // Observar cambios (reactivo) - devuelve Flow
    fun observeDarkMode() = context.dataStore.data.map { preferences ->
        preferences[PreferencesKeys.DARK_MODE] ?: false
    }

    // Modificar valor (suspending function)
    suspend fun setDarkMode(enable: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.DARK_MODE] = enable
        }
    }
}
```

**Patrones utilizados**:
- **Inyección por constructor**: `private val context: Context`
- **Funciones de observación**: Devuelven `Flow<T>`
- **Funciones de escritura**: Marcadas como `suspend`
- **Valores por defecto**: `?: false`

---

## 6. CAPA DE UI (PRESENTACIÓN)

### 6.1 MainActivity.kt - Punto de Entrada

**Propósito**: Configurar la actividad principal e inicializar dependencias.

```kotlin
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
import com.example.interprac.data.repository.SettingsRepository
import com.example.interprac.navigation.AppNavGraph
import com.example.interprac.notifications.NotificationsHelper
import com.example.interprac.ui.theme.InterPracTheme
import com.example.interprac.ui.viewmodel.SettingsViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()  // Habilita diseño edge-to-edge

        // Inicialización de canales de notificación
        NotificationsHelper.createChannelIfNeeded(this)
        
        // Creación manual de dependencias (sin Hilt/Dagger)
        val settingsRepository = SettingsRepository(applicationContext)
        val settingsViewModel = SettingsViewModel(settingsRepository)

        setContent {
            // Tema que reacciona al modo oscuro del ViewModel
            InterPracTheme(settingsViewModel.darkMode) {
                val navController = rememberNavController()
                Scaffold(
                    Modifier
                        .systemBarsPadding()
                        .fillMaxSize()
                ) { innerPadding ->
                    Box(Modifier.padding(innerPadding)) {
                        AppNavGraph(
                            navController,
                            settingsViewModel
                        )
                    }
                }
            }
        }
    }
}
```

**Patrones utilizados**:
- **enableEdgeToEdge()**: Para diseño moderno
- **systemBarsPadding()**: Evitar overlap con barras del sistema
- **Scaffold**: Estructura base de Material 3
- **Inyección manual de dependencias** (sin frameworks DI)

### 6.2 Estructura de Screens (Pantallas)

#### 6.2.1 Pantalla Simple con Navegación (`HomeScreen.kt`)

```kotlin
package com.example.interprac.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun HomeScreen(
    onGoCamera: () -> Unit,
    onGoMap: () -> Unit,
    onGoFiles: () -> Unit,
    onGoNotification: () -> Unit,
    onGoSettings: () -> Unit
) {
    Column(
        Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("PRACTICA TEMA 7", style = MaterialTheme.typography.headlineSmall)
        
        Button(onClick = onGoCamera, modifier = Modifier.fillMaxWidth()) {
            Text("CAMARA")
        }
        Button(onClick = onGoFiles, modifier = Modifier.fillMaxWidth()) {
            Text("ARCHIVOS")
        }
        Button(onClick = onGoMap, modifier = Modifier.fillMaxWidth()) {
            Text("MAP")
        }
        Button(onClick = onGoNotification, modifier = Modifier.fillMaxWidth()) {
            Text("NOTIFICACIONES")
        }
        Button(onClick = onGoSettings, modifier = Modifier.fillMaxWidth()) {
            Text("SETTINGS")
        }
    }
}
```

**Patrones utilizados**:
- **Navegación mediante lambdas**: `onGoCamera: () -> Unit`
- **Column con spacing**: `Arrangement.spacedBy(12.dp)`
- **Tipografía de Material**: `MaterialTheme.typography.headlineSmall`
- **Modifier encadenado**: `.fillMaxSize().padding(16.dp)`

#### 6.2.2 Pantalla con ViewModel (`SettingsScreen.kt`)

```kotlin
package com.example.interprac.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.interprac.ui.viewmodel.SettingsViewModel

@Composable
fun SettingsScreen(
    onBack: () -> Unit,
    settingsViewModel: SettingsViewModel
) {
    Column(
        Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text("Ajustes de la APP", style = MaterialTheme.typography.headlineSmall)
        Button(onClick = onBack) { Text("Volver") }

        ElevatedCard(Modifier.fillMaxWidth()) {
            Row(
                Modifier.padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text("Modo Oscuro", style = MaterialTheme.typography.titleMedium)
                    Text("Enciende o apaga el modo oscuro de la app")
                }
                Switch(
                    checked = settingsViewModel.darkMode,
                    onCheckedChange = { enable -> settingsViewModel.setterDarkMode(enable) }
                )
            }
        }
    }
}
```

**Patrones utilizados**:
- **ViewModel como parámetro** (no usar `viewModel()` dentro)
- **ElevatedCard** para agrupar contenido relacionado
- **Two-way binding**: `checked` + `onCheckedChange`

#### 6.2.3 Pantalla con Permisos y Estado Local (`CameraScreen.kt`)

```kotlin
package com.example.interprac.ui.screens

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Bitmap
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat

@Composable
fun CameraScreen(onBack: () -> Unit) {
    val context = LocalContext.current

    // Estado local para el permiso
    var hasCameraPermission by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA)
                    == PackageManager.PERMISSION_GRANTED
        )
    }

    // Estado local para la foto
    var photo by remember { mutableStateOf<Bitmap?>(null) }

    // Launcher para solicitar permisos
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { granted ->
        hasCameraPermission = granted
    }
    
    // Launcher para tomar foto
    val takePictureLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.TakePicturePreview()
    ) { bitmap ->
        photo = bitmap
    }

    Column(
        Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Header con título y botón volver
        Row(
            Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("CAMARA", style = MaterialTheme.typography.headlineSmall)
            Button(onClick = onBack) { Text("Volver") }
        }

        // Contenido condicional basado en permisos
        if (hasCameraPermission) {
            Button(
                modifier = Modifier.fillMaxWidth(),
                onClick = { takePictureLauncher.launch(null) }
            ) {
                Text("Hacer foto")
            }

            // Mostrar foto si existe
            photo?.let {
                Text("Última foto")
                Image(
                    bitmap = it.asImageBitmap(),
                    contentDescription = "Foto tomada",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                )
            } ?: run {
                Text("Aun no hay foto")
            }
        } else {
            Text("Necesitamos permisos de camara")
            Button(onClick = { permissionLauncher.launch(Manifest.permission.CAMERA) }) {
                Text("Volver a solicitar permisos")
            }
        }
    }
}
```

**Patrones utilizados**:
- **LocalContext.current**: Obtener contexto en Composables
- **remember + mutableStateOf**: Estado local
- **rememberLauncherForActivityResult**: Para Activity Results
- **Renderizado condicional**: `if (hasPermission) { ... } else { ... }`
- **Safe call con let**: `photo?.let { ... } ?: run { ... }`

#### 6.2.4 Pantalla con Funciones Internas (`MapScreen.kt`)

```kotlin
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MapScreen(
    onBack: () -> Unit,
    mapViewModel: MapViewModel
) {
    val context = LocalContext.current
    val fusedLocationClient = remember { 
        LocationServices.getFusedLocationProviderClient(context) 
    }

    var query by remember { mutableStateOf("") }
    var status by remember { mutableStateOf("Escribe un sitio para buscar") }

    var hasLocationPermission by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        )
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { granted ->
        hasLocationPermission = granted
        status = if (granted) "Permiso concedido" else "Permiso denegado"
    }

    // Función local para abrir maps
    fun openMaps(geoUri: Uri) {
        val intent = Intent(Intent.ACTION_VIEW, geoUri).apply {
            setPackage("com.google.android.apps.maps")
        }
        try {
            context.startActivity(intent)
        } catch (e: Exception) {
            context.startActivity(Intent(Intent.ACTION_VIEW, geoUri))
        }
    }

    // Función local para obtener ubicación
    fun fetchLastLocation(onOk: (Double, Double) -> Unit) {
        if (hasLocationPermission) {
            fusedLocationClient.lastLocation
                .addOnSuccessListener { location ->
                    if (location != null) {
                        mapViewModel.setLastLocation(location.latitude, location.longitude)
                        onOk(location.latitude, location.longitude)
                        status = "Latitud: ${location.latitude}, Longitud: ${location.longitude}"
                    } else {
                        status = "No se pudo obtener la ubicación"
                        openMaps(Uri.parse("geo:0,0?q=Valencia,Spain"))
                    }
                }
                .addOnFailureListener { error ->
                    status = "Error al obtener la ubicación: ${error.message}"
                }
        }
    }

    Column(
        Modifier
            .fillMaxSize()
            .padding(16.dp), 
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Header
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text("MAPA", style = MaterialTheme.typography.headlineSmall)
            Button(onClick = onBack) { Text("Volver") }
        }

        // Campo de búsqueda
        OutlinedTextField(
            value = query,
            onValueChange = { query = it },
            label = { Text("Buscar Sitio") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        // Botón buscar
        Button(
            modifier = Modifier.fillMaxWidth(),
            onClick = {
                val queryTrim = query.trim()
                if (queryTrim.isEmpty()) {
                    status = "Escribe un sitio para buscar"
                } else {
                    val encodedQuery = Uri.encode(queryTrim)
                    openMaps(Uri.parse("geo:0,0?q=$encodedQuery"))
                    status = "Buscando: $queryTrim"
                }
            }
        ) {
            Text("Buscar en Google Maps")
        }

        Text(status)

        Button(
            modifier = Modifier.fillMaxWidth(),
            onClick = { permissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION) }
        ) {
            Text("Solicitar Permiso")
        }

        Button(
            modifier = Modifier.fillMaxWidth(),
            enabled = hasLocationPermission,
            onClick = {
                fetchLastLocation { lat, lon ->
                    openMaps(Uri.parse("geo:$lat,$lon"))
                }
            }
        ) {
            Text("Ver mi ubicacion en Google Maps")
        }
    }
}
```

**Patrones utilizados**:
- **Funciones locales (`fun` dentro del Composable)**: Para lógica reutilizable
- **OutlinedTextField**: Input de texto con estilo outlined
- **Callbacks con lambdas**: `onOk: (Double, Double) -> Unit`
- **Button con enabled**: Deshabilitar botón condicionalmente

#### 6.2.5 Pantalla de Selección de Archivos (`FilePickerScreen.kt`)

```kotlin
@Composable
fun FilePickerScreen(onBack: () -> Unit) {
    var selectedUri by remember { mutableStateOf<Uri?>(null) }

    val pickerLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.OpenDocument()
    ) { uri ->
        selectedUri = uri
    }

    Column(
        Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text("ARCHIVOS de la APP", style = MaterialTheme.typography.headlineSmall)
            Button(onClick = onBack) { Text("Volver") }
        }

        Button(
            modifier = Modifier.fillMaxWidth(),
            onClick = { pickerLauncher.launch(arrayOf("*/*")) }
        ) {
            Text("Seleccionar archivo")
        }

        ElevatedCard(Modifier.fillMaxWidth()) {
            Column(Modifier.padding(12.dp)) {
                Text("URI seleccionado:")
                Text(selectedUri?.toString() ?: "No hay nada seleccionado aun")
            }
        }
    }
}
```

**Patrones utilizados**:
- **ActivityResultContracts.OpenDocument()**: Selector de archivos
- **Estado nullable**: `Uri?`
- **Elvis operator para texto**: `selectedUri?.toString() ?: "texto default"`

---

## 7. NAVEGACIÓN

### 7.1 Definición de Rutas (`Routes.kt`)

```kotlin
package com.example.interprac.navigation

object Routes {
    const val HOME = "home"
    const val CAMERA = "camera"
    const val MAP = "map"
    const val FILES = "files"
    const val NOTIFICATIONS = "notifications"
    const val SETTINGS = "settings"
}
```

**Patrón utilizado**:
- `object` para singleton
- `const val` para constantes en tiempo de compilación
- Nombres en minúsculas para las rutas

### 7.2 Grafo de Navegación (`AppNavGraph.kt`)

```kotlin
package com.example.interprac.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.interprac.ui.screens.*
import com.example.interprac.ui.viewmodel.MapViewModel
import com.example.interprac.ui.viewmodel.SettingsViewModel

@Composable
fun AppNavGraph(
    navController: NavHostController,
    settingsViewModel: SettingsViewModel
) {
    NavHost(navController = navController, startDestination = Routes.HOME) {
        
        // Pantalla principal
        composable(Routes.HOME) {
            HomeScreen(
                onGoCamera = { navController.navigate(Routes.CAMERA) },
                onGoFiles = { navController.navigate(Routes.FILES) },
                onGoMap = { navController.navigate(Routes.MAP) },
                onGoNotification = { navController.navigate(Routes.NOTIFICATIONS) },
                onGoSettings = { navController.navigate(Routes.SETTINGS) }
            )
        }
        
        // Pantalla con navegación hacia atrás
        composable(Routes.CAMERA) {
            CameraScreen(onBack = { navController.popBackStack() })
        }
        
        // Pantalla con ViewModel creado localmente
        composable(Routes.MAP) {
            val mapViewModel: MapViewModel = viewModel()
            MapScreen(
                onBack = { navController.popBackStack() },
                mapViewModel = mapViewModel
            )
        }
        
        composable(Routes.FILES) {
            FilePickerScreen(onBack = { navController.popBackStack() })
        }
        
        composable(Routes.NOTIFICATIONS) {
            NotificationScreen(onBack = { navController.popBackStack() })
        }
        
        // Pantalla con ViewModel compartido (pasado como parámetro)
        composable(Routes.SETTINGS) {
            SettingsScreen(
                onBack = { navController.popBackStack() },
                settingsViewModel = settingsViewModel
            )
        }
    }
}
```

**Patrones utilizados**:
- **NavHost**: Contenedor de navegación
- **composable()**: Define cada destino
- **navController.navigate()**: Navegar hacia adelante
- **navController.popBackStack()**: Navegar hacia atrás
- **viewModel()**: Crear ViewModel scoped a la navegación
- **ViewModel compartido**: Pasar como parámetro desde MainActivity

---

## 8. VIEWMODELS

### 8.1 ViewModel Simple (`MapViewModel.kt`)

```kotlin
package com.example.interprac.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class MapViewModel : ViewModel() {

    var lastLat by mutableStateOf<Double?>(null)
        private set
    
    var lastLon by mutableStateOf<Double?>(null)
        private set

    fun setLastLocation(lat: Double, long: Double) {
        lastLat = lat
        lastLon = long
    }
}
```

**Patrones utilizados**:
- **mutableStateOf**: Estado observable por Compose
- **private set**: Solo el ViewModel puede modificar el estado
- **Tipos nullable**: `Double?` para estados opcionales

### 8.2 ViewModel con Repository y Coroutines (`SettingsViewModel.kt`)

```kotlin
package com.example.interprac.ui.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.interprac.data.repository.SettingsRepository
import kotlinx.coroutines.launch

class SettingsViewModel(
    private val settingsRepository: SettingsRepository
) : ViewModel() {

    var darkMode by mutableStateOf(false)
        private set

    init {
        // Observar cambios del repositorio al inicializar
        viewModelScope.launch {
            settingsRepository.observeDarkMode().collect { enable ->
                darkMode = enable
            }
        }
    }

    fun setterDarkMode(enable: Boolean) {
        viewModelScope.launch {
            settingsRepository.setDarkMode(enable)
        }
    }
}
```

**Patrones utilizados**:
- **Inyección por constructor**: `private val settingsRepository`
- **init block**: Inicialización de observadores
- **viewModelScope.launch**: Coroutines ligadas al ciclo de vida
- **Flow.collect**: Observación reactiva

---

## 9. TEMA Y ESTILOS

### 9.1 Colores (`Color.kt`)

```kotlin
package com.example.interprac.ui.theme

import androidx.compose.ui.graphics.Color

// Light Theme
val LightPrimary = Color(0xFF0061A4)
val LightSecondary = Color(0xFF535F70)
val LightTertiary = Color(0xFF6B5778)
val LightBackground = Color(0xFFFDFCFF)
val LightSurface = Color(0xFFFDFCFF)

// Dark Theme
val DarkPrimary = Color(0xFF9ECAFF)
val DarkSecondary = Color(0xFFBBC7DB)
val DarkTertiary = Color(0xFFD6BEE4)
val DarkBackground = Color(0xFF1A1C1E)
val DarkSurface = Color(0xFF1A1C1E)
```

**Patrón utilizado**:
- Variables `val` de nivel superior
- Nombres descriptivos con prefijo Light/Dark
- Colores en formato hexadecimal `0xFF...`

### 9.2 Tipografía (`Type.kt`)

```kotlin
package com.example.interprac.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

val Typography = Typography(
    bodyLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp
    )
    // Agregar más estilos según necesidad
)
```

### 9.3 Tema (`Theme.kt`)

```kotlin
package com.example.interprac.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val DarkColorScheme = darkColorScheme(
    primary = DarkPrimary,
    secondary = DarkSecondary,
    tertiary = DarkTertiary,
    background = DarkBackground,
    surface = DarkSurface
)

private val LightColorScheme = lightColorScheme(
    primary = LightPrimary,
    secondary = LightSecondary,
    tertiary = LightTertiary,
    background = LightBackground,
    surface = LightSurface
)

@Composable
fun InterPracTheme(
    darkTheme: Boolean,
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme
    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
```

**Patrón utilizado**:
- **Función composable como wrapper**: `InterPracTheme { ... }`
- **darkColorScheme/lightColorScheme**: Esquemas de Material 3
- **Parámetro booleano** para controlar el tema

---

## 10. PATRONES DE CÓDIGO COMUNES

### 10.1 Estructura Común de una Screen

```kotlin
@Composable
fun NombreScreen(
    onBack: () -> Unit,                    // Navegación hacia atrás
    viewModel: TipoViewModel = viewModel() // ViewModel opcional
) {
    val context = LocalContext.current     // Contexto si se necesita

    // Estados locales
    var estado by remember { mutableStateOf(valorInicial) }

    // Launchers para permisos/resultados
    val launcher = rememberLauncherForActivityResult(...) { result -> ... }

    // Funciones auxiliares locales
    fun funcionAuxiliar() { ... }

    // UI
    Column(
        Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Header
        Row(
            Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text("TITULO", style = MaterialTheme.typography.headlineSmall)
            Button(onClick = onBack) { Text("Volver") }
        }

        // Contenido...
    }
}
```

### 10.2 Patrón de Estado con remember

```kotlin
// Estado simple
var valor by remember { mutableStateOf("") }

// Estado nullable
var valorOpcional by remember { mutableStateOf<Tipo?>(null) }

// Estado con valor inicial calculado
var hasPermission by remember {
    mutableStateOf(
        ContextCompat.checkSelfPermission(context, Manifest.permission.X)
            == PackageManager.PERMISSION_GRANTED
    )
}
```

### 10.3 Patrón de Permisos

```kotlin
// 1. Estado del permiso
var hasPermission by remember {
    mutableStateOf(
        ContextCompat.checkSelfPermission(context, Manifest.permission.X)
            == PackageManager.PERMISSION_GRANTED
    )
}

// 2. Launcher para solicitar
val permissionLauncher = rememberLauncherForActivityResult(
    contract = ActivityResultContracts.RequestPermission()
) { granted ->
    hasPermission = granted
}

// 3. Uso en UI
Button(onClick = { permissionLauncher.launch(Manifest.permission.X) }) {
    Text("Solicitar Permiso")
}

// 4. Contenido condicional
if (hasPermission) {
    // Mostrar funcionalidad
} else {
    // Mostrar mensaje y botón de permiso
}
```

### 10.4 Patrón de Activity Result

```kotlin
// Para cámara
val takePictureLauncher = rememberLauncherForActivityResult(
    ActivityResultContracts.TakePicturePreview()
) { bitmap ->
    photo = bitmap
}

// Para selección de archivos
val pickerLauncher = rememberLauncherForActivityResult(
    ActivityResultContracts.OpenDocument()
) { uri ->
    selectedUri = uri
}

// Uso
Button(onClick = { takePictureLauncher.launch(null) }) { ... }
Button(onClick = { pickerLauncher.launch(arrayOf("*/*")) }) { ... }
```

---

## 11. MANEJO DE PERMISOS

### 11.1 AndroidManifest.xml

```xml
<?xml version="1.0" encoding="utf-8"?>
<manifest xmlns:android="http://schemas.android.com/apk/res/android">

    <!-- Permisos de ubicación -->
    <uses-permission android:name="android.permission.ACCESS_FINE_LOCATION"/>
    <uses-permission android:name="android.permission.ACCESS_COARSE_LOCATION"/>

    <!-- Permisos de notificaciones -->
    <uses-permission android:name="android.permission.POST_NOTIFICATIONS"/>

    <!-- Permisos de cámara -->
    <uses-feature android:name="android.hardware.camera" android:required="false" />
    <uses-permission android:name="android.permission.CAMERA"/>

    <application
        android:allowBackup="true"
        android:icon="@mipmap/ic_launcher"
        android:label="@string/app_name"
        android:theme="@style/Theme.InterPrac">
        
        <activity
            android:name=".MainActivity"
            android:exported="true">
            <intent-filter>
                <action android:name="android.intent.action.MAIN" />
                <category android:name="android.intent.category.LAUNCHER" />
            </intent-filter>
        </activity>
        
    </application>
</manifest>
```

### 11.2 NotificationsHelper (`notifications/NotificationsHelper.kt`)

```kotlin
package com.example.interprac.notifications

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat

object NotificationsHelper {

    private const val CHANNEL_ID = "demo_channel"

    fun createChannelIfNeeded(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                "Canal prac fin",
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = "Canal para notificar prac"
            }
            val manager = context.getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(channel)
        }
    }

    @SuppressLint("MissingPermission")
    fun sendSimpleNotification(context: Context) {
        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setContentTitle("Pract Fin Dam")
            .setContentText("Notificacion enviada correctamente")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .build()

        NotificationManagerCompat.from(context).notify(100, notification)
    }
}
```

**Patrones utilizados**:
- **object**: Singleton para helpers
- **Verificación de versión**: `Build.VERSION.SDK_INT >= Build.VERSION_CODES.O`
- **apply**: Para configurar objetos de forma fluida

---

## 12. EJEMPLOS DE CÓDIGO COMPLETOS

### 12.1 Plantilla para Nueva Pantalla Simple

```kotlin
package com.example.tuapp.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun NuevaScreen(onBack: () -> Unit) {
    Column(
        Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Row(
            Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text("TITULO", style = MaterialTheme.typography.headlineSmall)
            Button(onClick = onBack) { Text("Volver") }
        }

        // Tu contenido aquí
    }
}
```

### 12.2 Plantilla para Nueva Pantalla con ViewModel

```kotlin
package com.example.tuapp.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.tuapp.ui.viewmodel.TuViewModel

@Composable
fun NuevaScreen(
    onBack: () -> Unit,
    viewModel: TuViewModel
) {
    Column(
        Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Row(
            Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text("TITULO", style = MaterialTheme.typography.headlineSmall)
            Button(onClick = onBack) { Text("Volver") }
        }

        // Acceder a estado del viewModel
        Text("Valor: ${viewModel.estado}")
        
        // Modificar estado
        Button(onClick = { viewModel.modificarEstado() }) {
            Text("Acción")
        }
    }
}
```

### 12.3 Plantilla para Nuevo ViewModel

```kotlin
package com.example.tuapp.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class NuevoViewModel(
    private val repository: TuRepository // opcional
) : ViewModel() {

    var estado by mutableStateOf("valor inicial")
        private set

    init {
        // Inicialización, cargar datos, etc.
        viewModelScope.launch {
            // repository.observarDatos().collect { estado = it }
        }
    }

    fun modificarEstado(nuevoValor: String) {
        viewModelScope.launch {
            // repository.guardar(nuevoValor)
            estado = nuevoValor
        }
    }
}
```

### 12.4 Plantilla para Nuevo Repository

```kotlin
package com.example.tuapp.data.repository

import android.content.Context
import androidx.datastore.preferences.core.edit
import com.example.tuapp.data.local.PreferencesKeys
import com.example.tuapp.data.local.dataStore
import kotlinx.coroutines.flow.map

class NuevoRepository(private val context: Context) {

    fun observar() = context.dataStore.data.map { preferences ->
        preferences[PreferencesKeys.TU_KEY] ?: valorDefault
    }

    suspend fun guardar(valor: TipoValor) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.TU_KEY] = valor
        }
    }
}
```

### 12.5 Agregar Nueva Ruta

1. **En `Routes.kt`**:
```kotlin
object Routes {
    // ... rutas existentes
    const val NUEVA_PANTALLA = "nueva_pantalla"
}
```

2. **En `AppNavGraph.kt`**:
```kotlin
composable(Routes.NUEVA_PANTALLA) {
    NuevaScreen(onBack = { navController.popBackStack() })
}
```

3. **Desde otra pantalla** (para navegar):
```kotlin
Button(onClick = { navController.navigate(Routes.NUEVA_PANTALLA) }) {
    Text("Ir a nueva pantalla")
}
```

O pasar como lambda:
```kotlin
@Composable
fun OtraPantalla(onGoNueva: () -> Unit) {
    Button(onClick = onGoNueva) { Text("Ir a nueva") }
}

// En AppNavGraph:
composable(Routes.OTRA) {
    OtraPantalla(onGoNueva = { navController.navigate(Routes.NUEVA_PANTALLA) })
}
```

---

## 📌 RESUMEN DE CONVENCIONES

| Aspecto | Convención |
|---------|------------|
| **Nombres de archivos** | PascalCase (ej: `HomeScreen.kt`) |
| **Nombres de funciones** | camelCase (ej: `fetchLastLocation`) |
| **Constantes** | SCREAMING_SNAKE_CASE (ej: `DARK_MODE`) |
| **Rutas** | minúsculas (ej: `"home"`, `"settings"`) |
| **Paquetes** | minúsculas.separadas.por.puntos |
| **Composables** | PascalCase, sufijo descriptivo (ej: `HomeScreen`) |
| **ViewModels** | PascalCase + ViewModel (ej: `SettingsViewModel`) |
| **Repositories** | PascalCase + Repository (ej: `SettingsRepository`) |

---

## 🎯 PRINCIPIOS CLAVE

1. **Sencillez**: Código simple y directo, sin abstracciones innecesarias
2. **Un archivo, una responsabilidad**: Cada archivo hace una cosa
3. **Estado observable**: Usar `mutableStateOf` para UI reactiva
4. **Navegación por lambdas**: Screens no conocen el NavController
5. **ViewModels ligeros**: Solo estado y lógica de negocio simple
6. **Sin inyección de dependencias compleja**: Creación manual en MainActivity
7. **Coroutines para asincronía**: `viewModelScope.launch { }`
8. **Flow para observación reactiva**: `repository.observe().collect { }`

---

*Documento generado para servir como contexto a agentes de IA en proyectos similares.*

