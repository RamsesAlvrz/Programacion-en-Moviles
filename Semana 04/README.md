
# Laboratorio 04: Mi Carrito TECSUP

**Estudiante:** Ramses Alvarez  
**Curso:** Programación en Móviles  
**Docente:** Juan José León Suiyon  

---

## 📱 Capturas de Pantalla del Resultado Final

### 1. Estado Vacío
<img width="357" height="798" alt="image" src="https://github.com/user-attachments/assets/57c20452-b79b-4414-8025-3e61e9dd3e6b" />


### 2. Carrito con Productos y Totales Calculados
<img width="366" height="796" alt="image" src="https://github.com/user-attachments/assets/e977b84d-1041-483c-94ff-240fa402be56" />


---

## ❓ Respuestas Conceptuales

### (a) ¿Por qué `mutableStateListOf` y no una `MutableList` normal?
`mutableStateListOf` es una estructura de datos observable creada específicamente para Jetpack Compose. Cuando agregas o eliminas elementos, esta lista notifica automáticamente al motor de Compose para realizar una **recomposición** (redibujado) de la interfaz de usuario en tiempo real. Una `MutableList` estándar de Kotlin modifica los datos en memoria, pero no informa a Compose, por lo que la pantalla no se actualizaría.

### (b) ¿Por qué la lista se declara con `val`?
Se declara con `val` porque la **referencia** al objeto contenedor de la lista en memoria no cambia. La palabra clave `val` evita reasignar la variable a una lista totalmente nueva, pero sí permite modificar los elementos internos (añadir, editar o remover elementos) dentro de esa misma instancia de lista.

### (c) ¿Qué hace `weight(1f)` en la `LazyColumn`?
El modificador `weight(1f)` le indica a Compose que la `LazyColumn` debe ocupar **todo el espacio vertical disponible sobrante** dentro del layout `Column`. Esto permite que la lista de productos tenga un desplazamiento scrollable independiente y, a la vez, empuja el panel de resumen de totales para mantenerse fijo en la parte inferior de la pantalla sin solaparse.
