package com.example.interprac.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.interprac.data.remote.dto.UserDto
import com.example.interprac.data.uiState.UiState
import com.example.interprac.ui.viewmodel.AuthViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminScreen(
    authViewModel: AuthViewModel
) {
    val usersState by authViewModel.usersState.collectAsState()
    var showEditDialog by remember { mutableStateOf<UserDto?>(null) }
    var showDeleteDialog by remember { mutableStateOf<UserDto?>(null) }

    LaunchedEffect(Unit) {
        authViewModel.loadUsers()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("👑 Administración") }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            // Offline warning
            if (!authViewModel.isOnline) {
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.errorContainer
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            Icons.Default.WifiOff,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onErrorContainer
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            "Sin conexión - No se pueden gestionar usuarios",
                            color = MaterialTheme.colorScheme.onErrorContainer
                        )
                    }
                }
            }

            when (val state = usersState) {
                is UiState.Loading -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }

                is UiState.Error -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(
                                Icons.Default.Error,
                                contentDescription = null,
                                modifier = Modifier.size(48.dp),
                                tint = MaterialTheme.colorScheme.error
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(state.message, color = MaterialTheme.colorScheme.error)
                            Spacer(modifier = Modifier.height(16.dp))
                            Button(onClick = { authViewModel.loadUsers() }) {
                                Text("Reintentar")
                            }
                        }
                    }
                }

                is UiState.Success -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        item {
                            Text(
                                text = "Usuarios registrados (${state.data.size})",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                        }

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

                is UiState.Idle -> {
                    // Initial state
                }
            }
        }
    }

    // Edit dialog
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

    // Delete dialog
    showDeleteDialog?.let { user ->
        val isSelf = user.username == authViewModel.currentUsername

        AlertDialog(
            onDismissRequest = { showDeleteDialog = null },
            title = { Text("Eliminar usuario") },
            text = {
                if (isSelf) {
                    Text("No puedes eliminarte a ti mismo.")
                } else {
                    Text("¿Estás seguro de que quieres eliminar a \"${user.firstname} ${user.lastname}\"?")
                }
            },
            confirmButton = {
                if (!isSelf) {
                    TextButton(
                        onClick = {
                            authViewModel.deleteUser(user.id)
                            showDeleteDialog = null
                        },
                        colors = ButtonDefaults.textButtonColors(
                            contentColor = MaterialTheme.colorScheme.error
                        )
                    ) {
                        Text("Eliminar")
                    }
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
    val isAdmin = user.role == "ADMIN"

    ElevatedCard(
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Avatar
            Surface(
                modifier = Modifier.size(48.dp),
                shape = MaterialTheme.shapes.medium,
                color = if (isAdmin) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.secondaryContainer
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = if (isAdmin) Icons.Default.AdminPanelSettings else Icons.Default.Person,
                        contentDescription = null,
                        tint = if (isAdmin) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onSecondaryContainer
                    )
                }
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "${user.firstname} ${user.lastname}",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    if (isSelf) {
                        Spacer(modifier = Modifier.width(8.dp))
                        AssistChip(
                            onClick = {},
                            label = { Text("Tú", style = MaterialTheme.typography.labelSmall) },
                            modifier = Modifier.height(24.dp)
                        )
                    }
                }
                Text(
                    text = "@${user.username}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = if (isAdmin) "Administrador" else "Usuario",
                    style = MaterialTheme.typography.labelSmall,
                    color = if (isAdmin) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            // Actions
            IconButton(onClick = onEdit) {
                Icon(Icons.Default.Edit, contentDescription = "Editar")
            }

            IconButton(
                onClick = onDelete,
                enabled = !isSelf
            ) {
                Icon(
                    Icons.Default.Delete,
                    contentDescription = "Eliminar",
                    tint = if (isSelf) MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f) else MaterialTheme.colorScheme.error
                )
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

                Text("Rol:", style = MaterialTheme.typography.labelMedium)
                Row(verticalAlignment = Alignment.CenterVertically) {
                    RadioButton(
                        selected = role == "USER",
                        onClick = { role = "USER" }
                    )
                    Text("Usuario")

                    Spacer(modifier = Modifier.width(16.dp))

                    RadioButton(
                        selected = role == "ADMIN",
                        onClick = { role = "ADMIN" }
                    )
                    Text("Administrador")
                }
            }
        },
        confirmButton = {
            TextButton(
                onClick = { onConfirm(firstname, lastname, role) },
                enabled = firstname.isNotBlank() && lastname.isNotBlank()
            ) {
                Text("Guardar")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar")
            }
        }
    )
}

