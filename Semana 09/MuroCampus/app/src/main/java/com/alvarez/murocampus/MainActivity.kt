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
import androidx.compose.ui.unit.sp
import com.alvarez.murocampus.data.PostRepositoryRemote
import com.alvarez.murocampus.data.remote.RetrofitClient
import com.alvarez.murocampus.ui.PantallaLista
import com.alvarez.murocampus.ui.PantallaDetalle
import com.alvarez.murocampus.ui.PantallaCrear
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

                var mostrarCrear by rememberSaveable {
                    mutableStateOf(false)
                }

                BackHandler(
                    enabled = idSeleccionado != null ||
                            mostrarCrear
                ) {

                    if (mostrarCrear) {
                        vm.limpiarPublicacion()
                        mostrarCrear = false

                    } else {
                        vm.cerrarDetalle()
                        idSeleccionado = null
                    }
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
                    },

                    floatingActionButton = {

                        if (idSeleccionado == null &&
                            !mostrarCrear
                        ) {

                            FloatingActionButton(
                                onClick = {
                                    vm.limpiarPublicacion()
                                    mostrarCrear = true
                                },
                                containerColor =
                                    MaterialTheme.colorScheme.primary,
                                contentColor =
                                    MaterialTheme.colorScheme.onPrimary
                            ) {

                                Text(
                                    text = "+",
                                    fontSize = 30.sp
                                )
                            }
                        }
                    }

                ) { innerPadding ->

                    val id = idSeleccionado

                    when {

                        mostrarCrear -> {

                            PantallaCrear(
                                vm = vm,
                                modifier = Modifier.padding(
                                    innerPadding
                                )
                            )
                        }

                        id != null -> {

                            PantallaDetalle(
                                vm = vm,
                                id = id,
                                modifier = Modifier.padding(
                                    innerPadding
                                )
                            )
                        }

                        else -> {

                            PantallaLista(
                                vm = vm,
                                modifier = Modifier.padding(
                                    innerPadding
                                ),
                                onVerDetalle = { postId ->
                                    vm.abrir(postId)
                                    idSeleccionado = postId
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}