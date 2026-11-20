# CampusGPS 2.0 — Fase 2: Mejoras con IA

## Descripción de la fase

En esta segunda fase trabajé en la rama `mejora-ia-gps` para mejorar CampusGPS con apoyo de herramientas de inteligencia artificial. El objetivo fue hacer que la aplicación sea más fácil de usar, tenga una presentación más ordenada y ofrezca nuevas opciones para explorar los lugares del campus.

Las mejoras se realizaron por etapas y se organizaron en commits separados. Se mantuvo la arquitectura **MVVM + Repository** durante el desarrollo.

## Mejoras realizadas

### Commit 11 — Rediseño visual

Se cambió la apariencia de la aplicación con una paleta verde esmeralda, turquesa y fondos claros. Se mejoraron el encabezado, las tarjetas de lugares, los botones y los mensajes de error usando Jetpack Compose y Material 3.

**Archivos principales:** `ui/theme/Color.kt`, `ui/theme/Theme.kt`, `ui/PantallaMapa.kt`, `ui/ListaLugares.kt` y `ui/MensajeError.kt`.

### Commit 12 — Filtros por categorías

Se agregaron los filtros **Todos, Estudio, Comida, Eventos y Servicios**. Al seleccionar una categoría, se actualizan tanto los marcadores del mapa como la lista de lugares. La lógica de filtrado se maneja desde el ViewModel con StateFlow.

**Archivos principales:** `viewmodel/MapaViewmodel.kt`, `ui/PantallaMapa.kt` y `ui/MapaUbicacion.kt`.

### Commit 13 — Radio de proximidad

Se incorporaron las opciones **Sin límite, 100 m, 200 m y 500 m** para consultar lugares según su distancia a la ubicación actual. El mapa dibuja un círculo que representa el radio elegido. Este filtro funciona junto con el filtro por categorías.

**Archivos principales:** `viewmodel/MapaViewmodel.kt`, `ui/PantallaMapa.kt` y `ui/MapaUbicacion.kt`.

### Commit 14 — Último lugar con DataStore

Se agregó **Preferences DataStore** para guardar el último lugar seleccionado y recuperarlo al volver a abrir la aplicación. Se creó un repositorio de preferencias y se conectó con el ViewModel sin guardar datos directamente desde la interfaz.

**Archivos principales:** `data/PreferenciasRepository.kt`, `viewmodel/MapaViewmodel.kt`, `viewmodel/CampusViewModelFactory.kt` y `MainActivity.kt`, además de los archivos de dependencias.

### Commit 15 — Rutas peatonales y detalles

Se incorporó el cálculo de **rutas a pie mediante OSRM**, utilizando los datos de OpenStreetMap. La ruta se dibuja dentro del mapa con osmdroid. También se añadió un panel inferior con información del destino y las opciones **Cómo llegar** y **Limpiar ruta**.

**Archivos principales:** `data/RutaRepository.kt`, `model/EstadoRuta.kt`, `ui/DetalleLugarSheet.kt`, `ui/MapaUbicacion.kt`, `ui/PantallaMapa.kt` y `viewmodel/MapaViewmodel.kt`.

### Commit 16 — Pantalla Inicio y navegación inferior

Se creó una pantalla Inicio con cuatro accesos rápidos: **Explorar el mapa, Rutas a pie, Lugares cercanos y Último lugar**. También se agregó una barra de navegación inferior para cambiar entre Inicio, Mapa y Último lugar. Se ajustó la barra superior y se ubicó la ayuda de rutas debajo del mapa.

**Archivos principales:** `ui/PantallaInicio.kt`, `ui/NavegacionCampus.kt`, `ui/PantallaMapa.kt` y `MainActivity.kt`.

### Commit 17 — Mejoras del mapa y ubicación del campus

Se ajustó el mapa al entorno de **TECSUP Santa Anita** y se actualizaron cinco lugares de referencia. También se mejoró la información sobre la llegada de las rutas, se agregó una **lista de lugares desplegable** y una opción para **ampliar el mapa**, ocultando temporalmente algunos controles.

**Archivos principales:** `data/LugaresRepositoryLocal.kt`, `data/RutaRepository.kt`, `ui/MapaUbicacion.kt` y `ui/PantallaMapa.kt`.

### Commit 18 — Ilustración y mejoras de Inicio

Se incorporó una ilustración personalizada relacionada con el campus y la navegación GPS. Además, se redujo el tamaño del encabezado y se ajustaron los espacios de las tarjetas para aprovechar mejor la pantalla.

Durante esta etapa también se probó un ícono personalizado, pero se decidió descartarlo y mantener un ícono genérico de Android. Tampoco se añadió un Splash Screen personalizado.

**Archivos principales:** `ui/PantallaInicio.kt` y `res/drawable/campus_ilustracion.png`.

## Cómo se utilizó la IA

La IA se utilizó como apoyo para proponer cambios de diseño, organizar las mejoras y revisar implementaciones en Kotlin y Jetpack Compose. Cada propuesta se fue ajustando según cómo se veía y funcionaba la aplicación en Android Studio. No se aceptaron todas las ideas: algunas se corrigieron y otras, como el logo personalizado y el Splash Screen, se descartaron.

Los prompts utilizados, las restricciones y las decisiones de cada etapa están registrados en el archivo [`PROMPTS.md`](PROMPTS.md).

## Organización de las mejoras

Para mantener el proyecto ordenado, las funciones se incorporaron por commits: primero el diseño, después los filtros y el radio, luego DataStore y las rutas, y finalmente la navegación y los ajustes visuales. Esto facilitó identificar qué se había cambiado en cada etapa y comprobar que las mejoras anteriores siguieran funcionando.

## Conclusiones

1. En esta fase aprendí que la IA puede ayudar a mejorar una aplicación, pero igual es necesario revisar el código y probar los cambios en Android Studio para comprobar que funcionen como se espera.

2. Al agregar filtros, radios, DataStore y rutas peatonales entendí mejor cómo incorporar nuevas funciones sin mezclar toda la lógica con las pantallas, manteniendo la arquitectura MVVM + Repository.

3. También comprobé que una buena interfaz hace que las funciones sean más fáciles de encontrar. Con la pantalla Inicio, la navegación inferior, el mapa ampliado y la ilustración, CampusGPS quedó más organizado y cómodo de utilizar.
