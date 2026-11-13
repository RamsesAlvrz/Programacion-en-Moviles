package com.tecsup.smartcatalog.data.mapper

import com.tecsup.smartcatalog.data.local.ItemEntity
import com.tecsup.smartcatalog.data.remote.dto.ItemDto
import com.tecsup.smartcatalog.domain.model.Item

fun ItemDto.toEntity(favorito: Boolean = false): ItemEntity =
    ItemEntity(
        id = id,
        nombre = title,
        descripcion = description,
        precio = price,
        imagen = thumbnail,
        marca = brand ?: "Sin marca",
        favorito = favorito
    )

fun ItemEntity.toDomain(): Item =
    Item(
        id = id,
        nombre = nombre,
        descripcion = descripcion,
        precio = precio,
        imagen = imagen,
        marca = marca,
        favorito = favorito
    )

fun ItemDto.toDomain(): Item =
    Item(
        id = id,
        nombre = title,
        descripcion = description,
        precio = price,
        imagen = thumbnail,
        marca = brand ?: "Sin marca"
    )
