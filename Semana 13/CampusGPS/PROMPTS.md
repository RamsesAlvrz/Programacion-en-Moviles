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

### Prompt 03 — Radio de proximidad de CampusGPS

> Actúa como desarrollador Android senior especializado en Kotlin, Jetpack Compose, Material 3, StateFlow, osmdroid y arquitectura MVVM + Repository.

#### CONTEXTO DEL PROYECTO

Estoy desarrollando CampusGPS 2.0, una aplicación Android de ubicación y exploración de lugares del campus que utiliza osmdroid (OpenStreetMap), FusedLocationProviderClient, StateFlow y MVVM + Repository.

Estoy trabajando en la rama `mejora-ia-gps`.

La Fase 1 está terminada. El COMMIT 11 incorporó un rediseño visual profesional y el COMMIT 12 implementó filtros por categorías que actualizan simultáneamente el mapa y la lista.

#### OBJETIVO

Implementa directamente en el proyecto una mejora de proximidad que permita seleccionar radios de 100, 200 y 500 metros alrededor de la ubicación actual del usuario, visualizar el área correspondiente en el mapa y filtrar los lugares según su distancia.

Conserva todas las funcionalidades existentes.

#### 1. OPCIONES DE RADIO

Implementa cuatro opciones:

- Sin límite
- 100 m
- 200 m
- 500 m

La opción predeterminada será Sin límite.

Cuando se seleccione un radio, solo deben aparecer los lugares cuya distancia calculada sea menor o igual al valor seleccionado.

Cuando se seleccione Sin límite, deben mostrarse nuevamente todos los lugares que correspondan al filtro de categoría activo.

#### 2. LÓGICA EN EL VIEWMODEL

Modifica `MapaViewmodel.kt` para:

- Crear un `MutableStateFlow<Int?>` privado que almacene el radio seleccionado.
- Utilizar `null` para representar la opción Sin límite.
- Exponer el estado mediante `StateFlow`.
- Crear la función `seleccionarRadio(radio: Int?)`.
- Validar que los valores permitidos sean únicamente `null`, 100, 200 y 500.
- Combinar la lista de lugares, la categoría seleccionada y el radio mediante `combine` y `stateIn`.
- Mantener la lista original de lugares y sus distancias sin modificaciones.
- Aplicar simultáneamente los filtros de categoría y radio.
- Limpiar la selección si el lugar seleccionado deja de estar visible.
- Conservar el cálculo de distancias y el ordenamiento existente.
- Mantener el lugar más cercano global y el más cercano dentro de los resultados visibles.
- No realizar cálculos de distancia dentro de los Composables.

#### 3. INTERFAZ DE RADIO

Modifica `PantallaMapa.kt` para:

- Agregar una sección titulada "Radio de proximidad".
- Colocarla debajo de los filtros por categorías y antes del mapa.
- Mostrar chips de Material 3 para Sin límite, 100 m, 200 m y 500 m.
- Resaltar el radio seleccionado utilizando la paleta verde esmeralda.
- Permitir desplazamiento horizontal si es necesario.
- Conectar las opciones con `seleccionarRadio()` del ViewModel.
- Enviar el radio seleccionado al componente `MapaUbicacion`.
- Mantener el contador de lugares actualizado.
- Mostrar un mensaje informativo cuando no existan lugares dentro del radio.
- Conservar el diseño adaptable y el funcionamiento de los botones existentes.

#### 4. CÍRCULO DE PROXIMIDAD EN EL MAPA

Modifica `MapaUbicacion.kt` para:

- Recibir el radio seleccionado mediante un parámetro opcional.
- Dibujar un círculo de osmdroid centrado en la ubicación actual del usuario.
- Utilizar un borde verde esmeralda y un relleno semitransparente.
- Actualizar el círculo al cambiar entre 100, 200 y 500 metros.
- Eliminar el círculo cuando se seleccione Sin límite.
- Evitar la acumulación de círculos anteriores.
- Mantener visibles los marcadores correspondientes al filtro activo.
- Conservar el marcador de ubicación del usuario.
- Mantener la corrección del COMMIT 12 que cierra las ventanas de información de lugares ocultos.
- No sustituir osmdroid por Google Maps.

#### 5. ARCHIVOS PERMITIDOS

- `viewmodel/MapaViewmodel.kt`
- `ui/PantallaMapa.kt`
- `ui/MapaUbicacion.kt`

#### 6. RESTRICCIONES OBLIGATORIAS

- No modifiques los repositorios ni los modelos.
- No modifiques las dependencias del proyecto.
- No cambies la biblioteca osmdroid por Google Maps.
- No elimines los filtros por categorías del COMMIT 12.
- No modifiques la identidad visual del COMMIT 11.
- Conserva los cinco lugares originales.
- Mantén la arquitectura MVVM + Repository.
- No agregues Context al ViewModel.
- No realices filtrados ni cálculos de distancia dentro de los Composables.
- Conserva los permisos de ubicación, GPS, distancias, selección, errores y botón Más cercano.
- No implementes DataStore ni navegación; corresponden a otros commits.
- No modifiques `local.properties` ni expongas claves API.
- No realices commits ni cambies de rama.

