package com.alvarez.murocampus.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp

@Composable
fun Cargando() {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        CircularProgressIndicator()

        Spacer(modifier = Modifier.height(16.dp))

        Text("Cargando publicaciones…")
    }
}

@Composable
fun ErrorConReintento(
    mensaje: String,
    onReintentar: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.errorContainer
            )
        ) {
            Text(
                text = mensaje,
                modifier = Modifier.padding(16.dp),
                color = MaterialTheme.colorScheme.error
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        Button(onClick = onReintentar) {
            Text("Reintentar")
        }
    }
}

@Composable
fun TarjetaPost(
    userId: Int,
    titulo: String,
    contenido: String,
    onClick: () -> Unit = {}
) {

    val colorUsuario = when (userId) {
        1 -> Color(0xFF287FC4)
        2 -> Color(0xFFFFB300)
        3 -> Color(0xFF287D46)
        4 -> Color(0xFF7B4BB7)
        5 -> Color(0xFFB3261E)
        6 -> Color(0xFF00796B)
        7 -> Color(0xFFC2410C)
        8 -> Color(0xFFAD356D)
        9 -> Color(0xFF475569)
        10 -> Color(0xFF795548)
        else -> MaterialTheme.colorScheme.primary
    }

    val colorTextoUsuario = if (userId == 2) {
        Color(0xFF202B38)
    } else {
        Color.White
    }

    Card(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 5.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer
        )
    ) {
        Column(
            modifier = Modifier.padding(12.dp)
        ) {
            Surface(
                color = colorUsuario,
                shape = RoundedCornerShape(8.dp)
            ) {
                Text(
                    text = "Usuario $userId",
                    modifier = Modifier.padding(
                        horizontal = 8.dp,
                        vertical = 2.dp
                    ),
                    color = colorTextoUsuario,
                    fontWeight = FontWeight.SemiBold,
                    style = MaterialTheme.typography.labelMedium
                )
            }

            Spacer(modifier = Modifier.height(5.dp))

            Text(
                text = titulo,
                fontWeight = FontWeight.Bold,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = contenido,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}