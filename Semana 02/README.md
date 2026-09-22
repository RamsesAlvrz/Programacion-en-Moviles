# Laboratorio 02: Carrito de Compras en Kotlin

Estudiante: Ramses Alvarez  
Curso: Programación en Móviles  
Institución: Tecsup

### Descripción del Proyecto
Este proyecto es una aplicación de consola en Kotlin que simula la gestión de un carrito de compras. Permite almacenar productos mediante un modelo de datos orientado a objetos, formatear la salida en consola para un reporte claro y realizar diversos cálculos comerciales de forma modular.

### Funciones Implementadas:
* calcularSubtotal(productos: List<Producto>): Recorre la lista de productos y calcula la suma total multiplicando el precio por la cantidad de cada ítem.
* calcularIGV(subtotal: Double): Devuelve el 18% del subtotal correspondiente al impuesto general a las ventas.
* calcularTotal(subtotal: Double, igv: Double): Suma el subtotal y el IGV para obtener el costo bruto.
* calcularDescuento(total: Double): Evalúa el monto total usando condicionales `when` para aplicar un 10% si supera S/ 5000 o un 5% si supera S/ 3000.
* mostrarDetalle(productos: List<Producto>): Imprime los productos alineados en columnas usando "String.format".
* maxByOrNull { it.precio }: Identifica automáticamente el producto con el precio más alto dentro del carrito.

## Diferencia entre `val` y `var`
* val (Inmutable): Se utiliza para declarar variables cuyo valor no va a cambiar una vez asignado (equivalente a "final" en Java). En el proyecto se usa para el "nombre" y "precio" del producto, así como para el "nombreCliente".
* var (Mutable): Se utiliza para variables cuyo valor puede modificarse a lo largo del tiempo. En el proyecto se utiliza para la "cantidad" del producto y contadores como los acumuladores de totales o índices del bucle.

## Captura de la Consola
<img width="757" height="842" alt="image" src="https://github.com/user-attachments/assets/492c5403-3dff-4e6f-a9ee-a8a5b45a5074" />