#### 7. RESULTADO ESPERADO

Aplica los cambios directamente en los archivos indicados, sin duplicar implementaciones.

Verifica que el proyecto compile y que los radios de 100, 200 y 500 metros actualicen correctamente la lista, los marcadores y el círculo del mapa.

Comprueba que los filtros por categorías sigan funcionando junto con el radio.

Al finalizar, resume los archivos modificados, las decisiones técnicas y las funcionalidades conservadas.

Si los cambios ya existen y cumplen los requisitos, consérvalos y evita sobrescribirlos.

---

---

### Prompt 04 — Persistencia del último lugar seleccionado con DataStore

> Actúa como desarrollador Android senior especializado en Kotlin, Jetpack Compose, Preferences DataStore, StateFlow y arquitectura MVVM + Repository.

#### CONTEXTO DEL PROYECTO

Estoy desarrollando CampusGPS 2.0 en la rama `mejora-ia-gps`. El proyecto utiliza osmdroid (OpenStreetMap), FusedLocationProviderClient, StateFlow y MVVM + Repository. Los COMMIT 11, 12 y 13 incorporaron el rediseño visual, filtros por categorías y radios de 100, 200 y 500 metros.

#### OBJETIVO

Implementar Preferences DataStore para guardar el ID del último lugar seleccionado y recuperarlo al cerrar y volver a abrir CampusGPS, conservando todas las funcionalidades actuales.

#### 1. DEPENDENCIAS

- Declarar AndroidX DataStore Preferences en `gradle/libs.versions.toml`.
- Agregar la dependencia en `app/build.gradle.kts`.
- No modificar otras dependencias innecesariamente.

#### 2. REPOSITORIO DE PREFERENCIAS

Crear `data/PreferenciasRepository.kt` para:

- Definir una única instancia de DataStore con `preferencesDataStore` y `applicationContext`.
- Guardar el ID mediante `intPreferencesKey`.
- Exponer el último ID como `Flow<Int?>`.
- Implementar funciones suspendidas para guardar y limpiar el ID.
- Manejar los errores de almacenamiento sin bloquear la aplicación.

#### 3. INTEGRACIÓN CON EL VIEWMODEL

Modificar `MapaViewmodel.kt` para:

- Recibir `PreferenciasRepository` por constructor.
- Recuperar el último ID al iniciar el ViewModel y verificar que corresponda a uno de los cinco lugares existentes.
- Restaurar la selección en el `StateFlow` actual.
- Guardar el ID cuando se seleccione un lugar válido.
- Evitar que una recuperación tardía sobrescriba una selección nueva.
- Mantener el último ID guardado aunque el lugar deje de ser visible por un filtro.
- No perder permanentemente la selección al actualizar la ubicación.
- Conservar el cálculo de distancias, el ordenamiento, las categorías y los radios.
- No utilizar `Context` dentro del ViewModel ni acceder a DataStore desde los Composables.

#### 4. INYECCIÓN DE DEPENDENCIAS

- Modificar `CampusViewModelFactory.kt` para pasar el nuevo repositorio al ViewModel.
- Modificar `MainActivity.kt` para inicializar `PreferenciasRepository` con `applicationContext`.
- No agregar frameworks de inyección de dependencias.

#### 5. ARCHIVOS PERMITIDOS

- `gradle/libs.versions.toml`
- `app/build.gradle.kts`
- `data/PreferenciasRepository.kt`
- `viewmodel/MapaViewmodel.kt`
- `viewmodel/CampusViewModelFactory.kt`
- `MainActivity.kt`

#### 6. RESTRICCIONES OBLIGATORIAS

- Mantener MVVM + Repository y los cinco lugares originales.
- Conservar osmdroid, los filtros del COMMIT 12 y los radios del COMMIT 13.
- No modificar el diseño esmeralda del COMMIT 11.
- Conservar permisos, GPS, errores, marcadores y botón Más cercano.
- No utilizar SharedPreferences, Firebase, Room ni bases de datos remotas.
- No implementar aún el panel de detalles ni navegación del COMMIT 15.
- No modificar `local.properties` ni exponer claves API.
- No realizar commits ni cambiar de rama.
- Si los cambios ya existen y funcionan, evitar sobrescribirlos o duplicarlos.

#### 7. RESULTADO ESPERADO

Revisar los archivos reales del proyecto, aplicar únicamente los cambios necesarios y comprobar errores evidentes de compilación.

