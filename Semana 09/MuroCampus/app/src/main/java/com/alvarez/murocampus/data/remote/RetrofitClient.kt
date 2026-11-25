package com.alvarez.murocampus.data.remote

import kotlinx.serialization.json.Json
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory

object RetrofitClient {

    private val json = Json {
        ignoreUnknownKeys = true
    }

    // Interceptor que solicita respuestas en formato JSON
    private val acceptInterceptor = Interceptor { chain ->

        val peticionOriginal = chain.request()

        val nuevaPeticion = peticionOriginal
            .newBuilder()
            .header("Accept", "application/json")
            .build()

        chain.proceed(nuevaPeticion)
    }

    private val cliente = OkHttpClient.Builder()

        // Agrega el encabezado antes de registrar la petición
        .addInterceptor(acceptInterceptor)

        // Permite visualizar las peticiones en Logcat
        .addInterceptor(
            HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            }
        )
        .build()

    val api: PostApi by lazy {

        Retrofit.Builder()
            .baseUrl("https://jsonplaceholder.typicode.com/")
            .client(cliente)
            .addConverterFactory(
                json.asConverterFactory(
                    "application/json".toMediaType()
                )
            )
            .build()
            .create(PostApi::class.java)
    }
}
