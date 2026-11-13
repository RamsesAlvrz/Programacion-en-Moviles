package com.tecsup.smartcatalog.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.tecsup.smartcatalog.R

@Composable
fun CatalogoBanner(
    onExplorar: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(185.dp)
            .clip(RoundedCornerShape(20.dp))
    ) {

        Image(
            painter = painterResource(
                id = R.drawable.banner_smartcatalog
            ),
            contentDescription = "Banner de celulares SmartCatalog",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        Column(
            modifier = Modifier
                .align(Alignment.CenterStart)
                .fillMaxWidth(0.58f)
                .padding(start = 16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = "NUEVA EXPERIENCIA",
                style = MaterialTheme.typography.labelSmall,
                color = Color(0xFF93C5FD)
            )

            Text(
                text = "Descubre tu próximo celular",
                style = MaterialTheme.typography.titleMedium,
                color = Color.White,
                fontWeight = FontWeight.Bold
            )

            Button(
                onClick = onExplorar,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.White,
                    contentColor = Color(0xFF172554)
                ),
                contentPadding = PaddingValues(
                    horizontal = 14.dp,
                    vertical = 4.dp
                )
            ) {
                Text("Explorar")
            }
        }
    }
}
