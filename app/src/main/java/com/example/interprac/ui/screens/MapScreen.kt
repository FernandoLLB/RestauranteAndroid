package com.example.interprac.ui.screens

import android.Manifest
import android.content.Intent
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.example.interprac.ui.viewmodel.MapViewModel
import com.google.android.gms.location.LocationServices


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MapScreen(
    onBack: () -> Unit,
    mapViewModel: MapViewModel
) {
    val context = LocalContext.current
    val fusedLocationClient = remember { LocationServices.getFusedLocationProviderClient(context) }

    var query by remember { mutableStateOf("") }
    var status by remember { mutableStateOf("Escribe un sitio para buscar") }

    var hasLocationPermission by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == android.content.pm.PackageManager.PERMISSION_GRANTED
        )
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { granted ->
        hasLocationPermission = granted
        status = if (granted) "Permiso concedido" else "Permiso denegado"
    }

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
        } else {
            status = "Permiso denegado"
        }
    }

    Column(Modifier.fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)){
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween){
            Text("MAPA", style = MaterialTheme.typography.headlineSmall)
            Button(onClick = onBack){
                Text("Volver")
            }
        }

        OutlinedTextField(
            value = query,
            onValueChange = { query = it },
            label = { Text("Buscar Sitio") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        Button(modifier = Modifier.fillMaxWidth(),
            onClick = {
                val queryTrim = query.trim()
                if(queryTrim.isEmpty()){
                    status = "Escribe un sitio para buscar"
                }else{
                    val encodedQuery = Uri.encode(queryTrim)
                    openMaps(Uri.parse("geo:0,0?q=$encodedQuery"))
                    status = "Buscando: $queryTrim"
                }
            }){
            Text("Buscar en Google Maps")
        }

        Text(status)

        Button(
            modifier = Modifier.fillMaxWidth(),
            onClick = {
                permissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
            }
        ) {
            Text("Solicitar Permiso")
        }

        Button(modifier = Modifier.fillMaxWidth(), enabled = hasLocationPermission,
            onClick = {fetchLastLocation {lat, lon ->
                openMaps(Uri.parse("geo:$lat,$lon"))
            }})
        {
            Text("Ver mi ubicacion en Google Maps")
        }
    }
}
