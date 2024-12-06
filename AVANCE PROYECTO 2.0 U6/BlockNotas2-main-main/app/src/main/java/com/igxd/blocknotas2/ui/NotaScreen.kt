package com.igxd.blocknotas2.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.Switch
import androidx.compose.material.SwitchDefaults
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.Alignment
import androidx.lifecycle.viewmodel.compose.viewModel
import com.igxd.BlockNotas.data.models.Nota
import com.igxd.BlockNotas.viewmodel.NotaViewModel

val LightPrimary = Color(0xFF6200EE)
val DarkPrimary = Color(0xFFBB86FC)
val LightBackground = Color(0xFFF5F5F5)
val DarkBackground = Color(0xFF121212)
val LightOnPrimary = Color.Black
val DarkOnPrimary = Color.White

@Composable
fun NotaScreen() {
    val viewModel: NotaViewModel = viewModel()
    val notas by viewModel.notas.observeAsState(listOf())
    var titulo by remember { mutableStateOf("") }
    var contenido by remember { mutableStateOf("") }
    var isDarkTheme by remember { mutableStateOf(false) }
    var notaParaEditar by remember { mutableStateOf<Nota?>(null) }
    var searchQuery by remember { mutableStateOf("") }
    val filteredNotas = if (searchQuery.isBlank()) notas else notas.filter {
        it.titulo.contains(searchQuery, ignoreCase = true) ||
                it.contenido.contains(searchQuery, ignoreCase = true)
    }

    val backgroundColor = if (isDarkTheme) DarkBackground else LightBackground
    val textColor = if (isDarkTheme) DarkOnPrimary else LightOnPrimary

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor)
            .padding(16.dp)
    ) {
        Text(
            text = "Block de Notas  \uD83D\uDCDD",
            style = MaterialTheme.typography.h4.copy(color = textColor),
            modifier = Modifier.padding(bottom = 16.dp)
        )

        Row(verticalAlignment = Alignment.CenterVertically) {
            Text("Tema", style = MaterialTheme.typography.h6.copy(color = textColor))
            Switch(
                checked = isDarkTheme,
                onCheckedChange = { isDarkTheme = it },
                colors = SwitchDefaults.colors(checkedThumbColor = LightPrimary)
            )
        }
        Text(text = if (isDarkTheme) "Modo Oscuro" else "Modo Claro", color = textColor)

        Spacer(modifier = Modifier.height(8.dp))

        // Campo de búsqueda
        TextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            label = { Text("Buscar nota") },
            textStyle = TextStyle(color = textColor),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Campos de texto para título y contenido
        TextField(
            value = titulo,
            onValueChange = { titulo = it },
            label = { Text("Título") },
            textStyle = TextStyle(color = textColor),
            modifier = Modifier.fillMaxWidth()
        )

        TextField(
            value = contenido,
            onValueChange = { contenido = it },
            label = { Text("Contenido") },
            textStyle = TextStyle(color = textColor),
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp)
        )

        Button(
            onClick = {
                if (titulo.isNotBlank() && contenido.isNotBlank()) {
                    if (notaParaEditar == null) {
                        viewModel.agregarNota(Nota(titulo = titulo, contenido = contenido))
                    } else {
                        viewModel.actualizarNota(notaParaEditar!!.copy(titulo = titulo, contenido = contenido))
                        notaParaEditar = null
                    }
                    titulo = ""
                    contenido = ""
                }
            },
            modifier = Modifier.padding(top = 16.dp)
        ) {
            Text(if (notaParaEditar == null) "Agregar Nota" else "Actualizar Nota", color = textColor)
        }

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn {
            items(filteredNotas) { nota ->
                Card(
                    backgroundColor = if (isDarkTheme) DarkBackground else LightBackground,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    elevation = 4.dp
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = nota.titulo,
                            style = MaterialTheme.typography.h6.copy(color = textColor)
                        )
                        Text(
                            text = nota.contenido,
                            style = MaterialTheme.typography.body1.copy(color = textColor)
                        )
                        Row {
                            Button(
                                onClick = {
                                    titulo = nota.titulo
                                    contenido = nota.contenido
                                    notaParaEditar = nota
                                },
                                modifier = Modifier.padding(top = 8.dp)
                            ) {
                                Text("Editar", color = textColor)
                            }
                            Spacer(modifier = Modifier.width(8.dp))
                            Button(
                                onClick = { viewModel.eliminarNota(nota) },
                                modifier = Modifier.padding(top = 8.dp)
                            ) {
                                Text("Eliminar", color = textColor)
                            }
                        }
                    }
                }
            }
        }
    }
}
