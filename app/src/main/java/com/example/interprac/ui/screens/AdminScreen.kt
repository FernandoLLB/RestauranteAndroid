package com.example.interprac.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.interprac.data.remote.dto.UserDto
import com.example.interprac.ui.state.UiState
import com.example.interprac.ui.viewmodel.AuthViewModel

@Composable
fun AdminScreen(authViewModel: AuthViewModel) {
    val usersState by authViewModel.usersState.collectAsState()
    var showEditDialog by remember { mutableStateOf<UserDto?>(null) }
    var showDeleteDialog by remember { mutableStateOf<UserDto?>(null) }

    LaunchedEffect(Unit) {
        authViewModel.loadUsers()
    }

    Column(
        Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text("ADMINISTRACIÓN", style = MaterialTheme.typography.headlineSmall)

        if (!authViewModel.isOnline) {
            Text("⚠️ Sin conexión a internet", color = MaterialTheme.colorScheme.error)
        }

        when (val state = usersState) {
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
                    Button(onClick = { authViewModel.loadUsers() }) {
                        Text("Reintentar")
                    }
                }
            }

            is UiState.Success -> {
                Text("Usuarios registrados: ${state.data.size}")

                LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(state.data, key = { it.id }) { user ->
                        UserCard(
                            user = user,
                            currentUsername = authViewModel.currentUsername,
                            onEdit = { showEditDialog = user },
                            onDelete = { showDeleteDialog = user }
                        )
                    }
                }
            }

            is UiState.Idle -> {}
        }
    }

    // Diálogo editar
    showEditDialog?.let { user ->
        EditUserDialog(
            user = user,
            onDismiss = { showEditDialog = null },
            onConfirm = { firstname, lastname, role ->
                authViewModel.updateUser(user.id, firstname, lastname, role)
                showEditDialog = null
            }
        )
    }

    // Diálogo eliminar
    showDeleteDialog?.let { user ->
        val isSelf = user.username == authViewModel.currentUsername
        AlertDialog(
            onDismissRequest = { showDeleteDialog = null },
            title = { Text("Eliminar usuario") },
            text = {
                if (isSelf) Text("No puedes eliminarte a ti mismo.")
                else Text("¿Eliminar a ${user.firstname} ${user.lastname}?")
            },
            confirmButton = {
                if (!isSelf) {
                    TextButton(onClick = {
                        authViewModel.deleteUser(user.id)
                        showDeleteDialog = null
                    }) { Text("Eliminar") }
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteDialog = null }) {
                    Text(if (isSelf) "Cerrar" else "Cancelar")
                }
            }
        )
    }
}

@Composable
fun UserCard(
    user: UserDto,
    currentUsername: String?,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    val isSelf = user.username == currentUsername

    ElevatedCard(Modifier.fillMaxWidth()) {
        Column(Modifier.padding(12.dp)) {
            Text(
                "${user.firstname} ${user.lastname}" + if (isSelf) " (Tú)" else "",
                style = MaterialTheme.typography.titleMedium
            )
            Text("@${user.username}")
            Text("Rol: ${if (user.role == "ADMIN") "Administrador" else "Usuario"}")

            Spacer(modifier = Modifier.height(8.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedButton(onClick = onEdit) { Text("Editar") }
                Button(
                    onClick = onDelete,
                    enabled = !isSelf,
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
                ) { Text("Eliminar") }
            }
        }
    }
}

@Composable
fun EditUserDialog(
    user: UserDto,
    onDismiss: () -> Unit,
    onConfirm: (String, String, String) -> Unit
) {
    var firstname by remember { mutableStateOf(user.firstname) }
    var lastname by remember { mutableStateOf(user.lastname) }
    var role by remember { mutableStateOf(user.role) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Editar usuario") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                OutlinedTextField(
                    value = firstname,
                    onValueChange = { firstname = it },
                    label = { Text("Nombre") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = lastname,
                    onValueChange = { lastname = it },
                    label = { Text("Apellido") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                Text("Rol:")
                Row(verticalAlignment = Alignment.CenterVertically) {
                    RadioButton(selected = role == "USER", onClick = { role = "USER" })
                    Text("Usuario")
                    Spacer(modifier = Modifier.width(16.dp))
                    RadioButton(selected = role == "ADMIN", onClick = { role = "ADMIN" })
                    Text("Admin")
                }
            }
        },
        confirmButton = {
            TextButton(
                onClick = { onConfirm(firstname, lastname, role) },
                enabled = firstname.isNotBlank() && lastname.isNotBlank()
            ) { Text("Guardar") }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancelar") }
        }
    )
}

