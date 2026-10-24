
package com.alvarez.lavacampus.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.alvarez.lavacampus.data.local.LavaDatabase
import com.alvarez.lavacampus.data.prefs.PreferenciasStore
import com.alvarez.lavacampus.data.repository.LavaRepository

// Fábrica encargada de crear los ViewModels de LavaCampus
class LavaViewModelFactory(
    private val repository: LavaRepository,
    private val preferenciasStore: PreferenciasStore,
    private val database: LavaDatabase
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(
        modelClass: Class<T>
    ): T {

        // ViewModel para gestionar el inicio y cierre de sesión
        if (modelClass.isAssignableFrom(SesionViewModel::class.java)) {
            return SesionViewModel(
                repository = repository,
                preferenciasStore = preferenciasStore
            ) as T
        }

        // ViewModel para consultar los pedidos del estudiante
        if (modelClass.isAssignableFrom(InicioViewModel::class.java)) {
            return InicioViewModel(
                database = database
            ) as T
        }

        // ViewModel para seleccionar servicios y registrar pedidos
        if (modelClass.isAssignableFrom(NuevoPedidoViewModel::class.java)) {
            return NuevoPedidoViewModel(
                repository = repository
            ) as T
        }

        // COMMIT 10: ViewModel para detalle, seguimiento y cancelación
        if (modelClass.isAssignableFrom(DetallePedidoViewModel::class.java)) {
            return DetallePedidoViewModel(
                repository = repository
            ) as T
        }

        // COMMIT 11: ViewModel para pedidos activos e historial
        if (modelClass.isAssignableFrom(MisPedidosViewModel::class.java)) {
            return MisPedidosViewModel(
                repository = repository
            ) as T
        }

        // COMMIT 12: ViewModel para el panel del administrador
        if (modelClass.isAssignableFrom(PanelViewModel::class.java)) {
            return PanelViewModel(
                repository = repository
            ) as T
        }

        // COMMIT 13: ViewModel de la bandeja de pedidos.
        if (modelClass.isAssignableFrom(BandejaViewModel::class.java)) {
            return BandejaViewModel(
                repository = repository
            ) as T
        }

        if (modelClass.isAssignableFrom(GestionPedidoViewModel::class.java)) {
            return GestionPedidoViewModel(
                repository = repository
            ) as T
        }

        throw IllegalArgumentException(
            "ViewModel no reconocido: ${modelClass.name}"
        )
    }
}
