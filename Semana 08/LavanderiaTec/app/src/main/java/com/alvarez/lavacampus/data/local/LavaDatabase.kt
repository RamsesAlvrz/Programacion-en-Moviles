
package com.alvarez.lavacampus.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
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
    version = 1,
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
}