Verificar que la selección persista tras cerrar y reabrir la aplicación y que siga funcionando junto con categorías, radios y mapa. Al finalizar, resumir archivos modificados, decisiones técnicas, funcionalidades conservadas y pruebas pendientes.

---

### Prompt 05 — Rutas peatonales dentro de CampusGPS y panel de detalles

> Actúa como desarrollador Android senior especializado en Kotlin, Jetpack Compose, Material 3, osmdroid, OpenStreetMap, OSRM, corrutinas y arquitectura MVVM + Repository.

#### CONTEXTO DEL PROYECTO

Estoy desarrollando CampusGPS 2.0 en la rama `mejora-ia-gps`. Los COMMIT 11, 12, 13 y 14 incorporaron el rediseño visual, filtros por categorías, radio de proximidad y persistencia del último lugar seleccionado con Preferences DataStore.

La aplicación ya muestra la ubicación actual, cinco lugares del campus, marcadores, distancias en línea recta y selección de destinos. Quiero que el recorrido se dibuje **dentro de CampusGPS**, sin abrir Google Maps ni depender de una API de Google Maps de pago.

#### OBJETIVO

Implementar una ruta peatonal entre la ubicación actual y un lugar seleccionado, calculada con el servicio de enrutamiento OSRM para peatones basado en datos OpenStreetMap. Mostrar el recorrido en el mapa osmdroid y ofrecer las acciones de navegación desde una ventana inferior de detalles (`ModalBottomSheet`).

#### 1. REPOSITORIO DE RUTAS

Crear `data/RutaRepository.kt` con los modelos `PuntoRuta` y `RutaPeatonal`, una interfaz `RutaRepository` y su implementación `RutaRepositoryOSRM`.

- Consultar el servidor `https://routing.openstreetmap.de/routed-foot/route/v1/driving/` con las coordenadas de origen y destino en orden **longitud,latitud**.
- Utilizar `overview=full&geometries=geojson` para obtener la geometría completa del recorrido.
- Ejecutar la petición HTTP con `Dispatchers.IO`, tiempos de espera y cierre de la conexión.
- Interpretar el GeoJSON y devolver puntos, distancia en metros y duración estimada en segundos.
- Manejar errores HTTP, respuestas sin rutas y datos incompletos.
- No exponer claves API ni introducir dependencias de Google Maps.

#### 2. ESTADO Y LÓGICA EN MVVM

Crear `model/EstadoRuta.kt` con los estados `SinRuta`, `Cargando`, `Lista` y `Error`.

Modificar `viewmodel/MapaViewmodel.kt` para:

- Inyectar `RutaRepository` sin romper la fábrica actual del ViewModel.
- Exponer `estadoRuta` como `StateFlow`.
- Implementar `calcularRuta()` y `limpiarRuta()`.
- Calcular el recorrido desde la última ubicación obtenida hasta el lugar seleccionado y visible.
- Evitar solicitudes repetidas en intervalos demasiado cortos y cancelar las solicitudes obsoletas.
- Limpiar la ruta al cambiar de destino, ocultarlo con filtros o volver a obtener la ubicación.
- Conservar las categorías, radios, ordenamiento por distancia y DataStore.

#### 3. DIBUJAR LA RUTA EN EL MAPA

Modificar `ui/MapaUbicacion.kt` para:

- Recibir opcionalmente la ruta calculada.
- Dibujar sus puntos mediante `Polyline` de osmdroid con un trazo turquesa visible.
- Ajustar el encuadre del mapa para mostrar el recorrido completo, sin impedir que el usuario haga zoom o mueva el mapa.
- Eliminar polilíneas antiguas al limpiar o cambiar la ruta.
- Conservar el marcador de ubicación actual, los marcadores de lugares y el círculo de proximidad.
- Permitir que al tocar un marcador de lugar se notifique su ID a la pantalla principal.

#### 4. VENTANA INFERIOR DE DETALLES

Crear `ui/DetalleLugarSheet.kt` y modificar `ui/PantallaMapa.kt` para:

- Abrir un `ModalBottomSheet` de Material 3 al tocar un lugar en la lista, un marcador o el botón «Más cercano».
- Mostrar nombre, categoría y distancia directa del lugar seleccionado.
- Cuando exista una ruta calculada para ese destino, mostrar también distancia real del recorrido y duración aproximada a pie.
- Colocar los botones **«Cómo llegar»** y **«Limpiar ruta»** dentro del panel inferior, **no como botones fijos debajo del mapa**.
- Al pulsar «Cómo llegar», calcular el recorrido y cerrar el panel para permitir ver el mapa.
- Al pulsar «Limpiar ruta», quitar la polilínea sin eliminar los lugares ni sus filtros.
- Mostrar carga, resultado y errores del servicio de rutas de forma clara.
- No abrir automáticamente el panel al restaurar desde DataStore la selección guardada.

#### 5. ARCHIVOS PERMITIDOS

