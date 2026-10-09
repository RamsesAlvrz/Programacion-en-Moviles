package com.alvarez.saludplus.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import com.alvarez.saludplus.navigation.Rutas

@Composable
fun BotonPrimario(
    texto: String,
    onClick: () -> Unit,
    habilitado: Boolean = true
) {
    Button(
        onClick = onClick,
        enabled = habilitado,
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = 52.dp),
        shape = RoundedCornerShape(14.dp)
    ) {
        Text(texto)
    }
}

@Composable
fun CampoTexto(
    etiqueta: String,
    valor: String,
    onCambio: (String) -> Unit,
    teclado: KeyboardType = KeyboardType.Text,
    secreto: Boolean = false
) {
    OutlinedTextField(
        value = valor,
        onValueChange = onCambio,
        modifier = Modifier.fillMaxWidth(),
        label = {
            Text(etiqueta)
        },
        singleLine = true,
        shape = RoundedCornerShape(12.dp),
        keyboardOptions = KeyboardOptions(
            keyboardType = teclado
        ),
        visualTransformation = if (secreto) {
            PasswordVisualTransformation()
        } else {
            VisualTransformation.None
        }
    )
}

@Composable
fun Encabezado(
    titulo: String,
    onVolver: (() -> Unit)? = null
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (onVolver != null) {
            TextButton(
                onClick = onVolver
            ) {
                Text("‹ Volver")
            }
        }

        Text(
            text = titulo,
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
fun Tarjeta(
    titulo: String,
    detalle: String,
    onClick: (() -> Unit)? = null
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = titulo,
                style = MaterialTheme.typography.titleMedium
            )

            Text(
                text = detalle,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            if (onClick != null) {
                TextButton(
                    onClick = onClick
                ) {
                    Text("Ver más ›")
                }
            }
        }
    }
}

@Composable
fun PantallaEnConstruccion(
    titulo: String,
    acciones: List<Pair<String, () -> Unit>> = emptyList()
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = "SaludPlus",
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.primary
        )

        Text(
            text = titulo,
            style = MaterialTheme.typography.titleLarge
        )

        Text(
            text = "Esqueleto inicial: pantalla pendiente de implementar."
        )

        acciones.forEach { (texto, accion) ->
            Button(
                onClick = accion
            ) {
                Text(texto)
            }
        }
    }
}

@Composable
fun BarraInferior(
    rutaActual: String?,
    onDestino: (String) -> Unit
) {
    val destinos = listOf(
        Rutas.HOME to "Inicio",
        Rutas.CITAS to "Citas",
        Rutas.RESULTADOS to "Resultados",
        Rutas.PERFIL to "Perfil"
    )

    val simbolos = listOf(
        "⌂",
        "+",
        "≡",
        "○"
    )

    NavigationBar(
        containerColor = MaterialTheme.colorScheme.surface
    ) {
        destinos.forEachIndexed { indice, destino ->

            val ruta = destino.first
            val titulo = destino.second

            NavigationBarItem(
                selected = rutaActual == ruta,
                onClick = {
                    onDestino(ruta)
                },
                icon = {
                    Text(
                        text = simbolos[indice],
                        style = MaterialTheme.typography.titleLarge
                    )
                },
                label = {
                    Text(titulo)
                },
                alwaysShowLabel = true
            )
        }
    }
}