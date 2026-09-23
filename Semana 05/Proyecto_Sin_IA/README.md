# NavLab - Aplicación de Navegación con Jetpack Compose

## 1. Requerimientos Funcionales del Programa

- **RF01 - Permitir la navegación entre pantallas principales:** El sistema debe permitir al usuario desplazarse entre la pantalla de Inicio (`HomeScreen`), la Lista de elementos (`ListScreen`) y el Perfil (`ProfileScreen`) mediante botones interactivos.
- **RF02 - Visualizar una lista interactiva de elementos:** El sistema debe mostrar un listado vertical de 8 elementos numerados donde el usuario pueda seleccionar cualquiera de ellos.
- **RF03 - Consultar el detalle de un elemento seleccionado:** El sistema debe transferir el identificador (`itemId`) del elemento presionado y desplegar su información detallada en una pantalla independiente (`DetailScreen`).
- **RF04 - Permitir el retorno y gestión del historial de navegación:** El sistema debe permitir al usuario regresar a la pantalla anterior mediante el botón de retroceso (`popBackStack`) y volver al Inicio desde la pantalla de Perfil limpiando el historial de navegación (`popUpTo`).

---

## 2. ¿Qué hace la aplicación? (Funcionamiento)

**NavLab** es una aplicación móvil nativa diseñada para gestionar el flujo de navegación entre múltiples pantallas. El funcionamiento de la aplicación se divide en 4 secciones interconectadas:

1. **Pantalla de Inicio (`HomeScreen`):**
    - Presenta el título principal *"Pantalla Tecsup"*.
    - Muestra dos botones principales: uno para ingresar a la lista de elementos y otro para ir al perfil.

2. **Pantalla de Lista (`ListScreen`):**
    - Muestra un listado desplazable (*scroll*) con 8 ítems etiquetados del *"Elemento número 1"* al *"Elemento número 8"*.
    - Al tocar cualquier elemento, la aplicación abre la pantalla de detalle correspondiente a ese número.
    - Incluye una barra superior con una flecha $\leftarrow$ para regresar al Inicio.

3. **Pantalla de Detalle (`DetailScreen`):**
    - Recibe y muestra en pantalla el número específico del elemento que el usuario tocó en la lista (por ejemplo: *"Elemento #3"* e *"ID recibido: 3"*).
    - Muestra una tarjeta informativa indicando que el dato fue transferido correctamente.
    - Incluye una barra superior con flecha $\leftarrow$ para volver a la lista.

4. **Pantalla de Perfil (`ProfileScreen`):**
    - Muestra el nombre del usuario (*"Juan León Suiyon"*).
    - Incluye el botón *"Ir al inicio"*, el cual regresa a la pantalla principal limpiando el historial para no acumular pantallas abiertas en memoria.

---

## 3. Tecnologías Utilizadas

- **Lenguaje:** Kotlin
- **Framework UI:** Jetpack Compose
- **Diseño:** Material 3 (Tema violeta con `dynamicColor = false`)
- **Navegación:** Navigation Compose (`androidx.navigation:navigation-compose:2.7.7`)
- **Iconografía:** Material Icons Extended (`androidx.compose.material:material-icons-extended`)

---

## 4. Estructura del Proyecto (`com.alvarez.navlab`)



---

## 5. Capturas del resultado final



---



---



---

