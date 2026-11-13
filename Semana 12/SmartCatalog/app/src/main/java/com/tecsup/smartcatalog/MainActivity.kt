
package com.tecsup.smartcatalog

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.viewmodel.compose.viewModel
import com.tecsup.smartcatalog.ui.lista.ListaScreen
import com.tecsup.smartcatalog.ui.lista.ListaViewModel
import com.tecsup.smartcatalog.ui.lista.ListaViewModelFactory
import com.tecsup.smartcatalog.ui.theme.SmartCatalogTheme

class MainActivity : ComponentActivity() {

    private val container by lazy {
        AppContainer(applicationContext)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            SmartCatalogTheme {

                val listaViewModel: ListaViewModel = viewModel(
                    factory = ListaViewModelFactory(
                        container.itemRepository
                    )
                )

                ListaScreen(
                    viewModel = listaViewModel,
                    onVerDetalle = { id ->
                        // La navegación al detalle se implementará después.
                    }
                )
            }
        }
    }
}
