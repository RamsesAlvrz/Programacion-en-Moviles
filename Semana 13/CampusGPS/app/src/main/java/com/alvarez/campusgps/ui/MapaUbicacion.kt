
package com.alvarez.campusgps.ui

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.graphics.Typeface
import android.graphics.drawable.BitmapDrawable
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
    masCercanoId: Int?,
    modifier: Modifier = Modifier
) {
    val contexto = LocalContext.current

    val azul = Color.rgb(30, 144, 255)
    val rojo = Color.rgb(229, 57, 53)
    val verde = Color.rgb(46, 160, 80)

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

    // Dibujar pin con círculo blanco en el centro
    fun crearIconoPin(color: Int): BitmapDrawable {
        val densidad =
            contexto.resources.displayMetrics.density

        val ancho = (34 * densidad).toInt()
        val alto = (44 * densidad).toInt()

        val bitmap = Bitmap.createBitmap(
            ancho,
            alto,
            Bitmap.Config.ARGB_8888
        )

        val lienzo = Canvas(bitmap)

        val pintura = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            this.color = color
            style = Paint.Style.FILL
        }

        val escalaX = ancho / 34f
        val escalaY = alto / 44f

        lienzo.save()
        lienzo.scale(escalaX, escalaY)

        // Cuerpo del marcador
        val forma = Path().apply {
            moveTo(17f, 42f)
            cubicTo(13f, 35f, 3f, 23f, 3f, 16f)
            cubicTo(3f, 8f, 9f, 2f, 17f, 2f)
            cubicTo(25f, 2f, 31f, 8f, 31f, 16f)
            cubicTo(31f, 23f, 21f, 35f, 17f, 42f)
            close()
        }

        lienzo.drawPath(forma, pintura)

        // Bolita blanca central
        pintura.color = Color.WHITE
        lienzo.drawCircle(
            17f,
            16f,
            6f,
            pintura
        )

        lienzo.restore()

        return BitmapDrawable(
            contexto.resources,
            bitmap
        )
    }

    // Ventana de información personalizada
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

        // Nombre en negrita
        val titulo = TextView(contexto).apply {
            text = nombre
            setTextColor(Color.BLACK)
            textSize = 14f
            gravity = Gravity.CENTER
            setTypeface(null, Typeface.BOLD)
        }

        contenedor.addView(titulo)

        // Categoría debajo
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
                // Contenido configurado
            }

            override fun onClose() {
                // Sin acciones adicionales
            }
        }
    }

    // Marcador rojo de ubicación actual
    val marcador = remember(mapa) {
        Marker(mapa).apply {
            title = "Estás aquí"

            setAnchor(
                Marker.ANCHOR_CENTER,
                Marker.ANCHOR_BOTTOM
            )

            icon = crearIconoPin(rojo)
            infoWindow = crearVentana("Estás aquí")

            setOnMarkerClickListener { marcadorTocado, mapaActual ->
                InfoWindow.closeAllInfoWindowsOn(mapaActual)
                marcadorTocado.showInfoWindow()
                true
            }
        }
    }

    // Marcadores del campus
    val marcadoresLugares = remember(
        mapa,
        lugares,
        masCercanoId
    ) {
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

                // Verde para el más cercano
                val colorMarcador =
                    if (item.lugar.id == masCercanoId) {
                        verde
                    } else {
                        azul
                    }

                icon = crearIconoPin(colorMarcador)

                infoWindow = crearVentana(
                    nombre = item.lugar.nombre,
                    categoria = item.lugar.categoria
                )

                // Solo una ventana abierta
                setOnMarkerClickListener { marcadorTocado, mapaActual ->
                    InfoWindow.closeAllInfoWindowsOn(mapaActual)
                    marcadorTocado.showInfoWindow()
                    true
                }
            }
        }
    }

    // Centrar al obtener ubicación
    LaunchedEffect(posicion) {
        if (posicion != null) {
            mapa.controller.setZoom(16.0)

            mapa.controller.animateTo(
                GeoPoint(
                    posicion.latitud,
                    posicion.longitud
                )
            )
        }
    }

    // Mover cámara al lugar seleccionado
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

                val marcadorSeleccionado =
                    marcadoresLugares.firstOrNull {
                        it.title == lugar.nombre
                    }

                // Cerrar ventanas anteriores
                InfoWindow.closeAllInfoWindowsOn(mapa)

                // Mostrar solo la seleccionada
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

            val marcadoresAnteriores =
                vista.overlays
                    .filterIsInstance<Marker>()
                    .filter { it !== marcador }

            // Quitar marcadores anteriores
            vista.overlays.removeAll(
                marcadoresAnteriores
            )

            // Agregar marcadores actualizados
            vista.overlays.addAll(
                marcadoresLugares
            )

            // Marcador rojo
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
