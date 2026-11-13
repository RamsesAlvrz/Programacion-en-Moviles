package com.tecsup.smartcatalog.data.auth

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class AuthRepository {

    private val auth = FirebaseAuth.getInstance()
    private val firestore = FirebaseFirestore.getInstance()

    fun usuarioActual() = auth.currentUser

    suspend fun registrar(
        nombre: String,
        correo: String,
        password: String
    ): Result<String> {

        return try {

            val resultado = auth
                .createUserWithEmailAndPassword(
                    correo,
                    password
                ).await()

            val uid = resultado.user?.uid
                ?: throw Exception("No se pudo crear el usuario")

            val datos = hashMapOf(
                "nombre" to nombre,
                "correo" to correo,
                "rol" to "usuario"
            )

            firestore.collection("usuarios")
                .document(uid)
                .set(datos)
                .await()

            Result.success(uid)

        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun iniciarSesion(
        correo: String,
        password: String
    ): Result<String> {

        return try {

            val resultado = auth
                .signInWithEmailAndPassword(
                    correo,
                    password
                ).await()

            val uid = resultado.user?.uid
                ?: throw Exception("No se pudo iniciar sesión")

            Result.success(uid)

        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun obtenerRol(): String {

        val uid = auth.currentUser?.uid
            ?: return "sin_sesion"

        val documento = firestore
            .collection("usuarios")
            .document(uid)
            .get()
            .await()

        return documento.getString("rol") ?: "usuario"
    }

    fun cerrarSesion() {
        auth.signOut()
    }
}
