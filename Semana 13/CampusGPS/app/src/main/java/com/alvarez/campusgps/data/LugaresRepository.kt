package com.alvarez.campusgps.data

import com.alvarez.campusgps.model.Lugar

interface LugaresRepository {
    fun obtenerLugares(): List<Lugar>
}
