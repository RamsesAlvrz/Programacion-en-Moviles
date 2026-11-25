
# MuroCampus - Fase 2

## Descripción

MuroCampus es una aplicación móvil desarrollada en Kotlin
y Jetpack Compose que permite consultar, crear, buscar,
editar y eliminar publicaciones mediante la API REST
de JSONPlaceholder.

En esta segunda fase se incorporaron mejoras funcionales
y visuales con asistencia de inteligencia artificial,
manteniendo la arquitectura MVVM + Repository.

## Tecnologías utilizadas

- Kotlin
- Jetpack Compose
- Material Design 3
- Retrofit
- OkHttp
- Kotlin Serialization
- Coroutines
- StateFlow
- JSONPlaceholder
- Android Studio
- Git y GitHub

## Mejoras implementadas

### 1. Búsqueda de publicaciones por título

Se agregó un buscador que permite encontrar publicaciones
mediante palabras contenidas en sus títulos.

- Búsqueda en tiempo real.
- Filtrado local de publicaciones.
- Búsqueda combinada con el filtro por usuario.
- Contador de resultados.
- Opción para limpiar la búsqueda.
- Mensaje cuando no existen coincidencias.

Capas modificadas: ViewModel y View.

### 2. Interceptor con encabezado Accept

Se implementó un interceptor de OkHttp que incorpora
automáticamente el siguiente encabezado HTTP:

    Accept: application/json

Esta configuración se centralizó en RetrofitClient,
evitando repetir el encabezado en cada método de la API.

El encabezado se verificó mediante Logcat.

### 3. Eliminación de publicaciones con DELETE

Se incorporó la opción de eliminar publicaciones
desde la pantalla de detalle.

- Botón de eliminación.
- Ventana de confirmación.
- Manejo de respuestas HTTP.
- Estados de carga, éxito y error.
- Actualización local del listado.
- Mensaje de confirmación.

Capas modificadas: API, Repository, ViewModel y View.

### 4. Edición de publicaciones con PATCH

Se implementó una pantalla que permite modificar
el título y contenido de una publicación existente.

- Campos precargados con los datos actuales.
- Validación de campos obligatorios.
- Actualización mediante PATCH.
- Manejo de estados y errores.
- Actualización local de los datos.
- Mensaje de confirmación.

Capas modificadas: Model, API, Repository,
ViewModel y View.

### 5. Rediseño profesional de la interfaz

Se personalizó la apariencia de MuroCampus para
conseguir una identidad visual propia.

Principales cambios:

- Paleta de colores personalizada.
- Jerarquía tipográfica.
- Barra superior personalizada.
- Iconos propios.
- Tarjetas con bordes redondeados.
- Avatares con colores diferenciados.
- Botones de edición y eliminación organizados.
- Rediseño de los estados de carga y error.
- Comentarios con avatares, numeración y separadores.
- Contador de comentarios.
- Mejora de las ventanas de confirmación.

También se corrigió la interacción de las tarjetas,
permitiendo acceder al detalle únicamente mediante
la sección Ver publicación y su flecha.

## Identidad visual

| Elemento | Color |
|---|---|
| Azul principal | #176B98 |
| Azul oscuro | #124B70 |
| Azul claro | #E4EEF5 |
| Fondo general | #F1F5F9 |
| Superficie | #FFFFFF |
| Texto principal | #202B38 |
| Texto secundario | #64748B |
| Error | #B3261E |
| Éxito | #287D46 |

## Arquitectura del proyecto

Se mantuvo la arquitectura MVVM + Repository
para separar las responsabilidades de cada capa.

    View (Jetpack Compose)
              |
              v
         ViewModel
              |
              v
         Repository
              |
              v
           PostApi
              |
              v
      Retrofit / OkHttp
              |
              v
       JSONPlaceholder

### Responsabilidades

- Model: representa los datos utilizados por la aplicación.
- View: muestra la información y recibe las acciones del usuario.
- ViewModel: administra los estados y la lógica de presentación.
- Repository: gestiona las operaciones y devuelve Result.
- API: define los métodos HTTP disponibles.
- RetrofitClient: configura la comunicación con el servicio REST.
- Theme: centraliza los colores, formas y tipografías.

La interfaz no realiza llamadas directas a la API.
El ViewModel se comunica con el Repository,
manteniendo la separación de responsabilidades.