- `data/RutaRepository.kt` (nuevo)
- `model/EstadoRuta.kt` (nuevo)
- `ui/DetalleLugarSheet.kt` (nuevo)
- `ui/MapaUbicacion.kt`
- `ui/PantallaMapa.kt`
- `viewmodel/MapaViewmodel.kt`

#### 6. RESTRICCIONES OBLIGATORIAS

- Mantener la arquitectura MVVM + Repository y los cinco lugares originales.
- Conservar el diseño esmeralda, filtros por categorías, radios de 100/200/500 m y DataStore.
- Mantener los permisos de ubicación, errores de GPS y la opción «Mi ubicación».
- No sustituir osmdroid por Google Maps ni abrir una aplicación externa para calcular la ruta.
- No prometer navegación GPS en tiempo real ni precisión idéntica a Google Maps: el recorrido depende de los datos de OpenStreetMap y del servicio OSRM.
- No modificar `local.properties`, no agregar credenciales y no realizar commits ni cambiar de rama.
- Si alguna parte ya funciona, conservarla y modificar únicamente lo necesario.

#### 7. RESULTADO ESPERADO

Revisar el código existente y aplicar la mejora sin duplicar funcionalidades. Comprobar errores evidentes de compilación. Verificar que la selección desde lista y marcadores abra el panel, que «Cómo llegar» dibuje la ruta dentro de CampusGPS, que «Limpiar ruta» la retire y que sigan funcionando los filtros, los marcadores y DataStore.

---

### Prompt 06 — Pantalla de Inicio y navegación inferior de CampusGPS 2.0

> Actúa como desarrollador Android senior especializado en Kotlin, Jetpack Compose, Material 3, StateFlow y arquitectura MVVM + Repository.

#### CONTEXTO DEL PROYECTO

Estoy desarrollando CampusGPS 2.0 en la rama `mejora-ia-gps`. Los COMMIT 11 al 15 incorporaron el rediseño visual esmeralda, los filtros por categorías, los radios de proximidad, la persistencia del último lugar con Preferences DataStore y las rutas peatonales dentro del mapa con OSRM y osmdroid.

#### OBJETIVO

Crear una pantalla de Inicio profesional y una navegación inferior para acceder a las funciones existentes sin modificar la arquitectura MVVM + Repository ni romper el mapa y sus rutas.

#### 1. PANTALLA DE INICIO

Crear `ui/PantallaInicio.kt` con encabezado verde esmeralda, título «Tu campus, siempre cerca», descripción y cuatro tarjetas de acceso rápido:

- **Explorar el mapa:** abrir el mapa con categoría Todos y radio Sin límite.
- **Rutas a pie:** abrir el mapa y mostrar instrucciones para seleccionar un destino y usar «Cómo llegar».
- **Lugares cercanos:** abrir el mapa con categoría Todos y radio de 200 metros; solicitar ubicación si todavía no está disponible mediante el control existente.
- **Último lugar:** mostrar la selección recuperada desde el ViewModel y persistida mediante DataStore.

Utilizar tarjetas redondeadas, espaciados coherentes y la paleta existente. Evitar duplicar el botón «Explorar mapa» dentro del encabezado, porque ya existe una tarjeta y una pestaña de mapa.

#### 2. NAVEGACIÓN INFERIOR

Crear `ui/NavegacionCampus.kt` con `NavigationBar` de Material 3 y tres secciones: Inicio, Mapa y Último lugar. Conservar la misma instancia de `MapaViewModel` al cambiar de sección. Permitir volver a Inicio con el botón Atrás. Mantener los colores esmeralda y evitar indicadores con tonos ajenos a la aplicación.

#### 3. INTEGRACIÓN EN MAINACTIVITY

Modificar `MainActivity.kt` para presentar `NavegacionCampus` dentro de `CampusGPSTheme`, reutilizando la fábrica y los repositorios existentes. Configurar el dibujo detrás de la barra de estado mediante `WindowCompat` y utilizar iconos de estado claros sobre el fondo esmeralda.

#### 4. CORRECCIONES VISUALES

- Corregir la franja blanca superior para integrar visualmente el encabezado con la barra de estado.
- Mantener el encabezado verde y las tarjetas del mapa.
- Quitar el botón duplicado «Explorar mapa» del encabezado de Inicio.
- Mejorar la presentación de las tarjetas y los colores de la navegación inferior.
- Evitar elementos que tapen el título o desplacen innecesariamente el mapa.

#### 5. AYUDA PARA RUTAS A PIE

Modificar `NavegacionCampus.kt` y `PantallaMapa.kt` para que la ayuda no aparezca como una banda encima del encabezado. En su lugar, mostrar una tarjeta compacta **debajo del mapa y encima de los botones «Mi ubicación» y «Más cercano»** cuando el usuario ingrese desde «Rutas a pie».

