package com.alvarez.navlabia.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.alvarez.navlabia.ui.theme.PurplePrimary

@Composable
fun UserAvatar(
    fotoUrl: String,
    nombre: String,
    modifier: Modifier = Modifier,
    size: Dp = 48.dp,
    borderWidth: Dp = 0.dp,
    borderColor: Color = Color.White
) {
    val initial = if (nombre.isNotEmpty()) nombre.take(1).uppercase() else "?"

    Box(
        modifier = modifier
            .size(size)
            .then(
                if (borderWidth > 0.dp) Modifier.border(borderWidth, borderColor, CircleShape)
                else Modifier
            )
            .clip(CircleShape)
            .background(PurplePrimary.copy(alpha = 0.2f)),
        contentAlignment = Alignment.Center
    ) {
        if (fotoUrl.isNotEmpty()) {
            AsyncImage(
                model = fotoUrl,
                contentDescription = "Avatar de $nombre",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxSize()
                    .clip(CircleShape)
            )
        } else {
            Text(
                text = initial,
                fontWeight = FontWeight.Bold,
                fontSize = (size.value * 0.4).sp,
                color = PurplePrimary
            )
        }
    }
}
