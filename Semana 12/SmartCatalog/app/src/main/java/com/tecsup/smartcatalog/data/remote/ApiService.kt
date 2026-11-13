package com.tecsup.smartcatalog.data.remote

import com.tecsup.smartcatalog.data.remote.dto.ItemDto
import com.tecsup.smartcatalog.data.remote.dto.ProductosResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiService {

    @GET("products/category/smartphones")
    suspend fun obtenerProductos(): ProductosResponse

    @GET("products/{id}")
    suspend fun obtenerDetalle(
        @Path("id") id: Int
    ): ItemDto
}