- Mostrar el título «Ruta a pie» y una indicación para seleccionar un destino y pulsar «Cómo llegar».
- Permitir cerrar la tarjeta manualmente.
- Ocultarla automáticamente cuando el usuario seleccione un lugar.
- No mostrarla al entrar mediante «Explorar el mapa».
- Conservar `DetalleLugarSheet`, el cálculo OSRM y la polilínea de la ruta.

#### 6. ARCHIVOS PERMITIDOS

- `MainActivity.kt` (modificado)
- `ui/NavegacionCampus.kt` (nuevo)
- `ui/PantallaInicio.kt` (nuevo)
- `ui/PantallaMapa.kt` (modificado)

#### 7. RESTRICCIONES OBLIGATORIAS

- Conservar MVVM + Repository y los cinco lugares existentes.
- Mantener osmdroid y OpenStreetMap; no sustituirlos por Google Maps.
- No modificar el repositorio de rutas OSRM ni la persistencia con DataStore.
- Conservar categorías, radios, permisos de ubicación, marcadores, distancias, botón «Más cercano» y panel de detalles.
- No introducir nuevas dependencias innecesarias ni exponer credenciales.
- No modificar `local.properties`, cambiar de rama ni realizar commits automáticamente.

#### 8. RESULTADO ESPERADO

La aplicación debe abrir en Inicio, permitir navegar a Mapa y Último lugar, ejecutar acciones diferenciadas desde las tarjetas y mantener el mapa funcional. El encabezado debe conservar la identidad esmeralda sin franja blanca; la ayuda para rutas debe quedar debajo del mapa y desaparecer al seleccionar un destino. Verificar la compilación mediante Gradle y probar las pantallas en el emulador.

---

## Registro de resultados y decisiones

| Prompt que escribí | Qué respondió Gemini | Qué cambié o descarté | Por qué |
|---|---|---|---|
| Prompt 01 — Rediseño visual de CampusGPS | Pendiente de registrar la respuesta real de Gemini. | Se actualizaron Color.kt, Theme.kt, PantallaMapa.kt, ListaLugares.kt y MensajeError.kt. Se conservó la lógica de ubicación. | Mejorar la presentación visual sin afectar las funciones existentes ni la arquitectura MVVM. |
| Prompt 02 — Filtros por categorías | Pendiente de registrar la respuesta real de Gemini. | Se implementaron los filtros en MapaViewmodel.kt y PantallaMapa.kt. Se corrigió en MapaUbicacion.kt el cierre de ventanas de información anteriores. | Permitir explorar lugares por categoría manteniendo sincronizados el mapa y la lista. |
| Prompt 03 — Radio de proximidad | Pendiente de registrar la respuesta real de Gemini. | Cambios subidos a GitHub; pruebas funcionales aún sin confirmar. | Permitir consultar lugares cercanos dentro de un radio seleccionado, conservando los filtros por categorías. |
| Prompt 04 — Persistencia con DataStore | Pendiente de registrar la respuesta real de Gemini. | Implementación preparada; pendiente de compilación y pruebas. | Recordar el último lugar seleccionado entre aperturas de la aplicación. |
| Prompt 05 — Ruta peatonal y panel de detalles | Pendiente de registrar la respuesta real de Gemini. | Se integró OSRM con osmdroid, se agregó EstadoRuta y un ModalBottomSheet con las acciones «Cómo llegar» y «Limpiar ruta». | Permitir recorrer el campus dentro de la aplicación sin depender de Google Maps y mejorar la experiencia de selección de destinos. |

---

| Prompt 06 — Pantalla de Inicio y navegación | No se registró una respuesta de Gemini; los cambios se desarrollaron y ajustaron durante la implementación. | Se crearon PantallaInicio.kt y NavegacionCampus.kt; se modificaron MainActivity.kt y PantallaMapa.kt. Se eliminaron accesos duplicados, se integró la barra de estado y se movió la ayuda de rutas debajo del mapa. | Organizar el acceso a las funciones y mejorar la interfaz sin alterar los repositorios ni el trazado de rutas. |

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

**Estado:** Implementado y probado.

**Archivos modificados:**

- `viewmodel/MapaViewmodel.kt`
- `ui/PantallaMapa.kt`
- `ui/MapaUbicacion.kt`

**Pruebas realizadas:**

- Mostrar los cinco lugares con Todos.
- Filtrar Estudio, Comida, Eventos y Servicios.
- Actualizar simultáneamente la lista y los marcadores.
- Conservar la selección y el botón Más cercano.
- Corregir las ventanas de información anteriores.
- Ejecutar la aplicación sin errores visibles.

### Commit 13 — Radio de proximidad

**Estado:** Push realizado a GitHub; pruebas funcionales pendientes de confirmar.

**Archivos previstos:**

