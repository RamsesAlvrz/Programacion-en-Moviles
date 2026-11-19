package com.alvarez.campusgps.data

import com.alvarez.campusgps.model.Posicion

interface UbicacionRepository {

    suspend fun obtenerPosicion(): Result<Posicion>

    fun playServicesDisponible(): Boolean

    fun gpsDisponible(): Boolean
}
