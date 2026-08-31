# Laboratorio 02: Carrito de Compras en Kotlin (Enfoque POO con IA)

Estudiante: Ramsés Álvarez  
Curso: Programación en Móviles  
Institución: Tecsup

### Descripción del Proyecto
Este proyecto es una aplicación de consola en Kotlin que simula la gestión de un carrito de compras bajo el paradigma de Programación Orientada a Objetos (POO). A diferencia de la versión procedural previa, se implementó una arquitectura en capas (Modelo, Servicio y Vista) que abstrae y encapsula el comportamiento del sistema, permitiendo procesar distintos tipos de productos y formatear la salida en consola para un reporte comercial claro y modular.

### Funciones y Métodos POO Implementados:
* calcularImporte(): Método polimórfico en la jerarquía Producto. Cada subclase (ProductoEstandar y ProductoImportado) implementa su propia lógica para determinar el valor total del producto con o sin aranceles.
* calcularSubtotal(): Recorre la lista polimórfica List<Producto> en CarritoCompras e invoca el cálculo individual de cada ítem de forma dinámica.
* calcularIGV(subtotal: Double): Devuelve el 18% del subtotal tras la aplicación de los descuentos de ley.
* calcularPorcentajeDescuento(subtotal: Double): Encapsulado en EstrategiaDescuento, evalúa el monto acumulado usando estructuras when aplicadas según los rangos del negocio.
* imprimirComprobante(...): Método de GeneradorReporte encargado de formatear la salida en consola alineando columnas mediante String.format.
* maxByOrNull { it.calcularImporte() }: Identifica automáticamente el producto de mayor valor dentro de la colección.
* buscarProductoPorNombre(nombre: String): Utiliza la función idiomática .find para localizar ítems por coincidencia exacta.
* eliminarProductoPorNombre(nombre: String): Emplea la función idiomática .removeIf para la supresión dinámica de elementos de la lista.

---

## Aplicación de los 4 Pilares de la POO

1. Abstracción: Implementación de la clase abstracta base Producto (model/Producto.kt) que define el contrato del sistema mediante el método abstracto calcularImporte().
2. Encapsulamiento: Restricción de acceso directo a propiedades sensibles mediante private y protected. Uso de setters convalidados para asegurar precios base e importes válidos (>= 0).
3. Herencia: Creación de las clases derivadas ProductoEstandar y ProductoImportado, extendiendo los atributos generales de la clase madre Producto.
4. Polimorfismo: Sobrescritura (override) de los métodos calcularImporte() y obtenerDetalle() para adaptar la respuesta del cálculo según la naturaleza del producto (arancel de importación vs. venta local).

---

## Diferencia entre val y var
* val (Inmutable): Declara referencias cuyos valores o referencias no cambian una vez asignadas. En el proyecto se aplica al nombre del producto, la lista inmutable devuelta por obtenerProductos(), y las instancias fijas de los servicios (CarritoCompras, EstrategiaDescuento, GeneradorReporte).
* var (Mutable): Asigna variables cuyos valores pueden mutar a lo largo del tiempo de ejecución. En el proyecto se emplea para propiedades encapsuladas como el precioBase, la cantidad, la tasaArancel y los acumuladores temporales de montos en bucles.