package com.alvarez.murocampus

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.compose.BackHandler
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import com.alvarez.murocampus.data.PostRepositoryRemote
import com.alvarez.murocampus.data.remote.RetrofitClient
import com.alvarez.murocampus.ui.PantallaLista
import com.alvarez.murocampus.ui.PantallaDetalle
import com.alvarez.murocampus.ui.theme.MuroCampusTheme
import com.alvarez.murocampus.viewmodel.PostsViewModel
import com.alvarez.murocampus.viewmodel.PostsViewModelFactory

class MainActivity : ComponentActivity() {

    private val vm: PostsViewModel by viewModels {
        PostsViewModelFactory(
            PostRepositoryRemote(RetrofitClient.api)
        )
    }

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            MuroCampusTheme {

                var idSeleccionado by rememberSaveable {
                    mutableStateOf<Int?>(null)
                }

                BackHandler(
                    enabled = idSeleccionado != null
                ) {
                    vm.cerrarDetalle()
                    idSeleccionado = null
                }

                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    containerColor = MaterialTheme.colorScheme.background,
                    topBar = {
                        TopAppBar(
                            title = {
                                Text(
                                    text = "MuroCampus",
                                    style = MaterialTheme.typography.titleLarge
                                )
                            },
                            colors = TopAppBarDefaults.topAppBarColors(
                                containerColor = MaterialTheme.colorScheme.primary,
                                titleContentColor = MaterialTheme.colorScheme.onPrimary
                            )
                        )
                    }
                ) { innerPadding ->

                    val id = idSeleccionado

                    if (id == null) {

                        PantallaLista(
                            vm = vm,
                            modifier = Modifier.padding(innerPadding),
                            onVerDetalle = { postId ->
                                vm.abrir(postId)
                                idSeleccionado = postId
                            }
                        )

                    } else {

                        PantallaDetalle(
                            vm = vm,
                            id = id,
                            modifier = Modifier.padding(innerPadding)
                        )
                    }
                }
            }
        }
    }
}