package com.alvarez.clinicasalud

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.alvarez.clinicasalud.model.Screen
import com.alvarez.clinicasalud.screens.ConfirmationScreen
import com.alvarez.clinicasalud.screens.HomeScreen
import com.alvarez.clinicasalud.screens.MedicalHistoryScreen
import com.alvarez.clinicasalud.screens.MyAppointmentsScreen
import com.alvarez.clinicasalud.screens.ProfileScreen
import com.alvarez.clinicasalud.screens.ScheduleScreen
import com.alvarez.clinicasalud.screens.UserProfileScreen // Asegúrate de importar tu nueva pantalla o archivo correspondiente
import com.alvarez.clinicasalud.ui.*
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

    // ModalNavigationDrawer envuelve toda la estructura de navegación secundaria
    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                Spacer(modifier = Modifier.height(16.dp))
                Text("Clínica Salud+", modifier = Modifier.padding(16.dp), style = MaterialTheme.typography.titleLarge)
                Divider()
                NavigationDrawerItem(
                    label = { Text("Inicio") },
                    selected = false,
                    onClick = {
                        scope.launch { drawerState.close() }
                        navController.navigate(Screen.Home.route) {
                            popUpTo(Screen.Home.route) { inclusive = true }
                        }
                    }
                )
                NavigationDrawerItem(
                    label = { Text("Mis Citas") },
                    selected = false,
                    onClick = {
                        scope.launch { drawerState.close() }
                        navController.navigate(Screen.MyAppointments.route)
                    }
                )
                NavigationDrawerItem(
                    label = { Text("Historial Médico") },
                    selected = false,
                    onClick = {
                        scope.launch { drawerState.close() }
                        navController.navigate(Screen.MedicalHistory.route)
                    }
                )
                // Opción añadida para el perfil del usuario en el menú lateral
                NavigationDrawerItem(
                    label = { Text("Perfil") },
                    selected = false,
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
                        navController.navigate(Screen.Home.route) {
                            popUpTo(Screen.Home.route) { inclusive = true }
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
            // Destino añadido para la pantalla de perfil del usuario
            composable(Screen.UserProfile.route) {
                UserProfileScreen(
                    onOpenDrawer = { scope.launch { drawerState.open() } }
                )
            }
        }
    }
}