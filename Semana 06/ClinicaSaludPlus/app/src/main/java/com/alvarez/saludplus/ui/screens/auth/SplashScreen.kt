package com.alvarez.saludplus.ui.screens.auth

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.alvarez.saludplus.R

@Composable
fun SplashScreen(
    onRegistro: () -> Unit,
    onLogin: () -> Unit
) {
    val azul = Color(0xFF2378C9)
    val textoOscuro = Color(0xFF17324F)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFFF0F8FF),
                        Color(0xFFE3F3F9),
                        Color(0xFFD7EDF4)
                    )
                )
            )
            .padding(horizontal = 20.dp)
            .padding(top = 16.dp, bottom = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(R.drawable.logo_saludplus),
            contentDescription = "Logo de Clínica SaludPlus",
            modifier = Modifier
                .weight(0.8f)
                .fillMaxWidth(),
            contentScale = ContentScale.Fit
        )

        Text(
            text = "Clínica\nSaludPlus",
            color = textoOscuro,
            fontSize = 30.sp,
            lineHeight = 32.sp,
            fontWeight = FontWeight.ExtraBold,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Tu salud, nuestra prioridad",
            color = textoOscuro,
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(12.dp))

        Image(
            painter = painterResource(R.drawable.medico_splash),
            contentDescription = "Médico de Clínica SaludPlus",
            modifier = Modifier
                .weight(2.5f)
                .fillMaxWidth(),
            contentScale = ContentScale.Fit,
            alignment = Alignment.BottomCenter
        )

        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = onRegistro,
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = 52.dp),
            shape = RoundedCornerShape(10.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = azul,
                contentColor = Color.White
            )
        ) {
            Text(
                text = "Registrarme",
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold
            )
        }

        TextButton(
            onClick = onLogin,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "Ya tengo una cuenta",
                color = Color(0xFF205A8D),
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}