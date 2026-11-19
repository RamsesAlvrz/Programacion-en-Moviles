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
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import com.alvarez.campusgps.data.RutaPeatonal
import com.alvarez.campusgps.model.LugarConDistancia
import com.alvarez.campusgps.model.Posicion
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.BoundingBox
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker
import org.osmdroid.views.overlay.Polygon
import org.osmdroid.views.overlay.Polyline
import org.osmdroid.views.overlay.infowindow.InfoWindow

@Composable
fun MapaUbicacion(
    posicion: Posicion?,
    lugares: List<LugarConDistancia>,
    seleccionado: Int?,
    masCercanoId: Int?,
    radioMetros: Int? = null,
    ruta: RutaPeatonal? = null,
    onLugarClick: (Int) -> Unit = {},
    modifier: Modifier = Modifier
) {
    val contexto = LocalContext.current
    val alPulsarLugar = rememberUpdatedState(onLugarClick)

    val azul = Color.rgb(30, 144, 255)
    val rojo = Color.rgb(229, 57, 53)
    val verde = Color.rgb(46, 160, 80)

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

        lienzo.save()
        lienzo.scale(ancho / 34f, alto / 44f)

        val forma = Path().apply {
            moveTo(17f, 42f)
            cubicTo(13f, 35f, 3f, 23f, 3f, 16f)
            cubicTo(3f, 8f, 9f, 2f, 17f, 2f)
            cubicTo(25f, 2f, 31f, 8f, 31f, 16f)
            cubicTo(31f, 23f, 21f, 35f, 17f, 42f)
            close()
        }

        lienzo.drawPath(forma, pintura)

        pintura.color = Color.WHITE
        lienzo.drawCircle(17f, 16f, 6f, pintura)

        lienzo.restore()

        return BitmapDrawable(
            contexto.resources,
            bitmap
        )
    }

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

        val titulo = TextView(contexto).apply {
            text = nombre
            setTextColor(Color.BLACK)
            textSize = 14f
            gravity = Gravity.CENTER
            setTypeface(null, Typeface.BOLD)
        }

        contenedor.addView(titulo)

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
            override fun onOpen(item: Any?) = Unit
            override fun onClose() = Unit
        }
    }

    val marcador = remember(mapa) {
        Marker(mapa).apply {
            title = "Estás aquí"

            setAnchor(
                Marker.ANCHOR_CENTER,
                Marker.ANCHOR_BOTTOM
            )

            icon = crearIconoPin(rojo)
            infoWindow = crearVentana("Estás aquí")

            setOnMarkerClickListener { tocado, vista ->
                InfoWindow.closeAllInfoWindowsOn(vista)
                tocado.showInfoWindow()
                true
            }
        }
    }

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

                icon = crearIconoPin(
                    if (item.lugar.id == masCercanoId) {
                        verde
                    } else {
                        azul
                    }
                )

                infoWindow = crearVentana(
                    nombre = item.lugar.nombre,
                    categoria = item.lugar.categoria
                )

                setOnMarkerClickListener { _, vista ->
                    InfoWindow.closeAllInfoWindowsOn(vista)
                    alPulsarLugar.value(item.lugar.id)
                    true
                }
            }
        }
    }

    // Conservar el comportamiento de centrado original.
    LaunchedEffect(posicion) {
        if (posicion != null && ruta == null) {
            mapa.controller.setZoom(16.0)

            mapa.controller.animateTo(
                GeoPoint(
                    posicion.latitud,
                    posicion.longitud
                )
            )
        }
    }

    LaunchedEffect(seleccionado, marcadoresLugares) {
        if (seleccionado != null && ruta == null) {
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

                InfoWindow.closeAllInfoWindowsOn(mapa)
            }
        }
    }

    // COMMIT 15: mostrar todo el recorrido.
    LaunchedEffect(ruta) {
        if (ruta != null && ruta.puntos.size >= 2) {
            val latitudes = ruta.puntos.map { it.latitud }
            val longitudes = ruta.puntos.map { it.longitud }

            val limites = BoundingBox(
                latitudes.maxOrNull()!!,
                longitudes.maxOrNull()!!,
                latitudes.minOrNull()!!,
                longitudes.minOrNull()!!
            )

            mapa.post {
                mapa.zoomToBoundingBox(
                    limites,
                    true,
                    70
                )
            }
        }
    }

    DisposableEffect(mapa) {
        mapa.onResume()

        onDispose {
            mapa.onPause()
            mapa.onDetach()
        }
    }

    AndroidView(
        factory = { mapa },
        modifier = modifier.fillMaxSize(),
        update = { vista ->

            val ventanaAbierta =
                vista.overlays
                    .filterIsInstance<Marker>()
                    .firstOrNull { it.isInfoWindowShown }

            if (ventanaAbierta != null &&
                ventanaAbierta !== marcador &&
                marcadoresLugares.none {
                    it === ventanaAbierta
                }
            ) {
                InfoWindow.closeAllInfoWindowsOn(vista)
            }

            // Retirar marcadores antiguos.
            val marcadoresAnteriores =
                vista.overlays
                    .filterIsInstance<Marker>()
                    .filter { it !== marcador }

            vista.overlays.removeAll(
                marcadoresAnteriores
            )

            // Retirar circulos antiguos.
            vista.overlays.removeAll(
                vista.overlays.filterIsInstance<Polygon>()
            )

            // Retirar rutas anteriores.
            vista.overlays.removeAll(
                vista.overlays.filterIsInstance<Polyline>()
            )

            if (posicion != null && radioMetros != null) {
                val circulo = Polygon(vista).apply {
                    points = Polygon.pointsAsCircle(
                        GeoPoint(
                            posicion.latitud,
                            posicion.longitud
                        ),
                        radioMetros.toDouble()
                    )

                    fillPaint.color =
                        Color.argb(45, 17, 94, 89)

                    outlinePaint.color =
                        Color.rgb(17, 94, 89)

                    outlinePaint.strokeWidth =
                        3f * contexto.resources
                            .displayMetrics.density
                }

                vista.overlays.add(0, circulo)
            }

            // Dibujar la ruta sobre el circulo
            // y debajo de los marcadores.
            if (ruta != null) {
                val linea = Polyline(vista).apply {
                    setPoints(
                        ruta.puntos.map { punto ->
                            GeoPoint(
                                punto.latitud,
                                punto.longitud
                            )
                        }
                    )

                    outlinePaint.color =
                        Color.rgb(13, 148, 136)

                    outlinePaint.strokeWidth =
                        6f * contexto.resources
                            .displayMetrics.density

                    outlinePaint.isAntiAlias = true
                }

                vista.overlays.add(linea)
            }

            vista.overlays.addAll(marcadoresLugares)

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
