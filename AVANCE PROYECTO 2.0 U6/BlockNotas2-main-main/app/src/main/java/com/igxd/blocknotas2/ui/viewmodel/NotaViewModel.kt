package com.igxd.BlockNotas.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.igxd.BlockNotas.data.models.Nota

class NotaViewModel : ViewModel() {
    private val _notas = MutableLiveData<List<Nota>>(listOf())
    val notas: LiveData<List<Nota>> = _notas

    // Función para agregar una nueva nota
    fun agregarNota(nota: Nota) {
        _notas.value = _notas.value?.plus(nota)
    }

    // Función para eliminar una nota
    fun eliminarNota(nota: Nota) {
        _notas.value = _notas.value?.filterNot { it == nota }
    }

    // Función para actualizar una nota
    fun actualizarNota(nota: Nota) {
        _notas.value = _notas.value?.map {
            if (it.id == nota.id) {
                it.copy(titulo = nota.titulo, contenido = nota.contenido)
            } else {
                it
            }
        }
    }
}
