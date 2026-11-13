
package com.tecsup.smartcatalog

import android.Manifest
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import com.tecsup.smartcatalog.notification.NotificationHelper
import com.tecsup.smartcatalog.ui.navigation.AppNav
import com.tecsup.smartcatalog.ui.theme.SmartCatalogTheme

class MainActivity : ComponentActivity() {

    private val container by lazy {
        AppContainer(applicationContext)
    }

    private val solicitarPermiso =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { _ ->
            // El sistema registra si se concedió el permiso.
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        NotificationHelper.crearCanal(this)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            solicitarPermiso.launch(
                Manifest.permission.POST_NOTIFICATIONS
            )
        }

        setContent {
            SmartCatalogTheme {
                AppNav(
                    repository = container.itemRepository
                )
            }
        }
    }
}
