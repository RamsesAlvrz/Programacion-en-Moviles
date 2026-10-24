package com.alvarez.lavacampus.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.alvarez.lavacampus.data.local.dao.DetallePedidoDao
import com.alvarez.lavacampus.data.local.dao.PedidoDao
import com.alvarez.lavacampus.data.local.dao.ServicioDao
import com.alvarez.lavacampus.data.local.dao.UsuarioDao
import com.alvarez.lavacampus.data.local.entity.DetallePedido
import com.alvarez.lavacampus.data.local.entity.Pedido
import com.alvarez.lavacampus.data.local.entity.Servicio
import com.alvarez.lavacampus.data.local.entity.Usuario

// Base de datos principal de LavaCampus
@Database(
    entities = [
        Usuario::class,
        Servicio::class,
        Pedido::class,
        DetallePedido::class
    ],
    version = 2,
    exportSchema = false
)
abstract class LavaDatabase : RoomDatabase() {

    // Acceso a las operaciones de usuarios
    abstract fun usuarioDao(): UsuarioDao

    // Acceso a las operaciones de servicios
    abstract fun servicioDao(): ServicioDao

    // Acceso a las operaciones de pedidos
    abstract fun pedidoDao(): PedidoDao

    // Acceso a las operaciones de detalles
    abstract fun detallePedidoDao(): DetallePedidoDao

    companion object {

        // Migración de la versión 1 a la versión 2.
        // Agrega las fechas de los estados sin eliminar pedidos existentes.
        val MIGRATION_1_2 = object : Migration(1, 2) {

            override fun migrate(db: SupportSQLiteDatabase) {

                // Fecha en que el pedido comenzó a procesarse
                db.execSQL(
                    "ALTER TABLE pedidos ADD COLUMN fechaInicioProceso INTEGER"
                )

                // Fecha en que el pedido quedó listo para recoger
                db.execSQL(
                    "ALTER TABLE pedidos ADD COLUMN fechaListo INTEGER"
                )

                // Fecha en que el pedido fue entregado
                db.execSQL(
                    "ALTER TABLE pedidos ADD COLUMN fechaEntregado INTEGER"
                )
            }
        }
    }
}