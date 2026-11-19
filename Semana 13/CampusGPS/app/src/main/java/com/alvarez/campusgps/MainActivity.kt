package com.alvarez.campusgps

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.viewmodel.compose.viewModel
import com.alvarez.campusgps.data.LugaresRepositoryLocal
import com.alvarez.campusgps.data.UbicacionRepositoryGoogle
import com.alvarez.campusgps.ui.PantallaMapa
import com.alvarez.campusgps.ui.theme.CampusGPSTheme
import com.alvarez.campusgps.viewmodel.CampusViewModelFactory
import com.alvarez.campusgps.viewmodel.MapaViewModel
import com.google.android.gms.location.LocationServices

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val ubicacionRepo = UbicacionRepositoryGoogle(
            LocationServices.getFusedLocationProviderClient(this)
        )

        val lugaresRepo = LugaresRepositoryLocal()

        val factory = CampusViewModelFactory(
            ubicacionRepo,
            lugaresRepo
        )

        setContent {
            CampusGPSTheme {
                val vm: MapaViewModel = viewModel(factory = factory)
                PantallaMapa(vm)
            }
        }
    }
}
