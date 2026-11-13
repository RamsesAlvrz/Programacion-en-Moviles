package com.tecsup.smartcatalog

import android.content.Context
import com.tecsup.smartcatalog.data.local.AppDatabase
import com.tecsup.smartcatalog.data.remote.RetrofitClient
import com.tecsup.smartcatalog.data.repository.ItemRepository
import com.tecsup.smartcatalog.data.repository.ItemRepositoryImpl

class AppContainer(context: Context) {

    private val database = AppDatabase.obtenerInstancia(context)

    val itemRepository: ItemRepository = ItemRepositoryImpl(
        api = RetrofitClient.api,
        dao = database.itemDao(),
        context = context.applicationContext
    )
}
