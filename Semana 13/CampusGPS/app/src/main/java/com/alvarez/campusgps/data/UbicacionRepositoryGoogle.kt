package com.alvarez.campusgps.data

import android.annotation.SuppressLint
import com.alvarez.campusgps.model.Posicion
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.Priority
import kotlinx.coroutines.tasks.await

class UbicacionRepositoryGoogle(
    private val cliente: FusedLocationProviderClient
) : UbicacionRepository {

    @SuppressLint("MissingPermission")
    override suspend fun obtenerPosicion(): Result<Posicion> =
        runCatching {
            val ubicacion = cliente.getCurrentLocation(
                Priority.PRIORITY_HIGH_ACCURACY,
                null
            ).await() ?: error("No se pudo obtener la ubicación")

            Posicion(
                latitud = ubicacion.latitude,
                longitud = ubicacion.longitude
            )
        }
}
