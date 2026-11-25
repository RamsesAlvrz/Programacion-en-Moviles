
package com.alvarez.murocampus

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.compose.BackHandler
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.alvarez.murocampus.data.PostRepositoryRemote
import com.alvarez.murocampus.data.remote.RetrofitClient
import com.alvarez.murocampus.ui.PantallaLista
import com.alvarez.murocampus.ui.PantallaDetalle
import com.alvarez.murocampus.ui.PantallaCrear
import com.alvarez.murocampus.ui.PantallaEditar
import com.alvarez.murocampus.ui.theme.*
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

                val enInicio =
                    idSeleccionado == null &&
                            !mostrarCrear &&
                            !mostrarEditar

                val ocupado =
                    eliminacion == UiState.Cargando ||
                            edicion == UiState.Cargando

                // Recuperar el detalle
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

                // Confirmacion de eliminacion
                LaunchedEffect(eliminacion) {

                    if (eliminacion is UiState.Exito) {

                        vm.cerrarDetalle()

                        mostrarEditar = false

                        idSeleccionado = null

                        scope.launch {

                            snackbarHostState.showSnackbar(
                                message =
                                    "Publicación eliminada correctamente",
                                duration = SnackbarDuration.Short
                            )
                        }

                        vm.limpiarEliminacion()
                    }
                }

                // Confirmacion de edicion
                LaunchedEffect(edicion) {

                    if (edicion is UiState.Exito) {

                        mostrarEditar = false

                        scope.launch {

                            snackbarHostState.showSnackbar(
                                message =
                                    "Publicación actualizada correctamente",
                                duration = SnackbarDuration.Short
                            )
                        }

                        vm.limpiarEdicion()
                    }
                }

                // Navegacion compartida
                val volver: () -> Unit = {

                    if (!ocupado) {

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

                BackHandler(
                    enabled = !enInicio,
                    onBack = volver
                )

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
                                modifier = Modifier.padding(8.dp),
                                shape = RoundedCornerShape(16.dp),
                                containerColor = VerdeExito,
                                contentColor = Blanco
                            )
                        }
                    },

                    topBar = {

                        TopAppBar(

                            navigationIcon = {

                                if (!enInicio) {

                                    IconButton(
                                        onClick = volver,
                                        enabled = !ocupado,
                                        modifier = Modifier.semantics {
                                            contentDescription =
                                                "Regresar"
                                        }
                                    ) {

                                        IconoRegresar(
                                            color = if (ocupado) {
                                                Blanco.copy(alpha = 0.4f)
                                            } else {
                                                Blanco
                                            },
                                            modifier =
                                                Modifier.size(24.dp)
                                        )
                                    }

                                } else {

                                    Surface(
                                        modifier = Modifier
                                            .padding(
                                                start = 12.dp,
                                                end = 10.dp
                                            )
                                            .size(42.dp),
                                        color = Blanco,
                                        shape =
                                            RoundedCornerShape(13.dp)
                                    ) {

                                        Box(
                                            contentAlignment =
                                                Alignment.Center
                                        ) {

                                            IconoCampus(
                                                color = AzulOscuro,
                                                modifier =
                                                    Modifier.size(28.dp)
                                            )
                                        }
                                    }
                                }
                            },

                            title = {

                                Column {

                                    Text(
                                        text = when {

                                            mostrarEditar ->
                                                "Editar publicación"

                                            mostrarCrear ->
                                                "Nueva publicación"

                                            idSeleccionado != null ->
                                                "Detalle"

                                            else ->
                                                "MuroCampus"
                                        },
                                        color = Blanco,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 21.sp
                                    )

                                    Text(
                                        text = when {

                                            mostrarEditar ->
                                                "Actualiza tu contenido"

                                            mostrarCrear ->
                                                "Comparte con la comunidad"

                                            idSeleccionado != null ->
                                                "Publicación #$idSeleccionado"

                                            else ->
                                                "Comunidad universitaria"
                                        },
                                        color = Color(0xFFC7DDEB),
                                        fontSize = 12.sp
                                    )
                                }
                            },

                            colors =
                                TopAppBarDefaults.topAppBarColors(
                                    containerColor = AzulOscuro,
                                    titleContentColor = Blanco,
                                    navigationIconContentColor = Blanco
                                )
                        )
                    },

                    floatingActionButton = {

                        if (enInicio) {

                            ExtendedFloatingActionButton(

                                onClick = {

                                    vm.limpiarPublicacion()

                                    mostrarCrear = true
                                },

                                icon = {

                                    IconoAgregar(
                                        color = Blanco,
                                        modifier =
                                            Modifier.size(23.dp)
                                    )
                                },

                                text = {

                                    Text(
                                        text = "Publicar",
                                        fontWeight =
                                            FontWeight.SemiBold
                                    )
                                },

                                shape = RoundedCornerShape(18.dp),

                                containerColor = AzulOscuro,

                                contentColor = Blanco
                            )
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
                                onCancelar = volver
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

// Icono de identidad universitaria
@Composable
private fun IconoCampus(
    color: Color,
    modifier: Modifier = Modifier
) {

    Canvas(modifier = modifier) {

        val w = size.width
        val h = size.height
        val grosor = size.minDimension * 0.065f

        val birrete = Path().apply {

            moveTo(w * 0.08f, h * 0.32f)

            lineTo(w * 0.50f, h * 0.10f)

            lineTo(w * 0.92f, h * 0.32f)

            lineTo(w * 0.50f, h * 0.55f)

            close()
        }

        drawPath(
            path = birrete,
            color = color
        )

        val base = Path().apply {

            moveTo(w * 0.25f, h * 0.48f)

            lineTo(w * 0.25f, h * 0.69f)

            quadraticBezierTo(
                w * 0.50f,
                h * 0.88f,
                w * 0.75f,
                h * 0.69f
            )

            lineTo(w * 0.75f, h * 0.48f)
        }

        drawPath(
            path = base,
            color = color,
            style = Stroke(
                width = grosor,
                cap = StrokeCap.Round
            )
        )

        drawLine(
            color = color,
            start = Offset(w * 0.89f, h * 0.35f),
            end = Offset(w * 0.89f, h * 0.76f),
            strokeWidth = grosor,
            cap = StrokeCap.Round
        )
    }
}

// Icono para regresar
@Composable
private fun IconoRegresar(
    color: Color,
    modifier: Modifier = Modifier
) {

    Canvas(modifier = modifier) {

        val w = size.width
        val h = size.height
        val grosor = size.minDimension * 0.09f

        drawLine(
            color = color,
            start = Offset(w * 0.18f, h * 0.50f),
            end = Offset(w * 0.85f, h * 0.50f),
            strokeWidth = grosor,
            cap = StrokeCap.Round
        )

        drawLine(
            color = color,
            start = Offset(w * 0.18f, h * 0.50f),
            end = Offset(w * 0.48f, h * 0.20f),
            strokeWidth = grosor,
            cap = StrokeCap.Round
        )

        drawLine(
            color = color,
            start = Offset(w * 0.18f, h * 0.50f),
            end = Offset(w * 0.48f, h * 0.80f),
            strokeWidth = grosor,
            cap = StrokeCap.Round
        )
    }
}

// Icono para publicar
@Composable
private fun IconoAgregar(
    color: Color,
    modifier: Modifier = Modifier
) {

    Canvas(modifier = modifier) {

        val w = size.width
        val h = size.height
        val grosor = size.minDimension * 0.10f

        drawLine(
            color = color,
            start = Offset(w * 0.50f, h * 0.15f),
            end = Offset(w * 0.50f, h * 0.85f),
            strokeWidth = grosor,
            cap = StrokeCap.Round
        )

        drawLine(
            color = color,
            start = Offset(w * 0.15f, h * 0.50f),
            end = Offset(w * 0.85f, h * 0.50f),
            strokeWidth = grosor,
            cap = StrokeCap.Round
        )
    }
}
