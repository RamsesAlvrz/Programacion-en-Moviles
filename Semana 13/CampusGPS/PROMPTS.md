
# Registro de prompts de Gemini — CampusGPS

**Proyecto:** CampusGPS  
**Rama:** mejora-ia-gps  
**Fase:** 2 — Mejoras asistidas por IA

---

## Objetivo

Utilizar Gemini en Android Studio como apoyo para mejorar el diseño y las funcionalidades de CampusGPS, manteniendo la arquitectura MVVM + Repository y conservando las funciones desarrolladas en la primera fase.

---

## Prompts utilizados

### Prompt 01 — Rediseño visual de CampusGPS

> Actúa como desarrollador Android senior especializado en Kotlin, Jetpack Compose, Material 3 y arquitectura MVVM + Repository.

#### CONTEXTO DEL PROYECTO

Estoy desarrollando CampusGPS, una aplicación Android de ubicación y exploración de lugares del campus que utiliza osmdroid (OpenStreetMap), FusedLocationProviderClient, StateFlow y MVVM + Repository. Estoy trabajando en la rama mejora-ia-gps. La Fase 1 está terminada y funciona correctamente.

#### OBJETIVO

Realiza directamente en el proyecto un rediseño visual profesional llamado CampusGPS 2.0, conservando todas las funcionalidades actuales.

#### 1. IDENTIDAD VISUAL

Implementa la paleta: verde esmeralda #115E59, turquesa #2DD4BF, fondo #F4F7F6, texto #172B2A y verde oscuro #083F3B. Centraliza los colores en Color.kt, configura esquemas claro y oscuro en Theme.kt y desactiva los colores dinámicos.

#### 2. REDISEÑO DE LA PANTALLA

Modifica PantallaMapa.kt: encabezado esmeralda con título CampusGPS y subtítulo 'Explora los lugares de tu campus'; mapa en tarjeta redondeada; etiqueta 'MAPA DEL CAMPUS'; botones 'Mi ubicación' y 'Más cercano'; indicador de carga; tarjeta destacada del más cercano; panel inferior con título 'Lugares del campus', contador y lista. Utiliza Material 3, espaciados consistentes y diseño adaptable.

#### 3. TARJETAS

Modifica ListaLugares.kt: tarjetas redondeadas con nombre, categoría, distancia, selección destacada y etiqueta 'MÁS CERCANO'. Conserva la selección y su conexión con el mapa.

#### 4. ERRORES

Modifica MensajeError.kt con tarjetas de error coherentes con el diseño y conserva las acciones 'Abrir ajustes' y 'Reintentar'.

#### 5. ARCHIVOS PERMITIDOS

- ui/theme/Color.kt
- ui/theme/Theme.kt
- ui/PantallaMapa.kt
- ui/ListaLugares.kt
- ui/MensajeError.kt

#### 6. RESTRICCIONES

No modifiques MapaViewmodel.kt, repositorios ni modelos. No elimines funciones. No sustituyas osmdroid por Google Maps. Conserva los cinco lugares, marcadores, distancias, selección, botón del más cercano y manejo de permisos, GPS apagado y ausencia de Google Play Services. Respeta MVVM + Repository. No agregues Context al ViewModel ni lógica de negocio a Composables. No modifiques local.properties ni expongas claves API. No realices commits ni cambies de rama.

#### 7. RESULTADO

Aplica los cambios directamente en los archivos indicados, sin duplicar implementaciones. Comprueba errores evidentes de compilación y resume los archivos modificados, decisiones de diseño y funcionalidades conservadas. Si los cambios ya existen y cumplen los requisitos, consérvalos y evita sobrescribirlos.

---

### Prompt 02 — Filtros por categorías de CampusGPS

> Actúa como desarrollador Android senior especializado en Kotlin, Jetpack Compose, Material 3, StateFlow y arquitectura MVVM + Repository.

#### CONTEXTO DEL PROYECTO

Estoy desarrollando CampusGPS 2.0, una aplicación Android que utiliza osmdroid (OpenStreetMap), FusedLocationProviderClient, Jetpack Compose y MVVM + Repository.

Estoy trabajando en la rama `mejora-ia-gps`. El rediseño visual profesional ya está terminado y funciona correctamente.

#### OBJETIVO

Implementa directamente en el proyecto un sistema de filtros por categorías que permita visualizar únicamente los lugares correspondientes a la categoría seleccionada, tanto en el mapa como en la lista inferior.

#### 1. CATEGORÍAS

Implementa cinco opciones:

- Todos
- Estudio
- Comida
- Eventos
- Servicios

La categoría predeterminada será Todos.

Utiliza los valores de categoría existentes en `LugaresRepositoryLocal.kt`. No modifiques los datos originales.

#### 2. LÓGICA EN EL VIEWMODEL

Modifica `MapaViewmodel.kt` para:

