
package com.alvarez.campusgps.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import com.alvarez.campusgps.model.Posicion
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker
import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter

@Composable
fun MapaUbicacion(
    posicion: Posicion?,
    modifier: Modifier = Modifier
) {
    val contexto = LocalContext.current

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

    // Marcador rojo predeterminado de osmdroid
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

            icon?.mutate()?.colorFilter =
                ColorMatrixColorFilter(filtroRojo)
        }
    }

    // Mover el mapa al recibir la ubicación
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
