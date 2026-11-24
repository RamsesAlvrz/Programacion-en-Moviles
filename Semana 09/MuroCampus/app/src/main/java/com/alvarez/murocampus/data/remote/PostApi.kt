package com.alvarez.murocampus.data.remote

import com.alvarez.murocampus.model.Post
import com.alvarez.murocampus.model.Comentario
import com.alvarez.murocampus.model.NuevoPost
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.http.Body

interface PostApi {

    @GET("posts")
    suspend fun listar(): List<Post>

    // Completar detalle

    // Completar comentarios

    // Completar porUsuario

    // Completar crear
}