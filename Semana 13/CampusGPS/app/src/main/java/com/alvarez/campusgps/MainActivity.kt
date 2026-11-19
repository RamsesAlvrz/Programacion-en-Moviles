
package com.alvarez.campusgps

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.core.view.WindowCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import com.alvarez.campusgps.data.LugaresRepositoryLocal
import com.alvarez.campusgps.data.PreferenciasRepository
import com.alvarez.campusgps.data.UbicacionRepositoryGoogle
import com.alvarez.campusgps.ui.NavegacionCampus
import com.alvarez.campusgps.ui.theme.CampusGPSTheme
import com.alvarez.campusgps.viewmodel.CampusViewModelFactory
import com.alvarez.campusgps.viewmodel.MapaViewModel
import com.google.android.gms.location.LocationServices

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Permite dibujar detrás de la barra de estado.
        WindowCompat.setDecorFitsSystemWindows(
            window,
            false
        )

        // Iconos blancos para combinar con el verde esmeralda.
        WindowCompat.getInsetsController(
            window,
            window.decorView
        ).isAppearanceLightStatusBars = false

        val ubicacionRepo = UbicacionRepositoryGoogle(
            cliente = LocationServices
                .getFusedLocationProviderClient(this),
            context = applicationContext
        )

        val lugaresRepo = LugaresRepositoryLocal()

        val preferenciasRepo = PreferenciasRepository(
            applicationContext
        )

        val factory = CampusViewModelFactory(
            ubicacionRepo,
            lugaresRepo,
            preferenciasRepo
        )

        setContent {
            CampusGPSTheme {
                val vm: MapaViewModel = viewModel(
                    factory = factory
                )

                NavegacionCampus(vm)
            }
        }
    }
}