- `viewmodel/MapaViewmodel.kt`
- `ui/PantallaMapa.kt`
- `ui/MapaUbicacion.kt`

**Pruebas pendientes:**

- Mostrar todos los lugares con Sin límite.
- Dibujar un círculo de 100 metros.
- Dibujar un círculo de 200 metros.
- Dibujar un círculo de 500 metros.
- Mostrar únicamente los lugares dentro del radio.
- Combinar correctamente radio y categoría.
- Mantener visible la ubicación del usuario.
- Eliminar el círculo al seleccionar Sin límite.
- Evitar círculos y marcadores duplicados.
- Mantener el funcionamiento del botón Más cercano.
- Compilar y ejecutar sin errores.

### Commit 15 — Rutas peatonales y panel de detalles

**Estado:** Implementado y probado visualmente en Android Studio; commit y push pendientes de confirmar.

**Archivos creados:**

- `data/RutaRepository.kt`
- `model/EstadoRuta.kt`
- `ui/DetalleLugarSheet.kt`

**Archivos modificados:**

- `viewmodel/MapaViewmodel.kt`
- `ui/MapaUbicacion.kt`
- `ui/PantallaMapa.kt`

**Pruebas funcionales comunicadas:**

- Visualización del recorrido peatonal sobre el mapa de CampusGPS.
- Apertura del panel de detalles y uso de los controles de ruta.
- Presentación de la distancia de la ruta y duración estimada.
- Revisión visual de la interfaz actualizada.

**Verificaciones recomendadas antes del commit:**

- Ejecutar `./gradlew assembleDebug` o `./gradlew.bat assembleDebug` según el sistema operativo.
- Comprobar «Cómo llegar» desde la lista, los marcadores y «Más cercano».
- Comprobar «Limpiar ruta» y cambiar de destino.
- Revisar categorías, radios, ubicación y restauración de DataStore.
- Confirmar que el servicio OSRM responde con conexión a Internet.

**Decisión técnica:** OSRM proporciona el trazado peatonal a partir de OpenStreetMap y osmdroid lo representa en la aplicación. No se utiliza Google Maps como aplicación externa. La distancia en línea recta puede ser menor que la distancia real del recorrido.

