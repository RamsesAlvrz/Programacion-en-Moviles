package com.alvarez.campusgps.data

import com.alvarez.campusgps.model.Posicion
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL
import java.util.Locale
import kotlin.math.*

data class PuntoRuta(
    val latitud: Double,
    val longitud: Double
)

data class RutaPeatonal(
    val puntos: List<PuntoRuta>,
    val distanciaMetros: Double,
    val duracionSegundos: Double,
    val separacionDestinoMetros: Double = 0.0
)

interface RutaRepository {
    suspend fun obtenerRuta(
        origen: Posicion,
        destinoLatitud: Double,
        destinoLongitud: Double
    ): RutaPeatonal
}

class RutaRepositoryOSRM : RutaRepository {

    override suspend fun obtenerRuta(
        origen: Posicion,
        destinoLatitud: Double,
        destinoLongitud: Double
    ): RutaPeatonal = withContext(Dispatchers.IO) {

        fun coordenada(valor: Double): String =
            String.format(Locale.US, "%.7f", valor)

        val coordenadas =
            "${coordenada(origen.longitud)},${coordenada(origen.latitud)};" +
                    "${coordenada(destinoLongitud)},${coordenada(destinoLatitud)}"

        val direccion =
            "https://routing.openstreetmap.de/routed-foot/route/v1/driving/" +
                    coordenadas +
                    "?overview=full&geometries=geojson&steps=false"

        val conexion =
            (URL(direccion).openConnection() as HttpURLConnection).apply {
                requestMethod = "GET"
                connectTimeout = 12000
                readTimeout = 15000

                setRequestProperty(
                    "User-Agent",
                    "CampusGPS/2.0 (Android; academic routing demo)"
                )

                setRequestProperty("Accept", "application/json")
            }

        try {
            val codigo = conexion.responseCode

            if (codigo != HttpURLConnection.HTTP_OK) {
                throw IllegalStateException(
                    if (codigo == 429) {
                        "Servicio ocupado; inténtalo más tarde."
                    } else {
                        "El servidor de rutas respondió con error $codigo."
                    }
                )
            }

            val contenido = conexion.inputStream
                .bufferedReader()
                .use { it.readText() }

            val respuesta = JSONObject(contenido)

            if (respuesta.optString("code") != "Ok") {
                throw IllegalStateException(
                    "No se encontró una ruta peatonal para este destino."
                )
            }

            val rutas = respuesta.optJSONArray("routes")

            if (rutas == null || rutas.length() == 0) {
                throw IllegalStateException(
                    "No se encontró una ruta disponible."
                )
            }

            val ruta = rutas.getJSONObject(0)

            val coordenadasJson = ruta
                .getJSONObject("geometry")
                .getJSONArray("coordinates")

            val puntos = (0 until coordenadasJson.length()).map { i ->
                val par = coordenadasJson.getJSONArray(i)

                PuntoRuta(
                    latitud = par.getDouble(1),
                    longitud = par.getDouble(0)
                )
            }

            if (puntos.size < 2) {
                throw IllegalStateException(
                    "La ruta recibida no contiene suficientes puntos."
                )
            }

            // Comprobar qué tan cerca termina la ruta
            // del destino solicitado.
            val puntoFinal = puntos.last()

            android.util.Log.d(
                "CampusGPS_RUTA",
                "Fin de ruta OSRM: " +
                        "${puntoFinal.latitud}, ${puntoFinal.longitud}"
            )

            val separacion = calcularDistanciaMetros(
                puntoFinal.latitud,
                puntoFinal.longitud,
                destinoLatitud,
                destinoLongitud
            )

            RutaPeatonal(
                puntos = puntos,
                distanciaMetros = ruta.getDouble("distance"),
                duracionSegundos = ruta.getDouble("duration"),
                separacionDestinoMetros = separacion
            )

        } finally {
            conexion.disconnect()
        }
    }

    private fun calcularDistanciaMetros(
        lat1: Double,
        lon1: Double,
        lat2: Double,
        lon2: Double
    ): Double {
        val radioTierra = 6371000.0

        val diferenciaLat = Math.toRadians(lat2 - lat1)
        val diferenciaLon = Math.toRadians(lon2 - lon1)

        val a =
            sin(diferenciaLat / 2).pow(2) +
                    cos(Math.toRadians(lat1)) *
                    cos(Math.toRadians(lat2)) *
                    sin(diferenciaLon / 2).pow(2)

        val c = 2 * atan2(
            sqrt(a),
            sqrt(1 - a)
        )

        return radioTierra * c
    }
}
