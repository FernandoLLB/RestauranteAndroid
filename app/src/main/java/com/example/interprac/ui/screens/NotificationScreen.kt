package com.example.interprac.ui.screens

import android.Manifest
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.example.interprac.notifications.NotificationsHelper

@Composable
fun NotificationScreen(onBack: () -> Unit) {
    val context = LocalContext.current

    var status by remember { mutableStateOf("") }
    var hasCamaraPermision by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS
            ) == android.content.pm.PackageManager.PERMISSION_GRANTED
        )
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { granted ->
        hasCamaraPermision = granted
        status = if (granted) "Permiso concedido" else "Permiso denegado"
    }

    

    Column(Modifier.fillMaxWidth().padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {

        Text("Notificaciones", style = MaterialTheme.typography.headlineSmall)
        Button(onClick = {
                onBack()
        }) {
            Text("Volver")
        }

        Button(
            modifier = Modifier.fillMaxWidth(),
            onClick = {
                permissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        ) {
            Text("Solicitar Permiso")
        }

        Spacer(modifier = Modifier.weight(1f))
        Text(status)
        Button(
            modifier = Modifier.fillMaxWidth(),
            onClick = { NotificationsHelper.sendSimpleNotification(context = context)}
        ) {
            Text("Enviar notificacion")
        }
    }
}