- Crear un `MutableStateFlow` privado que almacene la categoría seleccionada.
- Exponer el estado mediante `StateFlow`.
- Crear una lista filtrada reactiva a partir de los lugares existentes y la categoría seleccionada, utilizando `combine` y `stateIn`.
- Mantener intacta la lista original de lugares y sus distancias.
- Crear una función `seleccionarCategoria(categoria: String)` para actualizar el filtro.
- Limpiar la selección de un lugar si deja de pertenecer a la categoría visible.
- Conservar el cálculo y ordenamiento de distancias.
- Mantener el lugar más cercano global calculado con la lista original, pero permitir que el botón de lugar más cercano seleccione un lugar visible cuando exista un filtro activo.

#### 3. INTERFAZ DE FILTROS

Modifica `PantallaMapa.kt` para:

- Agregar una fila horizontal desplazable de chips debajo del encabezado y antes del mapa.
- Utilizar `FilterChip` de Material 3.
- Mostrar las cinco categorías.
- Destacar visualmente la categoría seleccionada con los colores del tema esmeralda.
- Mantener un diseño moderno, limpio y adaptable a pantallas pequeñas.
- Mostrar el contador de lugares según la categoría seleccionada.
- Conectar los chips con `seleccionarCategoria()` del ViewModel.

#### 4. FILTRADO DEL MAPA Y LA LISTA

La lista filtrada debe enviarse simultáneamente a:

- `MapaUbicacion`, para mostrar únicamente los marcadores de los lugares visibles.
- `ListaLugares`, para mostrar únicamente las tarjetas de los lugares visibles.

El marcador de la ubicación actual del usuario debe permanecer visible independientemente del filtro.

Los marcadores deben actualizarse al cambiar de categoría sin duplicarse ni quedarse marcadores de la categoría anterior.

Si una categoría no tiene resultados, muestra un mensaje informativo sin producir errores.

#### 5. ARCHIVOS PERMITIDOS

- `viewmodel/MapaViewmodel.kt`
- `ui/PantallaMapa.kt`
- `ui/MapaUbicacion.kt`, únicamente si es necesario para actualizar correctamente los marcadores.

#### 6. RESTRICCIONES OBLIGATORIAS

- No modifiques los repositorios ni los modelos.
- No cambies la biblioteca osmdroid por Google Maps.
- No modifiques la paleta ni el diseño profesional del COMMIT 11.
- No elimines funcionalidades existentes.
- Mantén MVVM + Repository.
- No realices el filtrado dentro de los Composables.
- No agregues Context al ViewModel.
- Conserva permisos, GPS, distancias, selección, errores y lugar más cercano.
- No implementes radio de cercanía, DataStore ni navegación; corresponden a otros commits.
- No modifiques `local.properties` ni claves API.
- No realices commits ni cambies de rama.

#### 7. RESULTADO ESPERADO

Aplica los cambios directamente en los archivos indicados.

Verifica que el proyecto compile y que al cambiar de categoría se actualicen simultáneamente la lista y los marcadores.

Al finalizar, proporciona un resumen de los archivos modificados, la lógica implementada y las funcionalidades conservadas.

---

## Registro de resultados y decisiones

| Prompt que escribí | Qué respondió Gemini | Qué cambié o descarté | Por qué |
|---|---|---|---|
| Prompt 01 — Rediseño visual de CampusGPS | Pendiente de registrar la respuesta real de Gemini. | Se actualizaron Color.kt, Theme.kt, PantallaMapa.kt, ListaLugares.kt y MensajeError.kt. Se conservó la lógica de ubicación. | Mejorar la presentación visual sin afectar las funciones existentes ni la arquitectura MVVM. |
| Prompt 02 — Filtros por categorías | Pendiente de registrar la respuesta real de Gemini. | Pendiente de verificar la implementación. | Permitir explorar lugares por categoría manteniendo sincronizados el mapa y la lista. |

---

## Verificación de las mejoras

### Commit 11 — Rediseño visual

**Estado:** Implementado y probado.

- Nueva identidad visual verde esmeralda.
- Encabezado y tarjetas rediseñados.
- Botones y mensajes de error renovados.
- Arquitectura MVVM + Repository conservada.
- Funcionalidades originales verificadas.

### Commit 12 — Filtros por categorías

**Estado:** Pendiente de pruebas.

**Archivos previstos:**

- `viewmodel/MapaViewmodel.kt`
- `ui/PantallaMapa.kt`

**Pruebas pendientes:**

- Mostrar los cinco lugares con Todos.
- Filtrar correctamente Estudio, Comida, Eventos y Servicios.
- Actualizar simultáneamente la lista y los marcadores.
- Mantener visible la ubicación del usuario.
- Conservar la selección y el botón Más cercano.
- Compilar y ejecutar sin errores.
