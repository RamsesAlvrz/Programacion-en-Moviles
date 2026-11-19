
package com.alvarez.campusgps.ui

import android.graphics.Color
import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import android.graphics.PorterDuff
import android.graphics.Typeface
import android.graphics.drawable.GradientDrawable
import android.view.Gravity
import android.widget.LinearLayout
import android.widget.TextView
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import com.alvarez.campusgps.model.LugarConDistancia
import com.alvarez.campusgps.model.Posicion
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker
import org.osmdroid.views.overlay.infowindow.InfoWindow

@Composable
fun MapaUbicacion(
    posicion: Posicion?,
    lugares: List<LugarConDistancia>,
    seleccionado: Int?,
    modifier: Modifier = Modifier
) {
    val contexto = LocalContext.current

    // Configuración inicial del mapa
    val mapa = remember(contexto) {
        Configuration.getInstance().userAgentValue =
            contexto.packageName

        MapView(contexto).apply {
            setTileSource(TileSourceFactory.MAPNIK)
            setMultiTouchControls(true)

            controller.setZoom(12.0)
            controller.setCenter(
                GeoPoint(-12.0464, -77.0428)
            )
        }
    }

    // Crear una ventana personalizada
    fun crearVentana(
        nombre: String,
        categoria: String? = null
    ): InfoWindow {

        val densidad =
            contexto.resources.displayMetrics.density

        val contenedor = LinearLayout(contexto).apply {
            orientation = LinearLayout.VERTICAL
            gravity = Gravity.CENTER

            setPadding(
                (12 * densidad).toInt(),
                (8 * densidad).toInt(),
                (12 * densidad).toInt(),
                (8 * densidad).toInt()
            )

            background = GradientDrawable().apply {
                setColor(Color.WHITE)
                cornerRadius = 10 * densidad

                setStroke(
                    (1 * densidad).toInt().coerceAtLeast(1),
                    Color.LTGRAY
                )
            }
        }

        // Nombre centrado y en negrita
        val titulo = TextView(contexto).apply {
            text = nombre
            setTextColor(Color.BLACK)
            textSize = 14f
            gravity = Gravity.CENTER
            setTypeface(null, Typeface.BOLD)
        }

        contenedor.addView(titulo)

        // Categoría centrada debajo del nombre
        if (!categoria.isNullOrBlank()) {
            val subtitulo = TextView(contexto).apply {
                text = categoria
                setTextColor(Color.DKGRAY)
                textSize = 12f
                gravity = Gravity.CENTER
            }

            contenedor.addView(subtitulo)
        }

        return object : InfoWindow(contenedor, mapa) {
            override fun onOpen(item: Any?) {
                // El contenido ya está configurado
            }

            override fun onClose() {
                // Sin acciones adicionales
            }
        }
    }

    // Marcador rojo de la ubicación actual
    val marcador = remember(mapa) {
        Marker(mapa).apply {
            title = "Estás aquí"

            setAnchor(
                Marker.ANCHOR_CENTER,
                Marker.ANCHOR_BOTTOM
            )

            val filtroRojo = ColorMatrix(
                floatArrayOf(
                    0f, 0f, 0f, 0f, 229f,
                    0f, 0f, 0f, 0f, 57f,
                    0f, 0f, 0f, 0f, 53f,
                    0f, 0f, 0f, 1f, 0f
                )
            )

            icon = icon?.constantState
                ?.newDrawable()
                ?.mutate()
                ?.apply {
                    colorFilter =
                        ColorMatrixColorFilter(filtroRojo)
                }

            infoWindow = crearVentana("Estás aquí")

            // Cerrar las otras ventanas al tocar el rojo
            setOnMarkerClickListener { marcadorTocado, mapaActual ->
                InfoWindow.closeAllInfoWindowsOn(mapaActual)
                marcadorTocado.showInfoWindow()
                true
            }
        }
    }

    // Cinco marcadores azules del campus
    val marcadoresLugares = remember(mapa, lugares) {
        lugares.map { item ->
            Marker(mapa).apply {
                position = GeoPoint(
                    item.lugar.latitud,
                    item.lugar.longitud
                )

                title = item.lugar.nombre
                snippet = item.lugar.categoria

                setAnchor(
                    Marker.ANCHOR_CENTER,
                    Marker.ANCHOR_BOTTOM
                )

                // Icono azul independiente
                icon = icon?.constantState
                    ?.newDrawable()
                    ?.mutate()
                    ?.apply {
                        setColorFilter(
                            Color.rgb(30, 144, 255),
                            PorterDuff.Mode.SRC_IN
                        )
                    }

                infoWindow = crearVentana(
                    nombre = item.lugar.nombre,
                    categoria = item.lugar.categoria
                )

                // Cerrar las otras ventanas al tocar un azul
                setOnMarkerClickListener { marcadorTocado, mapaActual ->
                    InfoWindow.closeAllInfoWindowsOn(mapaActual)
                    marcadorTocado.showInfoWindow()
                    true
                }
            }
        }
    }

    // Centrar el mapa al obtener la ubicación
    LaunchedEffect(posicion) {
        if (posicion != null) {
            val punto = GeoPoint(
                posicion.latitud,
                posicion.longitud
            )

            mapa.controller.setZoom(16.0)
            mapa.controller.animateTo(punto)
        }
    }

    // Mover el mapa al lugar seleccionado
    LaunchedEffect(seleccionado, marcadoresLugares) {
        if (seleccionado != null) {

            val lugar = lugares.firstOrNull {
                it.lugar.id == seleccionado
            }?.lugar

            if (lugar != null) {
                mapa.controller.setZoom(17.0)

                mapa.controller.animateTo(
                    GeoPoint(
                        lugar.latitud,
                        lugar.longitud
                    )
                )

                // Buscar el marcador correspondiente
                val marcadorSeleccionado =
                    marcadoresLugares.firstOrNull {
                        it.title == lugar.nombre
                    }

                // Cerrar todas las ventanas anteriores
                InfoWindow.closeAllInfoWindowsOn(mapa)

                // Abrir solo la ventana seleccionada
                marcadorSeleccionado?.showInfoWindow()
            }
        }
    }

    // Ciclo de vida del mapa
    DisposableEffect(mapa) {
        mapa.onResume()

        onDispose {
            mapa.onPause()
            mapa.onDetach()
        }
    }

    // Mostrar mapa y marcadores
    AndroidView(
        factory = { mapa },
        modifier = modifier.fillMaxSize(),
        update = { vista ->

            // Eliminar marcadores azules anteriores
            val marcadoresAnteriores =
                vista.overlays
                    .filterIsInstance<Marker>()
                    .filter { it !== marcador }

            vista.overlays.removeAll(marcadoresAnteriores)

            // Agregar los cinco marcadores azules
            vista.overlays.addAll(marcadoresLugares)

            // Mantener el marcador rojo
            if (posicion != null) {
                marcador.position = GeoPoint(
                    posicion.latitud,
                    posicion.longitud
                )

                if (!vista.overlays.contains(marcador)) {
                    vista.overlays.add(marcador)
                }
            } else {
                vista.overlays.remove(marcador)
            }

            vista.invalidate()
        }
    )
}