```

---

### Commit 16 — Pantalla de Inicio y navegación inferior

**Estado:** Implementado; compilación y pruebas visuales comunicadas como satisfactorias. Commit y push pendientes de confirmar.

**Archivos creados:**

- `ui/NavegacionCampus.kt`
- `ui/PantallaInicio.kt`

**Archivos modificados:**

- `MainActivity.kt`
- `ui/PantallaMapa.kt`

**Mejoras implementadas:**

- Pantalla de Inicio con encabezado esmeralda y cuatro accesos rápidos.
- Navegación inferior entre Inicio, Mapa y Último lugar.
- Acceso al mapa con filtros restablecidos.
- Acceso a lugares cercanos con filtro de 200 metros.
- Visualización del último lugar seleccionado mediante el ViewModel.
- Corrección visual de la franja blanca de la barra de estado.
- Eliminación del botón «Explorar mapa» duplicado en el encabezado.
- Tarjeta de ayuda para rutas a pie debajo del mapa, con cierre manual y automático al seleccionar un lugar.

**Pruebas comunicadas:**

- Compilación correcta con Gradle.
- Pantalla de Inicio y navegación inferior visibles en el emulador.
- Revisión visual de la barra superior y los accesos rápidos.
- Ayuda de rutas reubicada y funcionando según confirmación del desarrollador.

**Decisión técnica:** Se reutilizó una sola instancia de `MapaViewModel` para las distintas secciones y se mantuvieron la lógica de ubicación, los filtros, osmdroid, OSRM y DataStore. La navegación entre secciones utiliza estado de Compose, sin introducir una dependencia adicional.

**Verificación final pendiente:** Confirmar `git status` limpio y el push del COMMIT 16 a `origin/mejora-ia-gps`.

````

---

### Prompt 07 — Ubicación real del campus, precisión de rutas y mejora del mapa (COMMIT 17)

> Actúa como desarrollador Android senior especializado en Kotlin, Jetpack Compose, osmdroid, OpenStreetMap, OSRM y arquitectura MVVM + Repository.

#### CONTEXTO DEL PROYECTO

Estoy desarrollando CampusGPS 2.0 en la rama `mejora-ia-gps`, dentro de `Semana 13/CampusGPS`. La aplicación utiliza OpenStreetMap con osmdroid, GPS de Android, rutas peatonales OSRM, filtros por categorías, radios de proximidad y DataStore.

#### OBJETIVO

Adaptar el mapa a TECSUP Santa Anita, Lima, mejorar la presentación de las rutas y dar más espacio al mapa sin eliminar las funciones implementadas anteriormente.

#### 1. UBICACIÓN INICIAL DEL MAPA

- Centrar inicialmente el mapa en el entorno de TECSUP Santa Anita, Lima, Perú.
- Mantener el botón «Mi ubicación» para centrar el mapa en la posición GPS del usuario.
- Conservar el marcador rojo de ubicación y los marcadores de los lugares.

#### 2. LUGARES DEL CAMPUS

Actualizar el repositorio local con cinco lugares de referencia: Biblioteca Central, Cafetería Campus, Laboratorio de Cómputo, Auditorio Principal y Estacionamiento Norte. Usar coordenadas aproximadas de referencia, sin presentarlas como levantamiento geográfico exacto.

#### 3. PRECISIÓN DE LAS RUTAS

- Mantener el cálculo de rutas peatonales mediante OSRM y su dibujo en osmdroid.
- Comprobar la separación entre el punto final devuelto por el servicio y el marcador de destino.
- Informar cuando la ruta termine cerca del lugar, pero no exactamente sobre el marcador.
- Ajustar el punto de referencia de Estacionamiento Norte para mejorar la llegada por caminos disponibles en OpenStreetMap.
- Mantener distancia de ruta, duración estimada y la opción de limpiar el recorrido.

#### 4. MEJORAS DE INTERFAZ DEL MAPA

- Ampliar el espacio visible del mapa.
- Convertir «Lugares del campus» en una lista desplegable, inicialmente contraída.
- Añadir un control de mapa ampliado que oculte temporalmente filtros, controles y lista, permitiendo volver a la vista normal.
- Conservar los filtros por categoría, radio, navegación inferior, marcadores interactivos y panel de detalles.

#### 5. ARCHIVOS INVOLUCRADOS

- `data/LugaresRepositoryLocal.kt`
- `data/RutaRepository.kt`
- `ui/MapaUbicacion.kt`
- `ui/PantallaMapa.kt`

#### 6. RESTRICCIONES

No eliminar funciones existentes, no sustituir osmdroid por Google Maps, conservar MVVM + Repository, DataStore, filtros, navegación y diseño esmeralda. No introducir cambios innecesarios en otros archivos.

#### 7. RESULTADO OBTENIDO

Se ajustó el mapa al entorno de TECSUP Santa Anita, se actualizaron los cinco lugares de referencia y se mejoró la información sobre el final de las rutas. También se incorporaron una lista desplegable y un modo de mapa ampliado. Se probó la ubicación GPS simulada desde el emulador y se confirmó el push del COMMIT 17.

---

### Prompt 08 — Ilustración personalizada y optimización visual de Inicio (COMMIT 18)

> Actúa como desarrollador Android senior especializado en Kotlin, Jetpack Compose, Material 3, diseño UI/UX y arquitectura MVVM + Repository.

#### CONTEXTO DEL PROYECTO

Estoy desarrollando CampusGPS 2.0 en la rama `mejora-ia-gps`, dentro de `Semana 13/CampusGPS`. La aplicación ya tiene pantalla Inicio, navegación inferior, ubicación GPS, cinco lugares del campus, filtros por categoría y radio, rutas peatonales OSRM y persistencia con DataStore.

#### OBJETIVO

Mejorar la presentación visual de la pantalla Inicio mediante una ilustración personalizada relacionada con TECSUP y la navegación GPS, optimizando el encabezado y las tarjetas sin alterar las funciones existentes.

#### 1. ILUSTRACIÓN PERSONALIZADA

- Crear una ilustración moderna de un campus educativo con edificios, estudiantes, áreas verdes, un mapa digital, rutas y marcadores GPS.
- Usar una paleta coherente con CampusGPS: verde esmeralda, turquesa, verde menta y blanco.
- Diseñar una composición horizontal adecuada para el encabezado de una aplicación móvil.
- Evitar detalles pequeños que dificulten la lectura en pantalla reducida.
- Guardar el recurso definitivo como `app/src/main/res/drawable/campus_ilustracion.png`.

#### 2. INTEGRACIÓN EN JETPACK COMPOSE

Modificar `ui/PantallaInicio.kt` para mostrar la imagen con `Image`, `painterResource`, `ContentScale.Crop`, `Modifier.fillMaxWidth()`, altura controlada y `RoundedCornerShape`.

Colocar la ilustración debajo del título «Tu campus, siempre cerca» y de su descripción, dentro del encabezado esmeralda.

#### 3. OPTIMIZACIÓN DEL ENCABEZADO

- Conservar «CAMPUSGPS 2.0», el título y la descripción.
- Reducir los márgenes superior e inferior y el espacio entre componentes.
- Ajustar el tamaño de la tipografía y el interlineado.
- Usar una altura aproximada de 155 dp para la ilustración.
- Mantener los bordes inferiores redondeados.
- Evitar que el encabezado ocupe demasiado espacio vertical.

#### 4. TARJETAS DE ACCESO RÁPIDO

Mantener las cuatro opciones existentes y sus acciones:

1. **Explorar el mapa:** mostrar los lugares del campus.
2. **Rutas a pie:** acceder al mapa para seleccionar un destino y calcular la ruta.
3. **Lugares cercanos:** consultar lugares dentro de un radio de 200 metros.
4. **Último lugar:** consultar el destino seleccionado anteriormente.

Reducir ligeramente los espacios y dimensiones de las tarjetas, manteniendo sus títulos, descripciones, íconos y flechas. Conservar el desplazamiento vertical.

#### 5. ÚLTIMO DESTINO

Conservar la sección de último lugar con nombre, categoría, distancia directa, botón «Abrir mapa» y mensaje cuando no existe selección. Mantener su integración con el ViewModel y DataStore.

#### 6. AJUSTES Y DECISIONES DEL DESARROLLO

Se creó una primera ilustración y se integró en Inicio. Tras revisarla en el emulador, se decidió sustituirla por una segunda versión mejor adaptada y compactar el encabezado y las tarjetas.

También se generó y probó un logo personalizado mediante Image Asset, pero finalmente se descartó. Se restauró un ícono genérico de Android. Se decidió no implementar el Splash Screen personalizado.

Por lo tanto, **el logo personalizado y el Splash Screen no forman parte de las mejoras definitivas del COMMIT 18**.

#### 7. ARCHIVOS DE LA MEJORA DEFINITIVA

- `ui/PantallaInicio.kt`
- `res/drawable/campus_ilustracion.png`

Antes de confirmar el commit, revisar `git status` para identificar y excluir o restaurar los recursos adicionales que haya dejado Image Asset. No incluir documentación en el COMMIT 18.

#### 8. RESTRICCIONES

- Conservar MVVM + Repository.
- No modificar repositorios, ViewModel, lógica de GPS ni rutas.
- Mantener los cinco lugares, categorías, radios, osmdroid, OSRM y DataStore.
- Conservar la navegación inferior y la paleta esmeralda.
- No añadir dependencias innecesarias.
- No implementar Splash Screen.
- No subir recursos del logo personalizado descartado.
- Reservar la actualización de `PROMPTS.md` para el COMMIT 19.

#### 9. RESULTADO OBTENIDO

Se incorporó una nueva ilustración a la pantalla Inicio y se compactaron el encabezado y las tarjetas. El resultado visual fue revisado y aprobado en el emulador. Se mantuvieron los cuatro accesos rápidos y la sección de último lugar. Se descartaron el logo personalizado y el Splash Screen. El cierre del COMMIT 18 queda sujeto a la revisión final de los archivos modificados y al push.

---

## Registro complementario de resultados y decisiones

| Prompt | Qué se implementó | Qué se ajustó o descartó | Motivo |
|---|---|---|---|
| Prompt 07 — Ubicación, rutas y mapa | Mapa centrado en TECSUP Santa Anita, cinco lugares de referencia, información de separación al destino, lista desplegable y modo ampliado. | Ajuste de Estacionamiento Norte y pruebas con GPS simulado. | Mejorar la utilidad y visualización del mapa dentro del campus. |
| Prompt 08 — Ilustración y pantalla Inicio | Ilustración personalizada, encabezado más compacto y tarjetas ajustadas. | Se sustituyó la primera ilustración; se descartaron el logo personalizado y el Splash Screen. | Mantener un diseño atractivo sin agregar complejidad innecesaria. |

---

### Commit 17 — Ubicación del campus, rutas y visualización del mapa

**Estado:** Commit y push confirmados por el desarrollador.

**Archivos modificados:**
- `data/LugaresRepositoryLocal.kt`
- `data/RutaRepository.kt`
- `ui/MapaUbicacion.kt`
- `ui/PantallaMapa.kt`

**Resultados:** mapa adaptado al entorno del campus, cinco referencias, mejora de precisión de rutas, lista desplegable y mapa ampliado. Se verificó el funcionamiento con GPS simulado.

### Commit 18 — Ilustración y optimización de Inicio

**Estado:** Cambios visuales implementados y aprobados; pendiente de verificar `git status`, realizar commit y push.

**Archivos previstos:**
- `ui/PantallaInicio.kt`
- `res/drawable/campus_ilustracion.png`

**Verificaciones antes del commit:**
- Comprobar que Inicio muestre correctamente la nueva ilustración.
- Confirmar que los cuatro accesos rápidos funcionen.
- Comprobar la sección Último lugar.
- Revisar que el mapa, las rutas y la navegación sigan operativos.
- Excluir o restaurar cualquier recurso no deseado generado durante la prueba del ícono.
- Confirmar que `PROMPTS.md` no se incluya en este commit.

**Decisión:** El registro actualizado de prompts y las conclusiones se subirán en el COMMIT 19 de documentación.

