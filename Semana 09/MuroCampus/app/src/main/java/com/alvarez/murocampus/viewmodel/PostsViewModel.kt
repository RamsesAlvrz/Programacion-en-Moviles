
package com.alvarez.murocampus.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alvarez.murocampus.data.PostRepository
import com.alvarez.murocampus.data.mensajeDe
import com.alvarez.murocampus.model.Post
import com.alvarez.murocampus.model.Comentario
import com.alvarez.murocampus.model.NuevoPost
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

    private val _usuarioFiltro =
        MutableStateFlow<Int?>(null)

    val usuarioFiltro: StateFlow<Int?> =
        _usuarioFiltro.asStateFlow()

    private val _publicacion =
        MutableStateFlow<UiState<Post>?>(null)

    val publicacion: StateFlow<UiState<Post>?> =
        _publicacion.asStateFlow()

    // Estado del buscador
    private val _busqueda = MutableStateFlow("")

    val busqueda: StateFlow<String> =
        _busqueda.asStateFlow()

    private var publicacionesOriginales: List<Post> =
        emptyList()

    // Estado de la eliminacion
    private val _eliminacion =
        MutableStateFlow<UiState<Unit>?>(null)

    val eliminacion: StateFlow<UiState<Unit>?> =
        _eliminacion.asStateFlow()

    // IDs eliminados durante la sesion actual
    private val publicacionesEliminadas =
        mutableSetOf<Int>()

    private var trabajoLista: Job? = null

    private var trabajoDetalle: Job? = null

    private var trabajoPublicacion: Job? = null

    init {
        cargar()
    }

    fun cargar() {

        trabajoLista?.cancel()

        val usuario = _usuarioFiltro.value

        _lista.value = UiState.Cargando

        trabajoLista = viewModelScope.launch {

            val resultado = if (usuario == null) {
                repo.listar()
            } else {
                repo.porUsuario(usuario)
            }

            resultado
                .onSuccess { publicaciones ->

                    publicacionesOriginales =
                        publicaciones.filterNot { post ->
                            post.id in publicacionesEliminadas
                        }

                    aplicarBusqueda()
                }
                .onFailure { error ->

                    publicacionesOriginales = emptyList()

                    _lista.value =
                        UiState.Error(mensajeDe(error))
                }
        }
    }

    fun filtrar(userId: Int?) {

        if (_usuarioFiltro.value == userId) {
            return
        }

        _usuarioFiltro.value = userId

        cargar()
    }

    fun buscar(texto: String) {

        _busqueda.value = texto

        if (_lista.value is UiState.Exito) {
            aplicarBusqueda()
        }
    }

    fun limpiarBusqueda() {
        buscar("")
    }

    private fun aplicarBusqueda() {

        val consulta = _busqueda.value.trim()

        val publicacionesFiltradas =
            if (consulta.isBlank()) {

                publicacionesOriginales

            } else {

                publicacionesOriginales.filter { post ->

                    post.title.contains(
                        other = consulta,
                        ignoreCase = true
                    )
                }
            }

        _lista.value =
            UiState.Exito(publicacionesFiltradas)
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

    fun publicar(titulo: String, texto: String) {

        if (titulo.isBlank() || texto.isBlank()) {
            return
        }

        if (_publicacion.value == UiState.Cargando) {
            return
        }

        _publicacion.value = UiState.Cargando

        trabajoPublicacion = viewModelScope.launch {

            val nuevo = NuevoPost(
                userId = 1,
                title = titulo.trim(),
                body = texto.trim()
            )

            repo.crear(nuevo)
                .onSuccess { post ->

                    _publicacion.value =
                        UiState.Exito(post)
                }
                .onFailure { error ->

                    _publicacion.value =
                        UiState.Error(mensajeDe(error))
                }
        }
    }

    fun limpiarPublicacion() {

        trabajoPublicacion?.cancel()

        _publicacion.value = null
    }

    // Eliminar una publicacion mediante el Repository
    fun eliminar(id: Int) {

        if (_eliminacion.value == UiState.Cargando) {
            return
        }

        if (id in publicacionesEliminadas) {
            return
        }

        val detalleActual = _detalle.value

        if (
            detalleActual !is UiState.Exito ||
            detalleActual.datos.post.id != id
        ) {
            return
        }

        _eliminacion.value = UiState.Cargando

        viewModelScope.launch {

            repo.eliminar(id)
                .onSuccess {

                    publicacionesEliminadas.add(id)

                    publicacionesOriginales =
                        publicacionesOriginales.filterNot {
                            it.id == id
                        }

                    if (_lista.value is UiState.Exito) {
                        aplicarBusqueda()
                    }

                    _eliminacion.value =
                        UiState.Exito(Unit)
                }
                .onFailure { error ->

                    _eliminacion.value =
                        UiState.Error(mensajeDe(error))
                }
        }
    }

    fun limpiarEliminacion() {

        if (_eliminacion.value != UiState.Cargando) {

            _eliminacion.value = null
        }
    }
}
