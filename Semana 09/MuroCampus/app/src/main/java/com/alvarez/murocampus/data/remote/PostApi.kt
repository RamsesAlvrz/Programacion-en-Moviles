package com.alvarez.murocampus.data.remote

import com.alvarez.murocampus.model.Post
import com.alvarez.murocampus.model.Comentario
import com.alvarez.murocampus.model.NuevoPost
import com.alvarez.murocampus.model.EditarPost
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.DELETE
import retrofit2.http.PATCH
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.http.Body

interface PostApi {

    @GET("posts")
    suspend fun listar(): List<Post>

    @GET("posts/{id}")
    suspend fun detalle(
        @Path("id") id: Int
    ): Post

    @GET("posts/{id}/comments")
    suspend fun comentarios(
        @Path("id") id: Int
    ): List<Comentario>

    @GET("posts")
    suspend fun porUsuario(
        @Query("userId") userId: Int
    ): List<Post>

    @POST("posts")
    suspend fun crear(
        @Body nuevo: NuevoPost
    ): Post

    @DELETE("posts/{id}")
    suspend fun eliminar(
        @Path("id") id: Int
    ): Response<Unit>

    // Actualizar parcialmente una publicacion
    @PATCH("posts/{id}")
    suspend fun editar(
        @Path("id") id: Int,
        @Body cambios: EditarPost
    ): Post
}
