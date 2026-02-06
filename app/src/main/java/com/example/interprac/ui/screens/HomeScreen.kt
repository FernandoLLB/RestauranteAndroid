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
import com.example.interprac.ui.viewmodel.MapViewModel

@Composable
fun HomeScreen(
    onGoCamera: () -> Unit,
    onGoMap: () -> Unit,
    onGoFiles: () -> Unit,
    onGoNotification: () -> Unit,
    onGoSettings: () -> Unit
){
    Column(Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally) {

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