package com.alvarez.campusgps.data

import android.annotation.SuppressLint
import android.content.Context
import android.location.LocationManager
import com.alvarez.campusgps.model.Posicion
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.Priority
import kotlinx.coroutines.tasks.await

class UbicacionRepositoryGoogle(
    private val cliente: FusedLocationProviderClient,
    context: Context
) : UbicacionRepository {

    private val contexto = context.applicationContext

    // Comprobar Google Play Services
    override fun playServicesDisponible(): Boolean {
        return GoogleApiAvailability.getInstance()
            .isGooglePlayServicesAvailable(contexto) ==
                ConnectionResult.SUCCESS
    }

    // Comprobar si la ubicación del dispositivo está activa
    override fun gpsDisponible(): Boolean {
        val administrador = contexto.getSystemService(
            Context.LOCATION_SERVICE
        ) as LocationManager

        return try {
            if (android.os.Build.VERSION.SDK_INT >=
                android.os.Build.VERSION_CODES.P
            ) {
                administrador.isLocationEnabled
            } else {
                administrador.isProviderEnabled(
                    LocationManager.GPS_PROVIDER
                ) || administrador.isProviderEnabled(
                    LocationManager.NETWORK_PROVIDER
                )
            }
        } catch (e: Exception) {
            false
        }
    }

    // Obtener la ubicación actual
    @SuppressLint("MissingPermission")
    override suspend fun obtenerPosicion(): Result<Posicion> =
        runCatching {

            val ubicacion = cliente.getCurrentLocation(
                Priority.PRIORITY_HIGH_ACCURACY,
                null
            ).await() ?: error(
                "No se pudo obtener la ubicación"
            )

            Posicion(
                latitud = ubicacion.latitude,
                longitud = ubicacion.longitude
            )
        }
}
