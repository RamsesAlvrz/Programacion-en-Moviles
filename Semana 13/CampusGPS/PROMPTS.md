
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
