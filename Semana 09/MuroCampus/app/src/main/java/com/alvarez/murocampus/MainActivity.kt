
package com.alvarez.murocampus

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.compose.BackHandler
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.alvarez.murocampus.data.PostRepositoryRemote
import com.alvarez.murocampus.data.remote.RetrofitClient
import com.alvarez.murocampus.ui.PantallaLista
import com.alvarez.murocampus.ui.PantallaDetalle
import com.alvarez.murocampus.ui.PantallaCrear
import com.alvarez.murocampus.ui.PantallaEditar
import com.alvarez.murocampus.ui.theme.MuroCampusTheme
import com.alvarez.murocampus.viewmodel.PostsViewModel
import com.alvarez.murocampus.viewmodel.PostsViewModelFactory
import com.alvarez.murocampus.viewmodel.UiState
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    private val vm: PostsViewModel by viewModels {

        PostsViewModelFactory(
            PostRepositoryRemote(RetrofitClient.api)
        )
    }

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        setContent {

            MuroCampusTheme {

                var idSeleccionado by rememberSaveable {
                    mutableStateOf<Int?>(null)
                }

                var mostrarCrear by rememberSaveable {
                    mutableStateOf(false)
                }

                var mostrarEditar by rememberSaveable {
                    mutableStateOf(false)
                }

                val eliminacion by
                vm.eliminacion.collectAsStateWithLifecycle()

                val edicion by
                vm.edicion.collectAsStateWithLifecycle()

                val detalle by
                vm.detalle.collectAsStateWithLifecycle()

                val snackbarHostState = remember {
                    SnackbarHostState()
                }

                val scope = rememberCoroutineScope()

                // Recuperar el detalle si se recrea el proceso
                LaunchedEffect(idSeleccionado) {

                    val id = idSeleccionado

                    if (id != null) {

                        val actual = vm.detalle.value

                        if (
                            actual is UiState.Exito &&
                            actual.datos.post.id != id
                        ) {
                            vm.abrir(id)
                        } else if (
                            actual == UiState.Cargando
                        ) {
                            vm.abrir(id)
                        }
                    }
                }

                // Eliminacion exitosa
                LaunchedEffect(eliminacion) {

                    if (eliminacion is UiState.Exito) {

                        vm.cerrarDetalle()

                        mostrarEditar = false

                        idSeleccionado = null

                        scope.launch {

                            snackbarHostState.showSnackbar(
                                message = "Publicación eliminada correctamente",
                                duration = SnackbarDuration.Short
                            )
                        }

                        vm.limpiarEliminacion()
                    }
                }

                // Edicion exitosa
                LaunchedEffect(edicion) {

                    if (edicion is UiState.Exito) {

                        mostrarEditar = false

                        scope.launch {

                            snackbarHostState.showSnackbar(
                                message = "Publicación actualizada correctamente",
                                duration = SnackbarDuration.Short
                            )
                        }

                        vm.limpiarEdicion()
                    }
                }

                BackHandler(
                    enabled = idSeleccionado != null ||
                            mostrarCrear ||
                            mostrarEditar
                ) {

                    if (
                        eliminacion != UiState.Cargando &&
                        edicion != UiState.Cargando
                    ) {

                        when {

                            mostrarEditar -> {

                                vm.limpiarEdicion()

                                mostrarEditar = false
                            }

                            mostrarCrear -> {

                                vm.limpiarPublicacion()

                                mostrarCrear = false
                            }

                            else -> {

                                vm.cerrarDetalle()

                                vm.limpiarEliminacion()

                                vm.limpiarEdicion()

                                idSeleccionado = null
                            }
                        }
                    }
                }

                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    containerColor =
                        MaterialTheme.colorScheme.background,

                    snackbarHost = {

                        SnackbarHost(
                            hostState = snackbarHostState
                        ) { datos ->

                            Snackbar(
                                snackbarData = datos,
                                containerColor = Color(0xFF287D46),
                                contentColor = Color.White
                            )
                        }
                    },

                    topBar = {

                        TopAppBar(
                            title = {

                                Text(
                                    text = if (mostrarEditar) {
                                        "Editar publicación"
                                    } else {
                                        "MuroCampus"
                                    },
                                    style = MaterialTheme.typography.titleLarge
                                )
                            },
                            colors = TopAppBarDefaults.topAppBarColors(
                                containerColor =
                                    MaterialTheme.colorScheme.primary,
                                titleContentColor =
                                    MaterialTheme.colorScheme.onPrimary
                            )
                        )
                    },

                    floatingActionButton = {

                        if (
                            idSeleccionado == null &&
                            !mostrarCrear &&
                            !mostrarEditar
                        ) {

                            FloatingActionButton(
                                onClick = {

                                    vm.limpiarPublicacion()

                                    mostrarCrear = true
                                },
                                containerColor =
                                    MaterialTheme.colorScheme.primary,
                                contentColor =
                                    MaterialTheme.colorScheme.onPrimary
                            ) {

                                Text(
                                    text = "+",
                                    fontSize = 30.sp
                                )
                            }
                        }
                    }

                ) { innerPadding ->

                    val id = idSeleccionado

                    val detalleActual = detalle

                    when {

                        mostrarEditar &&
                                detalleActual is UiState.Exito -> {

                            PantallaEditar(
                                vm = vm,
                                post = detalleActual.datos.post,
                                modifier = Modifier.padding(
                                    innerPadding
                                ),
                                onCancelar = {

                                    vm.limpiarEdicion()

                                    mostrarEditar = false
                                }
                            )
                        }

                        mostrarCrear -> {

                            PantallaCrear(
                                vm = vm,
                                modifier = Modifier.padding(
                                    innerPadding
                                )
                            )
                        }

                        id != null -> {

                            PantallaDetalle(
                                vm = vm,
                                id = id,
                                modifier = Modifier.padding(
                                    innerPadding
                                ),
                                onEditar = {

                                    vm.limpiarEdicion()

                                    mostrarEditar = true
                                }
                            )
                        }

                        else -> {

                            PantallaLista(
                                vm = vm,
                                modifier = Modifier.padding(
                                    innerPadding
                                ),
                                onVerDetalle = { postId ->

                                    vm.limpiarEliminacion()

                                    vm.limpiarEdicion()

                                    idSeleccionado = postId

                                    vm.abrir(postId)
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}
