package com.alvarez.centrotareas

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.viewmodel.compose.viewModel
import com.alvarez.centrotareas.data.TareasRepositoryLocal
import com.alvarez.centrotareas.ui.PantallaPrincipal
import com.alvarez.centrotareas.ui.theme.CentroTareasTheme
import com.alvarez.centrotareas.viewmodel.TareasViewModel
import com.alvarez.centrotareas.viewmodel.TareasViewModelFactory

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val repository = TareasRepositoryLocal()
        val factory = TareasViewModelFactory(repository)

        setContent {
            CentroTareasTheme {
                val tareasViewModel: TareasViewModel = viewModel(
                    factory = factory
                )

                PantallaPrincipal(
                    viewModel = tareasViewModel
                )
            }
        }
    }
}
