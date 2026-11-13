# SmartCatalog 2.0

Aplicación móvil Android desarrollada en Kotlin y Jetpack Compose para consultar, explorar y comparar celulares mediante una interfaz moderna denominada **Dark Tech Premium**.

El proyecto integra autenticación de usuarios, consumo de servicios REST, almacenamiento local, favoritos y un panel de administración interactivo.

## Tecnologías utilizadas

- **Kotlin:** lenguaje de programación principal.
- **Jetpack Compose:** construcción de interfaces declarativas.
- **Material Design 3:** componentes de interfaz.
- **MVVM:** arquitectura para separar interfaz, lógica de presentación y acceso a datos.
- **Firebase Authentication:** autenticación de usuarios.
- **Cloud Firestore:** almacenamiento de información de usuarios y roles.
- **Retrofit:** consumo de la API REST de productos.
- **DummyJSON:** fuente de datos del catálogo de celulares.
- **Room:** almacenamiento local y caché.
- **Coil:** carga de imágenes.
- **Navigation Compose:** navegación entre pantallas.
- **Kotlin Coroutines y Flow:** operaciones asíncronas y manejo de estados.

## Funcionalidades principales

### 1. Autenticación

- Inicio de sesión mediante Firebase Authentication.
- Registro de nuevos usuarios.
- Validación de credenciales.
- Gestión de sesiones.
- Acceso diferenciado según el rol.
- Cierre de sesión.

### 2. Catálogo de celulares

- Consulta de productos mediante Retrofit.
- Presentación de celulares en cuadrícula de dos columnas.
- Búsqueda por nombre o marca.
- Filtros por marca.
- Ordenamiento por precio.
- Banner promocional.
- Visualización del detalle de productos.

### 3. Favoritos

- Agregar celulares a favoritos.
- Quitar celulares de favoritos.
- Consultar la lista de favoritos.
- Persistencia de información mediante el sistema de datos existente.
- Notificaciones relacionadas con favoritos.

### 4. Comparador de celulares

- Selección de dos modelos.
- Visualización de imágenes, marcas y precios.
- Cálculo de diferencia de precios.
- Identificación del celular más económico.
- Navegación desde el catálogo.

### 5. Panel de administrador

- Dashboard con estadísticas.
- Cantidad de productos.
- Cantidad de usuarios registrados.
- Cantidad de favoritos.
- Lista de usuarios.
- Búsqueda por nombre o correo.
- Filtros por rol: Todos, Admin y Usuario.
- Consulta de detalles del usuario.
- Visualización de UID de Firebase.
- Actualización manual de estadísticas y usuarios.
- Cierre de sesión.

## Diseño Dark Tech Premium

SmartCatalog 2.0 utiliza una identidad visual tecnológica basada en fondos azul marino oscuro, superficies diferenciadas, bordes sutiles y detalles azul eléctrico.

Los colores se centralizan en `ui/theme/Color.kt` para facilitar la reutilización y mantener consistencia entre las pantallas.

## Arquitectura MVVM

La aplicación utiliza el patrón Model-View-ViewModel.

**Model:** representa los datos del sistema, como los productos y usuarios.

**View:** contiene las pantallas y componentes desarrollados mediante Jetpack Compose.

**ViewModel:** administra los estados de la interfaz y coordina las operaciones necesarias.

**Repository:** proporciona acceso a las fuentes de datos, incluyendo servicios remotos y almacenamiento local.

### Flujo general

```text
Usuario
   |
   v
Pantallas Jetpack Compose
   |
   v
ViewModel
   |
   v
Repository
   |
   +---- Retrofit / DummyJSON
   |
   +---- Room
   |
   +---- Firebase / Firestore
```

La interfaz observa los estados proporcionados por los ViewModels y se actualiza cuando cambian los datos.

## Estructura principal

```text
SmartCatalog/
├── app/
│   └── src/main/
│       ├── java/com/tecsup/smartcatalog/
│       │   ├── data/
│       │   ├── domain/
│       │   └── ui/
│       │       ├── admin/
│       │       ├── comparador/
│       │       ├── components/
│       │       ├── detalle/
│       │       ├── favoritos/
│       │       ├── lista/
│       │       ├── login/
│       │       ├── navigation/
│       │       └── theme/
│       └── res/
├── gradle/
├── build.gradle.kts
├── settings.gradle.kts
├── PROMPTS.md
└── README.md
```

## Requisitos para ejecutar el proyecto

- Android Studio.
- SDK de Android compatible con la configuración Gradle del proyecto.
- Emulador Android o dispositivo físico.
- Conexión a Internet para los servicios remotos.
- Configuración válida de Firebase para el proyecto.

## Instrucciones de ejecución

1. Clonar el repositorio:

```bash
git clone https://github.com/RamsesAlvrz/Programacion-en-Moviles.git
```

2. Abrir Android Studio.

3. Seleccionar **Open**.

4. Abrir la carpeta:

```text
Programacion-en-Moviles/Semana 12/SmartCatalog
```

5. Esperar la sincronización de Gradle.

6. Configurar Firebase con los archivos y credenciales correspondientes al proyecto.

7. Seleccionar un emulador o dispositivo Android.

8. Ejecutar la aplicación.

**Nota:** Los archivos privados de configuración y las credenciales deben gestionarse de forma segura. No deben publicarse claves privadas en el repositorio.

## Control de versiones

El desarrollo de las mejoras se realizó en la rama:

`mejora-ia-smart`

Commits principales:

| Commit | Descripción |
|---|---|
| `0e281d8` | Búsqueda, filtros y catálogo premium |
| `cd7be74` | Rediseño Dark Tech |
| `fa1b964` | Comparador de celulares |
| Posterior | Panel de administrador interactivo |

## Uso de inteligencia artificial

Durante el desarrollo se utilizó inteligencia artificial como apoyo para estructurar requerimientos, implementar mejoras visuales y funcionales, y revisar código.

Las instrucciones de desarrollo y su relación con los commits están documentadas en:

**[PROMPTS.md](PROMPTS.md)**

## Pruebas realizadas

Se verificó el funcionamiento de:

- Inicio y cierre de sesión.
- Catálogo y búsqueda.
- Filtros y ordenamiento.
- Detalle de celulares.
- Gestión de favoritos.
- Comparador de celulares.
- Panel administrativo.
- Búsqueda y filtros de usuarios.
- Consulta de detalles de usuarios.
- Actualización de estadísticas.

## Conclusiones

SmartCatalog 2.0 integra diferentes tecnologías del desarrollo Android moderno mediante una arquitectura MVVM.

El proyecto permite aplicar conceptos de interfaces declarativas, consumo de APIs REST, almacenamiento local, autenticación, navegación y administración de datos.

Las mejoras implementadas contribuyen a una experiencia de usuario más completa, con un diseño visual coherente y herramientas interactivas tanto para usuarios como para administradores.