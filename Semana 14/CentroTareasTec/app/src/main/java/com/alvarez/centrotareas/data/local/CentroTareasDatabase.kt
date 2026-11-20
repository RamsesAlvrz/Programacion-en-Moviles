package com.alvarez.centrotareas.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [EjecucionEntity::class],
    version = 1,
    exportSchema = false
)
abstract class CentroTareasDatabase : RoomDatabase() {

    abstract fun ejecucionDao(): EjecucionDao
}
