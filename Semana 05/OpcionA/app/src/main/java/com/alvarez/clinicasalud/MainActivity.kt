package com.alvarez.clinicasalud

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.RadioButtonUnchecked
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.alvarez.clinicasalud.model.Screen
import com.alvarez.clinicasalud.screens.*
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                MainApp()
            }
        }
    }
}

@Composable
fun MainApp() {
    val navController = rememberNavController()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet(
                drawerShape = RoundedCornerShape(topEnd = 24.dp, bottomEnd = 24.dp),
                modifier = Modifier.width(300.dp)
            ) {
                Spacer(modifier = Modifier.height(24.dp))

                // Encabezado del usuario (JP)
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(horizontal = 20.dp, vertical = 8.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(48.dp)
                            .background(Color(0xFFF3E8FF), shape = CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "JP",
                            color = Color(0xFF512DA8),
                            fontWeight = FontWeight.Bold,
                            style = MaterialTheme.typography.titleMedium
                        )
                    }

                    Spacer(modifier = Modifier.width(16.dp))

                    Column {
                        Text(
                            text = "Juan Pérez",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Paciente",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.Gray
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))
                HorizontalDivider(modifier = Modifier.padding(horizontal = 20.dp), color = Color(0xFFE5E7EB))
                Spacer(modifier = Modifier.height(16.dp))

                // Opciones del Menú
                DrawerMenuItem(
                    label = "Inicio",
                    isSelected = currentRoute == Screen.Home.route,
                    onClick = {
                        scope.launch { drawerState.close() }
                        navController.navigate(Screen.Home.route) {
                            popUpTo(Screen.Home.route) { inclusive = true }
                        }
                    }
                )

                DrawerMenuItem(
                    label = "Mis citas",
                    isSelected = currentRoute == Screen.MyAppointments.route,
                    onClick = {
                        scope.launch { drawerState.close() }
                        navController.navigate(Screen.MyAppointments.route)
                    }
                )

                DrawerMenuItem(
                    label = "Historial médico",
                    isSelected = currentRoute == Screen.MedicalHistory.route,
                    onClick = {
                        scope.launch { drawerState.close() }
                        navController.navigate(Screen.MedicalHistory.route)
                    }
                )

                DrawerMenuItem(
                    label = "Perfil",
                    isSelected = currentRoute == Screen.UserProfile.route,
                    onClick = {
                        scope.launch { drawerState.close() }
                        navController.navigate(Screen.UserProfile.route)
                    }
                )
            }
        }
    ) {
        NavHost(navController = navController, startDestination = Screen.Home.route) {
            composable(Screen.Home.route) {
                HomeScreen(
                    onOpenDrawer = { scope.launch { drawerState.open() } },
                    onDoctorClick = { doctorId ->
                        navController.navigate(Screen.Profile.createRoute(doctorId))
                    }
                )
            }
            composable(
                route = Screen.Profile.route,
                arguments = listOf(navArgument("doctorId") { type = NavType.IntType })
            ) { backStackEntry ->
                val doctorId = backStackEntry.arguments?.getInt("doctorId") ?: 1
                ProfileScreen(
                    doctorId = doctorId,
                    onBackClick = { navController.popBackStack() },
                    onScheduleClick = { id ->
                        navController.navigate(Screen.Schedule.createRoute(id))
                    }
                )
            }
            composable(
                route = Screen.Schedule.route,
                arguments = listOf(navArgument("doctorId") { type = NavType.IntType })
            ) { backStackEntry ->
                val doctorId = backStackEntry.arguments?.getInt("doctorId") ?: 1
                ScheduleScreen(
                    doctorId = doctorId,
                    onBackClick = { navController.popBackStack() },
                    onConfirmReservation = { docName, date, time ->
                        navController.navigate(Screen.Confirmation.createRoute(docName, date, time)) {
                            popUpTo(Screen.Home.route)
                        }
                    }
                )
            }
            composable(
                route = Screen.Confirmation.route,
                arguments = listOf(
                    navArgument("doctorName") { type = NavType.StringType },
                    navArgument("date") { type = NavType.StringType },
                    navArgument("time") { type = NavType.StringType }
                )
            ) { backStackEntry ->
                val docName = backStackEntry.arguments?.getString("doctorName") ?: ""
                val date = backStackEntry.arguments?.getString("date") ?: ""
                val time = backStackEntry.arguments?.getString("time") ?: ""
                ConfirmationScreen(
                    doctorName = docName,
                    date = date,
                    time = time,
                    onBackHome = {
                        navController.navigate(Screen.MyAppointments.route) {
                            popUpTo(Screen.Home.route)
                        }
                    }
                )
            }
            composable(Screen.MyAppointments.route) {
                MyAppointmentsScreen(
                    onOpenDrawer = { scope.launch { drawerState.open() } }
                )
            }
            composable(Screen.MedicalHistory.route) {
                MedicalHistoryScreen(
                    onOpenDrawer = { scope.launch { drawerState.open() } }
                )
            }
            composable(Screen.UserProfile.route) {
                UserProfileScreen(
                    onOpenDrawer = { scope.launch { drawerState.open() } }
                )
            }
        }
    }
}

@Composable
private fun DrawerMenuItem(
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    NavigationDrawerItem(
        label = {
            Text(
                text = label,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                color = if (isSelected) Color(0xFF512DA8) else Color(0xFF374151)
            )
        },
        icon = {
            Icon(
                imageVector = Icons.Outlined.RadioButtonUnchecked,
                contentDescription = null,
                tint = if (isSelected) Color(0xFF512DA8) else Color(0xFF111827),
                modifier = Modifier.size(20.dp)
            )
        },
        selected = isSelected,
        onClick = onClick,
        colors = NavigationDrawerItemDefaults.colors(
            selectedContainerColor = Color(0xFFF3E8FF),
            unselectedContainerColor = Color.Transparent
        ),
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier.padding(horizontal = 12.dp, vertical = 2.dp)
    )
}