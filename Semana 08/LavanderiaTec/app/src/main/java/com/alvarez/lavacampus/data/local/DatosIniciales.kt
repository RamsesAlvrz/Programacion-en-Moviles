package com.alvarez.lavacampus.data.local

import com.alvarez.lavacampus.data.local.entity.Servicio
import com.alvarez.lavacampus.data.local.entity.Usuario
import com.alvarez.lavacampus.data.local.entity.Pedido
import com.alvarez.lavacampus.data.local.entity.DetallePedido

// Datos de prueba definidos para el laboratorio LavaCampus
object DatosIniciales {

    // Usuarios que deben existir al iniciar la aplicación
    val usuarios = listOf(
        Usuario(
            nombre = "Ana",
            codigo = "EST001",
            correo = "ana@correo.test",
            clave = "1234",
            rol = "CLIENTE"
        ),
        Usuario(
            nombre = "Luis",
            codigo = "EST002",
            correo = "luis@correo.test",
            clave = "1234",
            rol = "CLIENTE"
        ),
        Usuario(
            nombre = "Marta",
            codigo = "EST003",
            correo = "marta@correo.test",
            clave = "1234",
            rol = "CLIENTE"
        ),
        Usuario(
            nombre = "Administrador",
            codigo = "ADMIN001",
            correo = "admin@lavacampus.test",
            clave = "admin",
            rol = "ADMIN"
        )
    )

    // Catálogo de servicios solicitado por la guía
    val servicios = listOf(
        Servicio(
            nombre = "Lavado y secado",
            descripcion = "Lavado y secado de ropa por kilogramo",
            unidad = "KG",
            precio = 8.0,
            horasEntrega = 24,
            activo = true,
            icono = "wash"
        ),
        Servicio(
            nombre = "Planchado",
            descripcion = "Planchado de prendas de vestir",
            unidad = "PRENDA",
            precio = 2.5,
            horasEntrega = 48,
            activo = true,
            icono = "iron"
        ),
        Servicio(
            nombre = "Lavado en seco",
            descripcion = "Limpieza especial de prendas delicadas",
            unidad = "PRENDA",
            precio = 12.0,
            horasEntrega = 48,
            activo = true,
            icono = "shirt"
        ),
        Servicio(
            nombre = "Edredón / frazada",
            descripcion = "Lavado de edredones y frazadas por unidad",
            unidad = "UNIDAD",
            precio = 20.0,
            horasEntrega = 48,
            activo = false,
            icono = "bed"
        )
    )

    // Pedidos de ejemplo para probar los diferentes estados
    fun obtenerPedidosIniciales(
        anaId: Int,
        luisId: Int,
        martaId: Int
    ): List<Pedido> {

        val hoy = java.time.LocalDate.now()
        val formato = java.time.format.DateTimeFormatter.ISO_LOCAL_DATE

        fun fecha(dias: Long): String {
            return hoy.plusDays(dias).format(formato)
        }

        return listOf(
            Pedido(
                usuarioId = anaId,
                fechaRegistro = fecha(-5),
                fechaEntrega = fecha(-4),
                estado = "ENTREGADO",
                total = 24.0,
                pagado = true,
                nota = "Pedido entregado"
            ),
            Pedido(
                usuarioId = anaId,
                fechaRegistro = fecha(-3),
                fechaEntrega = fecha(-1),
                estado = "LISTO",
                total = 16.0,
                casillero = "C-05",
                nota = "Listo para recoger"
            ),
            Pedido(
                usuarioId = anaId,
                fechaRegistro = fecha(-1),
                fechaEntrega = fecha(1),
                estado = "EN_PROCESO",
                total = 12.0
            ),
            Pedido(
                usuarioId = luisId,
                fechaRegistro = fecha(-6),
                fechaEntrega = fecha(-4),
                estado = "ENTREGADO",
                total = 20.0,
                pagado = true
            ),
            Pedido(
                usuarioId = luisId,
                fechaRegistro = fecha(-2),
                fechaEntrega = fecha(0),
                estado = "LISTO",
                total = 25.0,
                casillero = "C-07"
            ),
            Pedido(
                usuarioId = luisId,
                fechaRegistro = fecha(-1),
                fechaEntrega = fecha(1),
                estado = "RECIBIDO",
                total = 8.0
            ),
            Pedido(
                usuarioId = martaId,
                fechaRegistro = fecha(-7),
                fechaEntrega = fecha(-5),
                estado = "ENTREGADO",
                total = 12.0,
                pagado = true
            ),
            Pedido(
                usuarioId = martaId,
                fechaRegistro = fecha(-4),
                fechaEntrega = fecha(-2),
                estado = "ENTREGADO",
                total = 16.0,
                pagado = true
            ),
            Pedido(
                usuarioId = martaId,
                fechaRegistro = fecha(-2),
                fechaEntrega = fecha(0),
                estado = "EN_PROCESO",
                total = 24.0
            ),
            Pedido(
                usuarioId = martaId,
                fechaRegistro = fecha(0),
                fechaEntrega = fecha(2),
                estado = "RECIBIDO",
                total = 8.0
            )
        )
    }

    // Detalles de los pedidos de prueba.
    // Se utiliza el orden de los 10 pedidos iniciales.
    fun obtenerDetallesIniciales(
        numeroPedido: Int,
        pedidoId: Int,
        lavadoId: Int,
        planchadoId: Int,
        lavadoSecoId: Int,
        edredonId: Int
    ): List<DetallePedido> {

        // Crea un detalle calculando automáticamente su subtotal
        fun detalle(
            servicioId: Int,
            cantidad: Double,
            precio: Double
        ): DetallePedido {
            return DetallePedido(
                pedidoId = pedidoId,
                servicioId = servicioId,
                cantidad = cantidad,
                precioUnitario = precio,
                subtotal = cantidad * precio
            )
        }

        return when (numeroPedido) {
            1 -> listOf(detalle(lavadoId, 3.0, 8.0))
            2 -> listOf(detalle(lavadoId, 2.0, 8.0))
            3 -> listOf(detalle(lavadoSecoId, 1.0, 12.0))
            4 -> listOf(detalle(edredonId, 1.0, 20.0))
            5 -> listOf(detalle(planchadoId, 10.0, 2.5))
            6 -> listOf(detalle(lavadoId, 1.0, 8.0))
            7 -> listOf(detalle(lavadoSecoId, 1.0, 12.0))
            8 -> listOf(detalle(lavadoId, 2.0, 8.0))
            9 -> listOf(detalle(lavadoId, 3.0, 8.0))
            10 -> listOf(detalle(lavadoId, 1.0, 8.0))
            else -> emptyList()
        }
    }
}
