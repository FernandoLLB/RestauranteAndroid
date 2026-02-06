package com.example.interprac.ui.screens

import androidx.annotation.ColorInt
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
import java.lang.Compiler.enable

@Composable
fun SettingsScreen(
    onBack: () -> Unit,
    settingsViewModel: SettingsViewModel
){
    Column(Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Text("Ajustes de la APP", style = MaterialTheme.typography.headlineSmall)
            Button(onClick = onBack) { Text("Volver")}

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
                    onCheckedChange = {enable -> settingsViewModel.setterDarkMode(enable)}
                )
            }
        }
        }



}