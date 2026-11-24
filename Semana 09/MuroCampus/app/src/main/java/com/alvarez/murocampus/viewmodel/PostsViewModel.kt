package com.alvarez.murocampus.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alvarez.murocampus.data.PostRepository
import com.alvarez.murocampus.data.mensajeDe
import com.alvarez.murocampus.model.Post
import com.alvarez.murocampus.model.Comentario
import kotlinx.coroutines.Job
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class DetalleUi(
    val post: Post,
    val comentarios: List<Comentario>
)

class PostsViewModel(
    private val repo: PostRepository
) : ViewModel() {

    private val _lista =
        MutableStateFlow<UiState<List<Post>>>(UiState.Cargando)

    val lista: StateFlow<UiState<List<Post>>> =
        _lista.asStateFlow()

    private val _detalle =
        MutableStateFlow<UiState<DetalleUi>>(UiState.Cargando)

    val detalle: StateFlow<UiState<DetalleUi>> =
        _detalle.asStateFlow()

    private var trabajoDetalle: Job? = null

    init {
        cargar()
    }

    fun cargar() {
        viewModelScope.launch {

            _lista.value = UiState.Cargando

            repo.listar()
                .onSuccess { publicaciones ->
                    _lista.value = UiState.Exito(publicaciones)
                }
                .onFailure { error ->
                    _lista.value = UiState.Error(mensajeDe(error))
                }
        }
    }

    fun abrir(id: Int) {

        trabajoDetalle?.cancel()

        _detalle.value = UiState.Cargando

        trabajoDetalle = viewModelScope.launch {

            try {
                val post = repo.detalle(id).getOrThrow()

                val comentarios =
                    repo.comentarios(id).getOrThrow()

                _detalle.value = UiState.Exito(
                    DetalleUi(
                        post = post,
                        comentarios = comentarios
                    )
                )

            } catch (e: CancellationException) {
                throw e

            } catch (e: Exception) {
                _detalle.value =
                    UiState.Error(mensajeDe(e))
            }
        }
    }

    fun cerrarDetalle() {
        trabajoDetalle?.cancel()
    }
}