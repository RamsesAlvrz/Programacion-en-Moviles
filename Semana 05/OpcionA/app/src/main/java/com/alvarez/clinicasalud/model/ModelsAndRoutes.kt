package com.alvarez.clinicasalud.model

// Modelo de datos para el médico
data class Doctor(
    val id: Int,
    val name: String,
    val specialty: String,
    val rating: Double,
    val imageRes: String = "Dr."
)

// Modelo para las citas agendadas (Sección Mis Citas)
data class Appointment(
    val id: Int,
    val doctorName: String,
    val specialty: String,
    val date: String,
    val time: String,
    val status: String // "Confirmada" o "Completada"
)

// Rutas de Navegación de la App
sealed class Screen(val route: String) {
    object Home : Screen("home")
    object Profile : Screen("profile/{doctorId}") {
        fun createRoute(doctorId: Int) = "profile/$doctorId"
    }
    object Schedule : Screen("schedule/{doctorId}") {
        fun createRoute(doctorId: Int) = "schedule/$doctorId"
    }
    object Confirmation : Screen("confirmation/{doctorName}/{date}/{time}") {
        fun createRoute(doctorName: String, date: String, time: String) =
            "confirmation/$doctorName/$date/$time"
    }
    object MyAppointments : Screen("my_appointments")
    object MedicalHistory : Screen("medical_history")
}