## Estructura principal de paquetes

    com.alvarez.murocampus/
    |
    |-- MainActivity.kt
    |
    |-- model/
    |   |-- Modelos.kt
    |
    |-- data/
    |   |-- PostRepository.kt
    |   |
    |   |-- remote/
    |       |-- PostApi.kt
    |       |-- RetrofitClient.kt
    |
    |-- viewmodel/
    |   |-- PostsViewModel.kt
    |   |-- PostsViewModelFactory.kt
    |   |-- UiState.kt
    |
    |-- ui/
        |-- Componentes.kt
        |-- PantallaLista.kt
        |-- PantallaDetalle.kt
        |-- PantallaCrear.kt
        |-- PantallaEditar.kt
        |
        |-- theme/
            |-- Color.kt
            |-- Theme.kt
            |-- Type.kt

## Endpoints utilizados

URL base:

https://jsonplaceholder.typicode.com/

| Método | Endpoint | Funcionalidad |
|---|---|---|
| GET | posts | Listar publicaciones |
| GET | posts/{id} | Consultar una publicación |
| GET | posts/{id}/comments | Consultar comentarios |
| GET | posts?userId={id} | Filtrar por usuario |
| POST | posts | Crear publicación |
| DELETE | posts/{id} | Eliminar publicación |
| PATCH | posts/{id} | Modificar publicación |

## Manejo de estados

La aplicación utiliza UiState para representar
los diferentes estados de las operaciones.

- Cargando: indica que una operación está en proceso.
- Exito: representa una operación completada correctamente.
- Error: muestra un mensaje cuando ocurre un problema.

El Repository devuelve Result y el ViewModel transforma
el resultado en el estado correspondiente.

## Limitaciones de JSONPlaceholder

JSONPlaceholder es una API REST de prueba.

Las operaciones POST, PATCH y DELETE son simuladas,
por lo que los cambios no se almacenan permanentemente
en el servidor.

Las modificaciones y eliminaciones se reflejan
localmente durante la sesión de la aplicación.

## Ejecución del proyecto

### Requisitos

- Android Studio.
- JDK compatible con la configuración de Gradle.
- Android SDK.
- Emulador o dispositivo Android.
- Conexión a internet.

### Pasos

1. Clonar el repositorio.

       git clone https://github.com/RamsesAlvrz/Programacion-en-Moviles.git

2. Ingresar al repositorio.

       cd Programacion-en-Moviles

3. Cambiar a la rama de mejoras.

       git checkout mejora-ia-muro

4. Abrir el proyecto ubicado en:

       Semana 09/MuroCampus

5. Sincronizar las dependencias de Gradle.

6. Ejecutar la aplicación desde Android Studio.

### Compilación desde PowerShell

Desde la carpeta MuroCampus:

    .\gradlew.bat assembleDebug

## Organización de commits

| Commit | Descripción |
|---|---|
| 1 | Búsqueda de publicaciones por título |
| 2 | Interceptor con encabezado Accept |
| 3 | Eliminación de publicaciones con DELETE |
| 4 | Modificación de publicaciones con PATCH |
| 5 | Rediseño profesional de MuroCampus |
| 6 | Documentación de mejoras y uso de IA |

Cada mejora fue organizada en un commit independiente
para mantener un historial ordenado de los cambios.

## Uso de inteligencia artificial

Durante la segunda fase se utilizaron prompts
estructurados para solicitar mejoras funcionales
y visuales sobre el proyecto.

Las propuestas fueron revisadas y adaptadas
para mantener la arquitectura y las funcionalidades
existentes.

El registro de prompts, respuestas, correcciones
y justificaciones técnicas se encuentra en:

[PROMPTS.md](PROMPTS.md)

## Conclusiones

1. La búsqueda por título permitió mejorar la consulta
   de publicaciones sin realizar peticiones adicionales
   al servicio REST.

2. La implementación de DELETE y PATCH permitió ampliar
   las funcionalidades de la aplicación y comprender
   mejor el funcionamiento de los métodos HTTP.

3. El interceptor permitió centralizar la configuración
   de los encabezados de las peticiones.

4. El rediseño visual permitió conseguir una interfaz
   más organizada, personalizada y fácil de utilizar.

5. La arquitectura MVVM + Repository permitió incorporar
   nuevas funcionalidades manteniendo la separación
   de responsabilidades.

## Autor

- Ramses Alvarez Contreras
- Diseño y Desarrollo de Software
- TECSUP
- Curso: Programación en Móviles
- Laboratorio 09
