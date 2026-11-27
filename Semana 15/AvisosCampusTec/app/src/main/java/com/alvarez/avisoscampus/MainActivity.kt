
package com.alvarez.avisoscampus

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.remember
import androidx.lifecycle.viewmodel.compose.viewModel
import com.alvarez.avisoscampus.data.AvisosRepositoryMemoria
import com.alvarez.avisoscampus.ui.PantallaAvisos
import com.alvarez.avisoscampus.ui.theme.AvisosCampusTheme
import com.alvarez.avisoscampus.viewmodel.AvisosViewModel
import com.alvarez.avisoscampus.viewmodel.AvisosViewModelFactory

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        setContent {

            AvisosCampusTheme {

                val repository = remember {
                    AvisosRepositoryMemoria()
                }

                val factory = remember {
                    AvisosViewModelFactory(repository)
                }

                val vm: AvisosViewModel = viewModel(
                    factory = factory
                )

                PantallaAvisos(vm = vm)
            }
        }
    }
}
