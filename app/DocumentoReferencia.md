# 馃摉 GU脥A COMPLETA DE ESTRUCTURA DE C脫DIGO
## Proyecto Android con Jetpack Compose - InterPrac

---

## 馃搵 脥NDICE

1. [Visi贸n General del Proyecto](#1-visi贸n-general-del-proyecto)
2. [Estructura de Carpetas](#2-estructura-de-carpetas)
3. [Configuraci贸n de Gradle](#3-configuraci贸n-de-gradle)
4. [Arquitectura del Proyecto](#4-arquitectura-del-proyecto)
5. [Capa de Datos (Data Layer)](#5-capa-de-datos-data-layer)
6. [Capa de UI (Presentaci贸n)](#6-capa-de-ui-presentaci贸n)
7. [Navegaci贸n](#7-navegaci贸n)
8. [ViewModels](#8-viewmodels)
9. [Tema y Estilos](#9-tema-y-estilos)
10. [Patrones de C贸digo Comunes](#10-patrones-de-c贸digo-comunes)
11. [Manejo de Permisos](#11-manejo-de-permisos)
12. [Ejemplos de C贸digo Completos](#12-ejemplos-de-c贸digo-completos)

---

## 1. VISI脫N GENERAL DEL PROYECTO

### 1.1 Tecnolog铆as Utilizadas

| Tecnolog铆a | Versi贸n | Prop贸sito |
|------------|---------|-----------|
| Kotlin | 2.0.21 | Lenguaje principal |
| Jetpack Compose | BOM 2024.09.00 | UI declarativa |
| Material 3 | (incluido en BOM) | Componentes de dise帽o |
| Navigation Compose | 2.7.7 / 2.9.6 | Navegaci贸n entre pantallas |
| DataStore Preferences | 1.1.1 | Persistencia de preferencias |
| Play Services Location | 21.3.0 | Servicios de ubicaci贸n |
| Android Gradle Plugin | 8.13.2 | Compilaci贸n |

### 1.2 Configuraci贸n de SDK

```
minSdk = 24
targetSdk = 36
compileSdk = 36
jvmTarget = 11
```

### 1.3 Caracter铆sticas Habilitadas

- **Jetpack Compose**: UI declarativa moderna
- **Edge-to-Edge**: Dise帽o que aprovecha toda la pantalla
- **DataStore**: Persistencia reactiva de preferencias

---

## 2. ESTRUCTURA DE CARPETAS

```
app/src/main/java/com/example/interprac/
鈹?
鈹溾攢鈹€ MainActivity.kt                    # Punto de entrada de la app
鈹?
鈹溾攢鈹€ data/                              # CAPA DE DATOS
鈹?  鈹溾攢鈹€ local/                         # Almacenamiento local
鈹?  鈹?  鈹溾攢鈹€ PreferencesKeys.kt         # Claves para DataStore
鈹?  鈹?  鈹斺攢鈹€ SettingsDataStore.kt       # Configuraci贸n de DataStore
鈹?  鈹?
鈹?  鈹斺攢鈹€ repository/                    # Repositorios
鈹?      鈹斺攢鈹€ SettingsRepository.kt      # Repositorio de configuraciones
鈹?
鈹溾攢鈹€ navigation/                        # CAPA DE NAVEGACI脫N
鈹?  鈹溾攢鈹€ Routes.kt                      # Definici贸n de rutas
鈹?  鈹斺攢鈹€ AppNavGraph.kt                 # Grafo de navegaci贸n
鈹?
鈹溾攢鈹€ notifications/                     # UTILIDADES DE NOTIFICACIONES
鈹?  鈹斺攢鈹€ NotificationsHelper.kt         # Helper para notificaciones
鈹?
鈹斺攢鈹€ ui/                                # CAPA DE PRESENTACI脫N
    鈹溾攢鈹€ screens/                       # Pantallas (Composables)
    鈹?  鈹溾攢鈹€ HomeScreen.kt
    鈹?  鈹溾攢鈹€ CameraScreen.kt
    鈹?  鈹溾攢鈹€ MapScreen.kt
    鈹?  鈹溾攢鈹€ FilePickerScreen.kt
    鈹?  鈹溾攢鈹€ NotificationScreen.kt
    鈹?  鈹斺攢鈹€ SettingsScreen.kt
    鈹?
    鈹溾攢鈹€ theme/                         # Tema de la aplicaci贸n
    鈹?  鈹溾攢鈹€ Color.kt                   # Definici贸n de colores
    鈹?  鈹溾攢鈹€ Theme.kt                   # Configuraci贸n del tema
    鈹?  鈹斺攢鈹€ Type.kt                    # Tipograf铆a
    鈹?
    鈹斺攢鈹€ viewmodel/                     # ViewModels
        鈹溾攢鈹€ MapViewModel.kt
        鈹斺攢鈹€ SettingsViewModel.kt
```

### 2.1 Principios de Organizaci贸n

1. **Separaci贸n por capas**: `data/`, `ui/`, `navigation/`
2. **Separaci贸n por funcionalidad**: `screens/`, `viewmodel/`, `theme/`
3. **Un archivo por clase/funcionalidad principal**
4. **Archivos peque帽os y focalizados** (m谩ximo ~100-150 l铆neas)

---

## 3. CONFIGURACI脫N DE GRADLE

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
# ... m谩s librer铆as

[plugins]
android-application = { id = "com.android.application", version.ref = "agp" }
kotlin-compose = { id = "org.jetbrains.kotlin.plugin.compose", version.ref = "kotlin" }
kotlin-android = { id = "org.jetbrains.kotlin.android", version.ref = "kotlin" }
```

### 3.2 Build.gradle.kts del m贸dulo App

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
    // Navegaci贸n
    implementation(libs.androidx.navigation.compose)
    
    // DataStore
    implementation(libs.androidx.datastore.preferences)
    
    // Location Services
    implementation(libs.play.services.location)

    // Core
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    
    // Compose BOM (gestiona versiones autom谩ticamente)
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

### 4.1 Patr贸n Arquitect贸nico: MVVM Simplificado

```
鈹屸攢鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹?
鈹?                       UI LAYER                              鈹?
鈹? 鈹屸攢鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹?   鈹屸攢鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹?鈹?
鈹? 鈹?   Screens      鈹傗梽鈹€鈹€鈹€鈹?        ViewModels              鈹?鈹?
鈹? 鈹? (Composables)  鈹?   鈹? (Estado + L贸gica de negocio)   鈹?鈹?
鈹? 鈹斺攢鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹?   鈹斺攢鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹?鈹?
鈹?          鈹?                         鈹?                      鈹?
鈹斺攢鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹傗攢鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹傗攢鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹?
            鈹?                         鈹?
            鈻?                         鈻?
鈹屸攢鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹?
鈹?                      DATA LAYER                             鈹?
鈹? 鈹屸攢鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹愨攤
鈹? 鈹?                   Repositories                          鈹傗攤
鈹? 鈹?           (Abstracci贸n de fuentes de datos)             鈹傗攤
鈹? 鈹斺攢鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹樷攤
鈹?                         鈹?                                  鈹?
鈹?                         鈻?                                  鈹?
鈹? 鈹屸攢鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹愨攤
鈹? 鈹?             Local Data Sources                          鈹傗攤
鈹? 鈹?          (DataStore, SharedPreferences)                 鈹傗攤
鈹? 鈹斺攢鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹樷攤
鈹斺攢鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹?
```

### 4.2 Flujo de Datos

1. **UI 鈫?ViewModel**: Eventos del usuario (clicks, inputs)
2. **ViewModel 鈫?Repository**: Peticiones de datos o acciones
3. **Repository 鈫?DataStore**: Operaciones de lectura/escritura
4. **DataStore 鈫?Repository 鈫?ViewModel 鈫?UI**: Datos reactivos (Flow)

---

## 5. CAPA DE DATOS (DATA LAYER)

### 5.1 DataStore - Definici贸n de Claves (`PreferencesKeys.kt`)

**Prop贸sito**: Centralizar todas las claves de preferencias en un objeto.

```kotlin
package com.example.interprac.data.local

import androidx.datastore.preferences.core.booleanPreferencesKey

object PreferencesKeys {
    val DARK_MODE = booleanPreferencesKey("dark_mode")
    // Agregar m谩s claves aqu铆 seg煤n necesidad:
    // val USER_NAME = stringPreferencesKey("user_name")
    // val NOTIFICATION_ENABLED = booleanPreferencesKey("notification_enabled")
}
```

**Patr贸n utilizado**:
- `object` para singleton
- Nombres de claves en `SCREAMING_SNAKE_CASE`
- Uso de funciones tipadas (`booleanPreferencesKey`, `stringPreferencesKey`, etc.)

### 5.2 DataStore - Configuraci贸n (`SettingsDataStore.kt`)

**Prop贸sito**: Crear la instancia de DataStore como extensi贸n del Context.

```kotlin
package com.example.interprac.data.local

import android.content.Context
import androidx.datastore.preferences.preferencesDataStore

val Context.dataStore by preferencesDataStore("settings")
```

**Patr贸n utilizado**:
- **Extension property**: `Context.dataStore`
- **Delegated property**: `by preferencesDataStore(...)`
- Nombre del archivo de preferencias: `"settings"`

### 5.3 Repository (`SettingsRepository.kt`)

**Prop贸sito**: Abstraer el acceso a datos y proporcionar una API limpia.

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
- **Inyecci贸n por constructor**: `private val context: Context`
- **Funciones de observaci贸n**: Devuelven `Flow<T>`
- **Funciones de escritura**: Marcadas como `suspend`
- **Valores por defecto**: `?: false`

---

## 6. CAPA DE UI (PRESENTACI脫N)

### 6.1 MainActivity.kt - Punto de Entrada

**Prop贸sito**: Configurar la actividad principal e inicializar dependencias.

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
        enableEdgeToEdge()  // Habilita dise帽o edge-to-edge

        // Inicializaci贸n de canales de notificaci贸n
        NotificationsHelper.createChannelIfNeeded(this)
        
        // Creaci贸n manual de dependencias (sin Hilt/Dagger)
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
- **enableEdgeToEdge()**: Para dise帽o moderno
- **systemBarsPadding()**: Evitar overlap con barras del sistema
- **Scaffold**: Estructura base de Material 3
- **Inyecci贸n manual de dependencias** (sin frameworks DI)

### 6.2 Estructura de Screens (Pantallas)

#### 6.2.1 Pantalla Simple con Navegaci贸n (`HomeScreen.kt`)

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
- **Navegaci贸n mediante lambdas**: `onGoCamera: () -> Unit`
- **Column con spacing**: `Arrangement.spacedBy(12.dp)`
- **Tipograf铆a de Material**: `MaterialTheme.typography.headlineSmall`
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
- **ViewModel como par谩metro** (no usar `viewModel()` dentro)
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
        // Header con t铆tulo y bot贸n volver
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
                Text("脷ltima foto")
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

    // Funci贸n local para abrir maps
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

    // Funci贸n local para obtener ubicaci贸n
    fun fetchLastLocation(onOk: (Double, Double) -> Unit) {
        if (hasLocationPermission) {
            fusedLocationClient.lastLocation
                .addOnSuccessListener { location ->
                    if (location != null) {
                        mapViewModel.setLastLocation(location.latitude, location.longitude)
                        onOk(location.latitude, location.longitude)
                        status = "Latitud: ${location.latitude}, Longitud: ${location.longitude}"
                    } else {
                        status = "No se pudo obtener la ubicaci贸n"
                        openMaps(Uri.parse("geo:0,0?q=Valencia,Spain"))
                    }
                }
                .addOnFailureListener { error ->
                    status = "Error al obtener la ubicaci贸n: ${error.message}"
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

        // Campo de b煤squeda
        OutlinedTextField(
            value = query,
            onValueChange = { query = it },
            label = { Text("Buscar Sitio") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        // Bot贸n buscar
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
- **Funciones locales (`fun` dentro del Composable)**: Para l贸gica reutilizable
- **OutlinedTextField**: Input de texto con estilo outlined
- **Callbacks con lambdas**: `onOk: (Double, Double) -> Unit`
- **Button con enabled**: Deshabilitar bot贸n condicionalmente

#### 6.2.5 Pantalla de Selecci贸n de Archivos (`FilePickerScreen.kt`)

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

## 7. NAVEGACI脫N

### 7.1 Definici贸n de Rutas (`Routes.kt`)

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

**Patr贸n utilizado**:
- `object` para singleton
- `const val` para constantes en tiempo de compilaci贸n
- Nombres en min煤sculas para las rutas

### 7.2 Grafo de Navegaci贸n (`AppNavGraph.kt`)

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
        
        // Pantalla con navegaci贸n hacia atr谩s
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
        
        // Pantalla con ViewModel compartido (pasado como par谩metro)
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
- **NavHost**: Contenedor de navegaci贸n
- **composable()**: Define cada destino
- **navController.navigate()**: Navegar hacia adelante
- **navController.popBackStack()**: Navegar hacia atr谩s
- **viewModel()**: Crear ViewModel scoped a la navegaci贸n
- **ViewModel compartido**: Pasar como par谩metro desde MainActivity

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
- **Inyecci贸n por constructor**: `private val settingsRepository`
- **init block**: Inicializaci贸n de observadores
- **viewModelScope.launch**: Coroutines ligadas al ciclo de vida
- **Flow.collect**: Observaci贸n reactiva

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

**Patr贸n utilizado**:
- Variables `val` de nivel superior
- Nombres descriptivos con prefijo Light/Dark
- Colores en formato hexadecimal `0xFF...`

### 9.2 Tipograf铆a (`Type.kt`)

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
    // Agregar m谩s estilos seg煤n necesidad
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

**Patr贸n utilizado**:
- **Funci贸n composable como wrapper**: `InterPracTheme { ... }`
- **darkColorScheme/lightColorScheme**: Esquemas de Material 3
- **Par谩metro booleano** para controlar el tema

---

## 10. PATRONES DE C脫DIGO COMUNES

### 10.1 Estructura Com煤n de una Screen

```kotlin
@Composable
fun NombreScreen(
    onBack: () -> Unit,                    // Navegaci贸n hacia atr谩s
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

### 10.2 Patr贸n de Estado con remember

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

### 10.3 Patr贸n de Permisos

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
    // Mostrar mensaje y bot贸n de permiso
}
```

### 10.4 Patr贸n de Activity Result

```kotlin
// Para c谩mara
val takePictureLauncher = rememberLauncherForActivityResult(
    ActivityResultContracts.TakePicturePreview()
) { bitmap ->
    photo = bitmap
}

// Para selecci贸n de archivos
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

    <!-- Permisos de ubicaci贸n -->
    <uses-permission android:name="android.permission.ACCESS_FINE_LOCATION"/>
    <uses-permission android:name="android.permission.ACCESS_COARSE_LOCATION"/>

    <!-- Permisos de notificaciones -->
    <uses-permission android:name="android.permission.POST_NOTIFICATIONS"/>

    <!-- Permisos de c谩mara -->
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
- **Verificaci贸n de versi贸n**: `Build.VERSION.SDK_INT >= Build.VERSION_CODES.O`
- **apply**: Para configurar objetos de forma fluida

---

## 12. EJEMPLOS DE C脫DIGO COMPLETOS

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

        // Tu contenido aqu铆
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
            Text("Acci贸n")
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
        // Inicializaci贸n, cargar datos, etc.
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

## 馃搶 RESUMEN DE CONVENCIONES

| Aspecto | Convenci贸n |
|---------|------------|
| **Nombres de archivos** | PascalCase (ej: `HomeScreen.kt`) |
| **Nombres de funciones** | camelCase (ej: `fetchLastLocation`) |
| **Constantes** | SCREAMING_SNAKE_CASE (ej: `DARK_MODE`) |
| **Rutas** | min煤sculas (ej: `"home"`, `"settings"`) |
| **Paquetes** | min煤sculas.separadas.por.puntos |
| **Composables** | PascalCase, sufijo descriptivo (ej: `HomeScreen`) |
| **ViewModels** | PascalCase + ViewModel (ej: `SettingsViewModel`) |
| **Repositories** | PascalCase + Repository (ej: `SettingsRepository`) |

---

## 馃幆 PRINCIPIOS CLAVE

1. **Sencillez**: C贸digo simple y directo, sin abstracciones innecesarias
2. **Un archivo, una responsabilidad**: Cada archivo hace una cosa
3. **Estado observable**: Usar `mutableStateOf` para UI reactiva
4. **Navegaci贸n por lambdas**: Screens no conocen el NavController
5. **ViewModels ligeros**: Solo estado y l贸gica de negocio simple
6. **Sin inyecci贸n de dependencias compleja**: Creaci贸n manual en MainActivity
7. **Coroutines para asincron铆a**: `viewModelScope.launch { }`
8. **Flow para observaci贸n reactiva**: `repository.observe().collect { }`

---

*Documento generado para servir como contexto a agentes de IA en proyectos similares.*

