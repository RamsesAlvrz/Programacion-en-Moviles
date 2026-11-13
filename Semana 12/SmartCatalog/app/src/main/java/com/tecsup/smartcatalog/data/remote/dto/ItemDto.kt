package com.tecsup.smartcatalog.data.remote.dto

data class ProductosResponse(
    val products: List<ItemDto>
)

data class ItemDto(
    val id: Int,
    val title: String,
    val description: String,
    val price: Double,
    val brand: String? = null,
    val thumbnail: String
)
