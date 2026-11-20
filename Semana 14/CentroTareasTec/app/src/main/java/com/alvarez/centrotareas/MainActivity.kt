
package com.alvarez.centrotareas

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.core.view.WindowCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.room.Room
import com.alvarez.centrotareas.data.HistorialRepository
import com.alvarez.centrotareas.data.TareasRepositoryLocal
import com.alvarez.centrotareas.data.local.CentroTareasDatabase
import com.alvarez.centrotareas.ui.PantallaPrincipal
import com.alvarez.centrotareas.ui.theme.CentroTareasTheme
import com.alvarez.centrotareas.viewmodel.HistorialViewModel
import com.alvarez.centrotareas.viewmodel.HistorialViewModelFactory
import com.alvarez.centrotareas.viewmodel.TareasViewModel
import com.alvarez.centrotareas.viewmodel.TareasViewModelFactory

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Permite dibujar la interfaz detrás de las barras del sistema.
        WindowCompat.setDecorFitsSystemWindows(window, false)

        // Mantiene los iconos de la barra de estado en blanco.
        WindowCompat.getInsetsController(
            window,
            window.decorView
        ).isAppearanceLightStatusBars = false

        val database = Room.databaseBuilder(
            applicationContext,
            CentroTareasDatabase::class.java,
            "centro_tareas_db"
        ).build()

        val historialRepository = HistorialRepository(
            database.ejecucionDao()
        )

        val tareasFactory = TareasViewModelFactory(
            TareasRepositoryLocal(),
            historialRepository
        )

        val historialFactory = HistorialViewModelFactory(
            historialRepository
        )

        setContent {
            CentroTareasTheme {
                val tareasViewModel: TareasViewModel = viewModel(
                    factory = tareasFactory
                )

                val historialViewModel: HistorialViewModel = viewModel(
                    factory = historialFactory
                )

                PantallaPrincipal(
                    viewModel = tareasViewModel,
                    historialViewModel = historialViewModel
                )
            }
        }
    }
}
