package com.alvarez.navlabia.screens

import java.util.Locale
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.School
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.alvarez.navlabia.components.UserAvatar
import com.alvarez.navlabia.model.DummyData
import com.alvarez.navlabia.ui.theme.CardBackground
import com.alvarez.navlabia.ui.theme.HeaderGradientEnd
import com.alvarez.navlabia.ui.theme.HeaderGradientStart
import com.alvarez.navlabia.ui.theme.PurplePrimary
import com.alvarez.navlabia.ui.theme.SurfaceLavender
import com.alvarez.navlabia.ui.theme.TextGray
import com.alvarez.navlabia.ui.theme.TextPrimary
import com.alvarez.navlabia.ui.theme.TextSecondaryPurple
import com.alvarez.navlabia.ui.theme.TopBarLavender

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(navController: NavController, alumnoId: Int) {
    val alumno = DummyData.getAlumnoById(alumnoId)
    val formattedId = String.format(Locale.US, "2024-%04d", alumno.id)

    Scaffold(
        containerColor = SurfaceLavender,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Expediente Académico",
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Volver",
                            tint = TextPrimary
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = TopBarLavender
                )
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Header Banner con avatar superpuesto
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp),
                contentAlignment = Alignment.TopCenter
            ) {
                // Banner con degradado horizontal y esquinas inferiores redondeadas
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(130.dp)
                        .background(
                            brush = Brush.horizontalGradient(
                                colors = listOf(HeaderGradientStart, HeaderGradientEnd)
                            ),
                            shape = RoundedCornerShape(bottomStart = 24.dp, bottomEnd = 24.dp)
                        )
                )

                // Avatar circular superpuesto al borde inferior del banner
                UserAvatar(
                    fotoUrl = alumno.fotoUrl,
                    nombre = alumno.nombre,
                    size = 96.dp,
                    borderWidth = 3.dp,
                    borderColor = Color.White,
                    modifier = Modifier.align(Alignment.BottomCenter)
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Nombre y carrera
            Text(
                text = alumno.nombre,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = TextPrimary
            )

            Spacer(modifier = Modifier.height(2.dp))

            Text(
                text = alumno.carrera,
                style = MaterialTheme.typography.bodyMedium,
                color = TextSecondaryPurple,
                fontWeight = FontWeight.Medium
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Tarjeta unificada con datos académicos + biografía + división
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = CardBackground),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp)
                ) {
                    // Filas de información académica
                    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                        DetailInfoRow(
                            icon = Icons.Default.Badge,
                            label = "ID Estudiante",
                            value = formattedId
                        )
                        DetailInfoRow(
                            icon = Icons.Default.Email,
                            label = "Correo Electrónico",
                            value = alumno.correo
                        )
                        DetailInfoRow(
                            icon = Icons.Default.School,
                            label = "Facultad",
                            value = alumno.facultad
                        )
                    }

                    // Línea divisora
                    HorizontalDivider(
                        modifier = Modifier.padding(vertical = 18.dp),
                        thickness = 1.dp,
                        color = Color(0xFFE0E0E0)
                    )

                    // Sección Biografía
                    Text(
                        text = "Biografía",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = alumno.biografia,
                        style = MaterialTheme.typography.bodyMedium,
                        color = TextGray,
                        lineHeight = 20.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Composable
private fun DetailInfoRow(
    icon: ImageVector,
    label: String,
    value: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = PurplePrimary,
            modifier = Modifier.size(24.dp)
        )

        Spacer(modifier = Modifier.width(16.dp))

        Column {
            Text(
                text = label,
                style = MaterialTheme.typography.labelSmall,
                color = TextGray
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = value,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold,
                color = TextPrimary
            )
        }
    }
}
