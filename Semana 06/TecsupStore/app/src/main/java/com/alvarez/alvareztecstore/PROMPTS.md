# Registro de Prompts de IA — Laboratorio 06 (Fase 2)

**Proyecto:** AlvarezTecStore  
**Componentes involucrados:** `AppNavegacion.kt`, `AppDrawer.kt`, `TarjetaProducto.kt`  
**Objetivo:** Implementar un Badge con contador interactivo en la opción "Favoritos" del `NavigationDrawer`, reflejando las acciones realizadas desde el menú desplegable de cada producto.

---

## 📌 Prompt 1: Elevación de Estado (State Hoisting) para Favoritos

### Prompt enviado a la IA:
> **Rol:** Eres un desarrollador experto en Android con Jetpack Compose.  
> **Contexto:** Tengo una pantalla principal `AppNavegacion` que renderiza una lista de productos (`TarjetaProducto`) dentro de un `LazyColumn` y un panel lateral (`AppDrawer`) dentro de un `ModalNavigationDrawer`.  
> **Tarea:** Necesito conectar el callback `onAgregarFavorito` de `TarjetaProducto` con el contador que se mostrará en `AppDrawer`.  
> **Restricciones e Instrucciones:**
> 1. Aplica el patrón de *State Hoisting* en `AppNavegacion.kt`.
> 2. Declara un estado mutable de entero llamado `contadorFavoritos` inicializado en `0`.
> 3. Incrementa `contadorFavoritos` en 1 cada vez que se ejecute el evento `onAgregarFavorito` de una tarjeta.
> 4. Pasa `contadorFavoritos` como argumento a `AppDrawer`.
> 5. NO modifiques la estructura de colores, la barra superior ni la tipografía existente.

### Respuesta resumida de la IA:
La IA propuso declarar `var contadorFavoritos by remember { mutableIntStateOf(0) }` en `AppNavegacion.kt`. Luego, asignó la lambda `{ contadorFavoritos++ }` al parámetro `onAgregarFavorito` de `TarjetaProducto` y pasó `contadorFavoritos` a la firma del composable `AppDrawer`.

### Ajustes y decisiones aplicadas:
* Se usó `mutableIntStateOf` en lugar de `mutableStateOf` para optimizar el rendimiento de la recomposición con tipos primitivos enteros en Jetpack Compose.
* Se conservó la estructura de visualización sin alterar la lógica de navegación ni los estados preexistentes (`destinoSeleccionado`, `drawerState`).

---

## 📌 Prompt 2: Integración visual del Badge dinámico en el Drawer

### Prompt enviado a la IA:
> **Rol:** Eres un diseñador de interfaz de usuario y desarrollador experto en Material 3 para Jetpack Compose.  
> **Contexto:** Tengo un componente `AppDrawer.kt` que renderiza las opciones de navegación mediante `NavigationDrawerItem`.  
> **Tarea:** Agregar un indicador tipo `Badge` con un contador dinámico únicamente a la opción "Favoritos".  
> **Restricciones e Instrucciones:**
> 1. Añade el parámetro `contadorFavoritos: Int = 0` a la firma de `AppDrawer`.
> 2. Utiliza la propiedad `badge = { ... }` nativa del componente `NavigationDrawerItem`.
> 3. Renderiza el `Badge` **solo si** el título de la opción es "Favoritos" Y `contadorFavoritos > 0`.
> 4. El contenedor del `Badge` debe usar el color morado institucional `Color(0xFF522175)` y texto blanco en negrita.
> 5. Mantén intactos los íconos circulares (`CircleShape`) y los bordes personalizados de las demás opciones.

### Respuesta resumida de la IA:
La IA añadió la propiedad `badge` dentro del `forEach` de `itemsDrawer`. Mediante una condición lógica, renderizó el componente `Badge` de Material 3 conteniendo un `Text` con el valor formateado de `contadorFavoritos`.

### Ajustes y decisiones aplicadas:
* Se aseguró que cuando el contador sea `0`, el `Badge` no ocupe espacio visual ni sobrecargue la interfaz.
* Se mantuvo la coherencia estética con el color corporativo (`#522175`) para conservar la identidad visual del proyecto.