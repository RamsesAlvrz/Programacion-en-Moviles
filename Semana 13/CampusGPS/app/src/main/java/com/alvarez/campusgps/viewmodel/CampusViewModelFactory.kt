package com.alvarez.campusgps.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.alvarez.campusgps.data.LugaresRepository
import com.alvarez.campusgps.data.PreferenciasRepository
import com.alvarez.campusgps.data.UbicacionRepository

class CampusViewModelFactory(
    private val ubicacionRepo: UbicacionRepository,
    private val lugaresRepo: LugaresRepository,
    private val preferenciasRepo: PreferenciasRepository
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(
        modelClass: Class<T>
    ): T {
        return MapaViewModel(
            ubicacionRepo,
            lugaresRepo,
            preferenciasRepo
        ) as T
    }
}
