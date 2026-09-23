package com.alvarez.navlabia.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.School
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.alvarez.navlabia.components.UserAvatar
import com.alvarez.navlabia.model.DummyData
import com.alvarez.navlabia.navigation.Screen
import com.alvarez.navlabia.ui.theme.HeaderGradientEnd
import com.alvarez.navlabia.ui.theme.HeaderGradientStart
import com.alvarez.navlabia.ui.theme.LogoutBackground
import com.alvarez.navlabia.ui.theme.LogoutText
import com.alvarez.navlabia.ui.theme.TextGray
import com.alvarez.navlabia.ui.theme.TextPrimary
import com.alvarez.navlabia.ui.theme.TextSecondaryPurple

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(navController: NavController) {
    Scaffold(
        containerColor = Color.White,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Configuración de Perfil",
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
                    containerColor = Color.White
                )
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
        ) {
            // Banner superior RECTANGULAR (sin esquinas redondeadas en la parte inferior)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
                    .background(
                        brush = Brush.horizontalGradient(
                            colors = listOf(HeaderGradientStart, HeaderGradientEnd)
                        ),
                        shape = RectangleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    UserAvatar(
                        fotoUrl = DummyData.userFotoUrl,
                        nombre = DummyData.userNombreCompleto,
                        size = 80.dp,
                        borderWidth = 3.dp,
                        borderColor = Color.White
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = DummyData.userNombreCompleto,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 20.dp)
            ) {
                // INFORMACIÓN PERSONAL
                Text(
                    text = "INFORMACIÓN PERSONAL",
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Bold,
                    color = TextSecondaryPurple,
                    letterSpacing = 1.2.sp
                )

                Spacer(modifier = Modifier.height(14.dp))

                ProfileInfoRow(
                    icon = Icons.Default.Person,
                    label = "Nombre Completo",
                    value = DummyData.userNombreCompleto
                )

                Spacer(modifier = Modifier.height(14.dp))

                ProfileInfoRow(
                    icon = Icons.Default.Email,
                    label = "Correo",
                    value = DummyData.userCorreo
                )

                Spacer(modifier = Modifier.height(14.dp))

                ProfileInfoRow(
                    icon = Icons.Default.Phone,
                    label = "Teléfono",
                    value = DummyData.userTelefono
                )

                Spacer(modifier = Modifier.height(28.dp))

                // ACADÉMICO
                Text(
                    text = "ACADÉMICO",
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Bold,
                    color = TextSecondaryPurple,
                    letterSpacing = 1.2.sp
                )

                Spacer(modifier = Modifier.height(14.dp))

                ProfileInfoRow(
                    icon = Icons.Default.School,
                    label = "Carrera",
                    value = DummyData.userCarrera
                )

                Spacer(modifier = Modifier.height(14.dp))

                ProfileInfoRow(
                    icon = Icons.Default.CalendarMonth,
                    label = "Ciclo Actual",
                    value = DummyData.userCiclo
                )

                Spacer(modifier = Modifier.height(36.dp))

                // Botón Cerrar Sesión con bordes redondeados tipo píldora
                Button(
                    onClick = {
                        navController.navigate(Screen.Login.route) {
                            popUpTo(Screen.Login.route) { inclusive = true }
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    shape = RoundedCornerShape(25.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = LogoutBackground)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.Logout,
                            contentDescription = "Cerrar Sesión",
                            tint = LogoutText,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Cerrar Sesión",
                            color = LogoutText,
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

@Composable
private fun ProfileInfoRow(
    icon: ImageVector,
    label: String,
    value: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Contenedor plomo suave para el ícono
        Box(
            modifier = Modifier
                .size(42.dp)
                .background(Color(0xFFEFEBF5), shape = RoundedCornerShape(12.dp)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = Color(0xFF1C1B1F),
                modifier = Modifier.size(20.dp)
            )
        }

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
