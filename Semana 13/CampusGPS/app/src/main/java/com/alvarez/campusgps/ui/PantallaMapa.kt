package com.alvarez.campusgps.ui

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.alvarez.campusgps.model.EstadoRuta
import com.alvarez.campusgps.model.UbicacionUi
import com.alvarez.campusgps.util.formatearDistancia
import com.alvarez.campusgps.viewmodel.MapaViewModel

@Composable
fun PantallaMapa(
    vm: MapaViewModel,
    mostrarAyudaRuta: Boolean = false,
    onCerrarAyudaRuta: () -> Unit = {}
) {
    val estado by vm.estado.collectAsStateWithLifecycle()
    val lugares by vm.lugaresFiltrados.collectAsStateWithLifecycle()
    val categoria by vm.categoriaSeleccionada.collectAsStateWithLifecycle()
    val radio by vm.radioSeleccionado.collectAsStateWithLifecycle()
    val seleccionado by vm.seleccionado.collectAsStateWithLifecycle()
    val masCercano by vm.masCercanoVisible.collectAsStateWithLifecycle()
    val estadoRuta by vm.estadoRuta.collectAsStateWithLifecycle()

    val contexto = LocalContext.current
    val colores = MaterialTheme.colorScheme

    var mostrarDetalle by remember {
        mutableStateOf(false)
    }

    // Solo presentación: lista plegable y mapa ampliado.
    var listaDesplegada by remember { mutableStateOf(false) }
    var mapaAmpliado by remember { mutableStateOf(false) }

    // Solicitudes de centrado en el GPS.
    var solicitudCentrado by remember {
        mutableIntStateOf(0)
    }

    val lugarSeleccionado = lugares.firstOrNull {
        it.lugar.id == seleccionado
    }

    LaunchedEffect(seleccionado, lugares) {
        if (mostrarDetalle && lugarSeleccionado == null) {
            mostrarDetalle = false
        }
    }

    fun abrirDetalle(id: Int) {
        onCerrarAyudaRuta()
        vm.seleccionar(id)
        mostrarDetalle = true
    }

    val rutaActual =
        (estadoRuta as? EstadoRuta.Lista)?.ruta

    val permisos = arrayOf(
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION
    )

    val lanzador = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { resultado ->
        if (resultado.values.any { it }) {
            solicitudCentrado++
            vm.buscar()
        } else {
            vm.permisoDenegado()
        }
    }

    fun solicitarUbicacion() {
        val tienePermiso = permisos.any { permiso ->
            ContextCompat.checkSelfPermission(
                contexto,
                permiso
            ) == PackageManager.PERMISSION_GRANTED
        }

        if (tienePermiso) {
            solicitudCentrado++
            vm.buscar()
        } else {
            lanzador.launch(permisos)
        }
    }

    val posicion =
        (estado as? UbicacionUi.Lista)?.posicion

    val error =
        estado as? UbicacionUi.Error

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(colores.background)
    ) {
        Surface(
            color = colores.primary,
            shape = RoundedCornerShape(
                bottomStart = 24.dp,
                bottomEnd = 24.dp
            ),
            shadowElevation = 4.dp
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        horizontal = 20.dp,
                        vertical = 18.dp
                    )
            ) {
                Text(
                    text = "CampusGPS",
                    fontSize = 26.sp,
                    fontWeight = FontWeight.Bold,
                    color = colores.onPrimary
                )

                Spacer(Modifier.height(3.dp))

                Text(
                    text = "Explora los lugares de tu campus",
                    fontSize = 13.sp,
                    color = colores.onPrimary.copy(alpha = 0.85f)
                )
            }
        }

        if (error != null) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text = "No pudimos mostrar tu ubicación",
                    style = MaterialTheme.typography.titleMedium,
                    color = colores.onBackground,
                    fontWeight = FontWeight.SemiBold
                )

                Button(
                    onClick = { solicitarUbicacion() },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp)
                ) {
                    Text("Obtener mi ubicación")
                }

                MensajeError(
                    error = error,
                    onReintentar = { solicitarUbicacion() }
                )
            }
        } else {

            if (!mapaAmpliado) {
                // Filtros de categoría.
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp)
                ) {
                    Text(
                        text = "Explorar por categoría",
                        modifier = Modifier.padding(
                            horizontal = 18.dp,
                            vertical = 3.dp
                        ),
                        style = MaterialTheme.typography.labelLarge,
                        color = colores.onBackground,
                        fontWeight = FontWeight.SemiBold
                    )

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .horizontalScroll(rememberScrollState())
                            .padding(
                                horizontal = 14.dp,
                                vertical = 2.dp
                            ),
                        horizontalArrangement =
                            Arrangement.spacedBy(8.dp)
                    ) {
                        MapaViewModel.CATEGORIAS.forEach { opcion ->
                            FilterChip(
                                selected = categoria == opcion,
                                onClick = {
                                    vm.seleccionarCategoria(opcion)
                                },
                                label = {
                                    Text(
                                        text = opcion,
                                        fontSize = 12.sp
                                    )
                                },
                                shape = RoundedCornerShape(14.dp),
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = colores.primary,
                                    selectedLabelColor = colores.onPrimary,
                                    containerColor = colores.surface,
                                    labelColor = colores.onSurface
                                )
                            )
                        }
                    }
                }

                // Filtros de proximidad.
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 2.dp)
                ) {
                    Text(
                        text = "Radio de proximidad",
                        modifier = Modifier.padding(
                            horizontal = 18.dp,
                            vertical = 3.dp
                        ),
                        style = MaterialTheme.typography.labelLarge,
                        color = colores.onBackground,
                        fontWeight = FontWeight.SemiBold
                    )

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .horizontalScroll(rememberScrollState())
                            .padding(
                                horizontal = 14.dp,
                                vertical = 2.dp
                            ),
                        horizontalArrangement =
                            Arrangement.spacedBy(8.dp)
                    ) {
                        FilterChip(
                            selected = radio == null,
                            onClick = {
                                vm.seleccionarRadio(null)
                            },
                            label = {
                                Text(
                                    text = "Sin límite",
                                    fontSize = 12.sp
                                )
                            },
                            shape = RoundedCornerShape(14.dp),
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = colores.primary,
                                selectedLabelColor = colores.onPrimary
                            )
                        )

                        MapaViewModel.RADIOS.forEach { opcion ->
                            FilterChip(
                                selected = radio == opcion,
                                onClick = {
                                    vm.seleccionarRadio(opcion)
                                },
                                label = {
                                    Text(
                                        text = "$opcion m",
                                        fontSize = 12.sp
                                    )
                                },
                                shape = RoundedCornerShape(14.dp),
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = colores.primary,
                                    selectedLabelColor = colores.onPrimary
                                )
                            )
                        }
                    }
                }

            } // Fin de filtros, ocultos únicamente en modo mapa ampliado.

            // Mapa principal.
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(
                        start = 12.dp,
                        end = 12.dp,
                        top = 6.dp
                    ),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(
                    containerColor = colores.surface
                ),
                elevation = CardDefaults.cardElevation(
                    defaultElevation = 3.dp
                )
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(RoundedCornerShape(20.dp))
                ) {
                    MapaUbicacion(
                        posicion = posicion,
                        lugares = lugares,
                        seleccionado = seleccionado,
                        masCercanoId = masCercano?.lugar?.id,
                        radioMetros = radio,
                        ruta = rutaActual,
                        solicitudCentrado = solicitudCentrado,
                        onLugarClick = { id ->
                            abrirDetalle(id)
                        },
                        modifier = Modifier.fillMaxSize()
                    )

                    Surface(
                        modifier = Modifier
                            .align(Alignment.TopStart)
                            .padding(12.dp),
                        shape = RoundedCornerShape(12.dp),
                        color = colores.surface.copy(alpha = 0.95f),
                        shadowElevation = 2.dp
                    ) {
                        Text(
                            text = if (rutaActual != null) {
                                "RUTA PEATONAL"
                            } else {
                                "MAPA DEL CAMPUS"
                            },
                            modifier = Modifier.padding(
                                horizontal = 12.dp,
                                vertical = 8.dp
                            ),
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            color = colores.primary
                        )
                    }

                    // Control visual del mapa: no modifica el GPS ni las rutas.
                    FilledTonalIconButton(
                        onClick = { mapaAmpliado = !mapaAmpliado },
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(10.dp),
                        colors = IconButtonDefaults.filledTonalIconButtonColors(
                            containerColor = colores.surface.copy(alpha = 0.96f),
                            contentColor = colores.primary
                        )
                    ) {
                        Text(
                            text = if (mapaAmpliado) "↙" else "⛶",
                            fontSize = 22.sp
                        )
                    }
                }
            }

            if (!mapaAmpliado) {
                // Ayuda debajo del mapa.
                if (mostrarAyudaRuta) {
                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(
                                start = 16.dp,
                                end = 16.dp,
                                top = 9.dp
                            ),
                        color = colores.primaryContainer,
                        shape = RoundedCornerShape(15.dp),
                        shadowElevation = 2.dp
                    ) {
                        Row(
                            modifier = Modifier.padding(
                                start = 13.dp,
                                end = 5.dp,
                                top = 7.dp,
                                bottom = 7.dp
                            ),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Text(
                                text = "↝",
                                color = colores.primary,
                                fontSize = 24.sp,
                                fontWeight = FontWeight.Bold
                            )

                            Column(
                                modifier = Modifier.weight(1f),
                                verticalArrangement =
                                    Arrangement.spacedBy(3.dp)
                            ) {
                                Text(
                                    text = "Ruta a pie",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 13.sp,
                                    color = colores.onPrimaryContainer
                                )

                                Text(
                                    text = "Selecciona un lugar y pulsa " +
                                            "\"Cómo llegar\".",
                                    fontSize = 11.sp,
                                    lineHeight = 15.sp,
                                    color = colores.onPrimaryContainer
                                )
                            }

                            TextButton(
                                onClick = onCerrarAyudaRuta,
                                contentPadding = PaddingValues(
                                    horizontal = 8.dp,
                                    vertical = 6.dp
                                )
                            ) {
                                Text(
                                    text = "Cerrar",
                                    fontSize = 11.sp
                                )
                            }
                        }
                    }
                }

                // Botones y resultados.
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            horizontal = 16.dp,
                            vertical = 7.dp
                        ),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Row(
                        horizontalArrangement =
                            Arrangement.spacedBy(10.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Button(
                            onClick = { solicitarUbicacion() },
                            enabled = estado !is UbicacionUi.Buscando,
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(14.dp),
                            contentPadding = PaddingValues(
                                horizontal = 8.dp,
                                vertical = 10.dp
                            )
                        ) {
                            Text(
                                text = "Mi ubicación",
                                fontSize = 12.sp
                            )
                        }

                        OutlinedButton(
                            onClick = {
                                masCercano?.let {
                                    abrirDetalle(it.lugar.id)
                                }
                            },
                            enabled = masCercano != null &&
                                    estado is UbicacionUi.Lista,
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(14.dp),
                            contentPadding = PaddingValues(
                                horizontal = 8.dp,
                                vertical = 10.dp
                            )
                        ) {
                            Text(
                                text = "Más cercano",
                                fontSize = 12.sp
                            )
                        }
                    }

                    if (estado is UbicacionUi.Buscando) {
                        LinearProgressIndicator(
                            modifier = Modifier.fillMaxWidth()
                        )

                        Text(
                            text = "Buscando tu ubicación...",
                            fontSize = 12.sp,
                            color = colores.onSurfaceVariant
                        )
                    }

                    if (radio != null && posicion == null) {
                        Text(
                            text = "Obtén tu ubicación para aplicar el radio.",
                            fontSize = 11.sp,
                            color = colores.onSurfaceVariant
                        )
                    }

                    when (val resultado = estadoRuta) {
                        EstadoRuta.Cargando -> {
                            LinearProgressIndicator(
                                modifier = Modifier.fillMaxWidth()
                            )

                            Text(
                                "Calculando ruta peatonal...",
                                fontSize = 12.sp,
                                color = colores.primary
                            )
                        }

                        is EstadoRuta.Lista -> {
                            Surface(
                                color = colores.primaryContainer,
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(10.dp),
                                    verticalArrangement = Arrangement.spacedBy(6.dp)
                                ) {
                                    Text(
                                        text = "Ruta a ${resultado.destinoNombre} · " +
                                                formatearDistancia(
                                                    resultado.ruta.distanciaMetros.toFloat()
                                                ),
                                        fontSize = 12.sp,
                                        color = colores.onPrimaryContainer
                                    )

                                    // OSRM puede terminar en un camino cercano,
                                    // no necesariamente en el marcador del edificio.
                                    val separacion =
                                        resultado.ruta.separacionDestinoMetros

                                    if (separacion > 20.0) {
                                        Text(
                                            text = "La ruta termina a unos " +
                                                    "${separacion.toInt()} m del destino. " +
                                                    "Puede faltar un camino interior " +
                                                    "en OpenStreetMap.",
                                            fontSize = 12.sp,
                                            color = colores.error
                                        )
                                    }
                                }
                            }
                        }

                        is EstadoRuta.Error -> {
                            Text(
                                text = resultado.mensaje,
                                fontSize = 12.sp,
                                color = colores.error
                            )
                        }

                        EstadoRuta.SinRuta -> Unit
                    }

                    if (estadoRuta is EstadoRuta.SinRuta) {
                        masCercano?.let { item ->
                            Surface(
                                color = colores.primaryContainer,
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Text(
                                    text = "Más cercano: ${item.lugar.nombre} · " +
                                            (item.metros?.let {
                                                formatearDistancia(it)
                                            } ?: "Sin calcular"),
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(10.dp),
                                    color = colores.onPrimaryContainer,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Medium
                                )
                            }
                        }
                    }
                }

                // Lista inferior de lugares.
                Surface(
                    color = colores.surface,
                    shape = RoundedCornerShape(
                        topStart = 22.dp,
                        topEnd = 22.dp
                    ),
                    shadowElevation = 5.dp
                ) {
                    Column {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { listaDesplegada = !listaDesplegada }
                                .padding(
                                    start = 18.dp,
                                    end = 18.dp,
                                    top = 12.dp,
                                    bottom = 12.dp
                                ),
                            horizontalArrangement =
                                Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Lugares del campus",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )

                            Text(
                                text = "${lugares.size} lugares  " +
                                        if (listaDesplegada) "⌃ Ocultar" else "⌄ Mostrar",
                                color = colores.onSurfaceVariant,
                                fontSize = 12.sp
                            )
                        }

                        if (listaDesplegada && lugares.isEmpty()) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(90.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = if (radio != null) {
                                        "No hay lugares dentro de $radio m"
                                    } else {
                                        "No hay lugares en esta categoría"
                                    },
                                    color = colores.onSurfaceVariant,
                                    style = MaterialTheme.typography.bodyMedium
                                )
                            }
                        } else if (listaDesplegada) {
                            ListaLugares(
                                lugares = lugares,
                                seleccionado = seleccionado,
                                masCercanoId = masCercano?.lugar?.id,
                                onClick = { id ->
                                    abrirDetalle(id)
                                },
                                modifier = Modifier.height(170.dp)
                            )
                        }

                        if (listaDesplegada) {
                            Spacer(Modifier.height(6.dp))
                        }
                    }
                }
            } // Fin de controles y lista ocultos en modo mapa ampliado.
        }
    }

    if (mostrarDetalle && lugarSeleccionado != null) {
        DetalleLugarSheet(
            item = lugarSeleccionado,
            estadoRuta = estadoRuta,
            tieneUbicacion = posicion != null,
            onCerrar = {
                mostrarDetalle = false
            },
            onComoLlegar = {
                onCerrarAyudaRuta()
                vm.calcularRuta()
                mostrarDetalle = false
            },
            onLimpiarRuta = {
                vm.limpiarRuta()
            }
        )
    }
}
