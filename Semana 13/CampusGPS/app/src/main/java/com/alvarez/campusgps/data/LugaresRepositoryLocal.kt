package com.alvarez.campusgps.data

import com.alvarez.campusgps.model.Lugar

class LugaresRepositoryLocal : LugaresRepository {

    override fun obtenerLugares(): List<Lugar> = listOf(
        Lugar(1, "Biblioteca Central", "Estudio", -12.04370, -76.95320),
        Lugar(2, "Cafetería Campus", "Comida", -12.04400, -76.95265),
        Lugar(3, "Laboratorio de Cómputo", "Estudio", -12.04420, -76.95315),
        Lugar(4, "Auditorio Principal", "Eventos", -12.04385, -76.95295),
        Lugar(5, "Estacionamiento Norte", "Servicios", -12.043834, -76.952806)
    )
}
