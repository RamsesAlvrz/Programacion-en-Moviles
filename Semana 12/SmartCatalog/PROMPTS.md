# REGISTRO DE PROMPTS — SMARTCATALOG 2.0

## 1. Información general

**Proyecto:** SmartCatalog 2.0  
**Tipo de aplicación:** Catálogo de celulares para Android  
**Lenguaje:** Kotlin  
**Framework de interfaz:** Jetpack Compose y Material Design 3  
**Arquitectura:** MVVM  
**Tecnologías:** Firebase Authentication, Cloud Firestore, Retrofit, Room y Navigation Compose  
**Repositorio:** https://github.com/RamsesAlvrz/Programacion-en-Moviles  
**Directorio:** `Semana 12/SmartCatalog`  
**Rama de desarrollo:** `mejora-ia-smart`

### Objetivo del documento

Documentar las mejoras implementadas en SmartCatalog 2.0 con apoyo de inteligencia artificial, relacionando las instrucciones de desarrollo con los cambios realizados en el código y los commits de GitHub.

Los prompts están redactados como instrucciones para un agente de IA capaz de inspeccionar, crear y modificar archivos directamente en un proyecto Android.

**Nota:** Los prompts son reconstrucciones estructuradas del trabajo realizado. No representan necesariamente una transcripción literal de las conversaciones originales.

---

# 2. PROMPT 01 — Mejorar el catálogo de celulares

**Commit:** `0e281d8`  
**Mensaje:** `Mejorar catalogo con busqueda filtros y diseno premium`

## 2.1. Prompt dirigido al agente de IA

> **ROL DEL AGENTE**
>
> Actúa como un desarrollador Android senior especializado en Kotlin, Jetpack Compose, Material Design 3 y arquitectura MVVM.
>
> **CONTEXTO DEL PROYECTO**
>
> Estoy desarrollando SmartCatalog, una aplicación Android ubicada en `Semana 12/SmartCatalog`, con paquete principal `com.tecsup.smartcatalog`.
>
> La aplicación utiliza Firebase para autenticación, Retrofit para consultar celulares, Room para almacenamiento local y un repositorio que proporciona los productos a la interfaz.
>
> Actualmente dispone de catálogo, favoritos y navegación hacia el detalle de cada celular.
>
> Necesito que analices directamente los archivos existentes y mejores la experiencia de exploración del catálogo sin afectar las funcionalidades implementadas.
>
> **OBJETIVO PRINCIPAL**
>
> Transformar el catálogo principal en una interfaz moderna, organizada e interactiva que permita buscar, filtrar y ordenar celulares.
>
> **REQUISITO 1: BÚSQUEDA DE CELULARES**
>
> 1. Modifica `ListaScreen.kt` para incorporar un campo de búsqueda.
> 2. Permite buscar productos por nombre o marca.
> 3. Actualiza los resultados mientras el usuario escribe.
> 4. Ignora las diferencias entre mayúsculas y minúsculas.
> 5. Muestra un mensaje cuando no existan coincidencias.
> 6. Permite combinar la búsqueda con los filtros y el ordenamiento.
>
> **REQUISITO 2: FILTROS POR MARCA**
>
> 1. Agrega una sección horizontal con marcas seleccionables.
> 2. Obtén las marcas desde la lista de productos disponible.
> 3. Incluye una opción "Todos".
> 4. Resalta visualmente la marca seleccionada.
> 5. Mantén la búsqueda activa al cambiar de filtro.
>
> **REQUISITO 3: ORDENAMIENTO**
>
> 1. Agrega opciones de ordenamiento por precio.
> 2. Implementa orden ascendente y descendente.
> 3. Conserva los filtros y la búsqueda cuando cambie el orden.
>
> **REQUISITO 4: CUADRÍCULA DE PRODUCTOS**
>
> 1. Reorganiza los productos en dos columnas.
> 2. Crea el componente reutilizable `ProductoGridCard.kt`.
> 3. Muestra imagen, marca, nombre y precio.
> 4. Incluye un botón para agregar o quitar favoritos.
> 5. Permite abrir el detalle del producto seleccionado.
>
> **REQUISITO 5: BANNER PROMOCIONAL**
>
> 1. Crea `CatalogoBanner.kt`.
> 2. Integra el recurso `banner_smartcatalog.png`.
> 3. Presenta un mensaje promocional relacionado con celulares.
> 4. Incorpora un botón "Explorar".
> 5. Adapta el diseño al tamaño de la pantalla.
>
> **ARCHIVOS QUE DEBES MODIFICAR O CREAR**
>
> - `ui/lista/ListaScreen.kt`
> - `ui/components/ProductoGridCard.kt`
> - `ui/components/CatalogoBanner.kt`
> - `res/drawable/banner_smartcatalog.png`
>
> **RESTRICCIONES TÉCNICAS**
>
> - Mantén Kotlin y Jetpack Compose.
> - Respeta la arquitectura MVVM.
> - Reutiliza el modelo `Item`.
> - No modifiques innecesariamente Firebase, Retrofit ni Room.
> - No elimines las funciones de favoritos y detalle.
> - Evita dependencias adicionales innecesarias.
> - No realices commits automáticamente.
>
> **CRITERIOS DE ACEPTACIÓN**
>
> - La búsqueda funciona por nombre y marca.
> - Los filtros muestran los productos correspondientes.
> - El ordenamiento por precio funciona.
> - Los productos aparecen en dos columnas.
> - El banner se visualiza correctamente.
> - Los favoritos y el detalle continúan funcionando.
>
> **INSTRUCCIÓN FINAL**
>
> Inspecciona primero la estructura del proyecto y las dependencias existentes. Implementa los cambios directamente en los archivos correspondientes, verifica las referencias y comprueba que el proyecto compile sin afectar las funcionalidades anteriores.

## 2.2. Archivos registrados en el commit

| Archivo | Modificación |
|---|---|
| `CatalogoBanner.kt` | Creación del banner promocional |
| `ProductoGridCard.kt` | Creación de tarjetas de productos |
| `ListaScreen.kt` | Búsqueda, filtros, ordenamiento y cuadrícula |
| `banner_smartcatalog.png` | Incorporación del recurso gráfico |

## 2.3. Resultado obtenido

Se mejoró la experiencia del catálogo mediante un buscador, filtros por marca, ordenamiento por precio y presentación de productos en dos columnas.

También se incorporó un banner promocional y un componente reutilizable para mostrar los celulares.

**Validación funcional:** Se comprobó el funcionamiento de la búsqueda, los filtros, el ordenamiento, la navegación hacia el detalle y los favoritos.

---

# 3. PROMPT 02 — Rediseño Dark Tech Premium

**Commit:** `cd7be74`  
**Mensaje:** `Redisenar interfaz SmartCatalog con estilo Dark Tech`

## 3.1. Prompt dirigido al agente de IA

> **ROL DEL AGENTE**
>
> Actúa como un desarrollador Android senior y diseñador UI/UX especializado en Kotlin, Jetpack Compose, Material Design 3 y sistemas de diseño.
>
> **CONTEXTO DEL PROYECTO**
>
> Estoy desarrollando SmartCatalog 2.0, una aplicación Android que utiliza Firebase Authentication, Firestore, Retrofit, Room y arquitectura MVVM.
>
> La aplicación dispone de inicio de sesión, registro, catálogo, búsqueda, filtros, favoritos, detalle de productos y panel de administración.
>
> Aunque estas funcionalidades están implementadas, las pantallas presentan diferencias visuales.
>
> Necesito que analices el proyecto y unifiques su apariencia sin modificar innecesariamente la lógica de negocio.
>
> **OBJETIVO PRINCIPAL**
>
> Implementar una identidad visual denominada "Dark Tech Premium", caracterizada por fondos azul marino oscuro, tarjetas elegantes, bordes sutiles, detalles azul eléctrico y textos de alto contraste.
>
> **REQUISITO 1: SISTEMA DE COLORES**
>
> Centraliza los siguientes colores en `ui/theme/Color.kt`:
>
> - `FondoPremium = Color(0xFF080F20)`
> - `SuperficiePremium = Color(0xFF101E38)`
> - `BordePremium = Color(0xFF304A73)`
> - `AzulElectrico = Color(0xFF2563EB)`
> - `AzulBrillante = Color(0xFF60A5FA)`
> - `TextoPremium = Color(0xFFF8FAFC)`
> - `TextoPremiumSecundario = Color(0xFFA9BAD5)`
> - `AmarilloFavorito = Color(0xFFF59E0B)`
>
> Utiliza estas constantes en los componentes para mantener coherencia visual.
>
> **REQUISITO 2: REDISEÑO DEL LOGIN**
>
> 1. Modifica `LoginScreen.kt`.
> 2. Integra `login_smartcatalog.png`.
> 3. Mejora la distribución de títulos, campos y botones.
> 4. Utiliza superficies oscuras y detalles azul eléctrico.
> 5. Conserva la autenticación y las validaciones existentes.
>
> **REQUISITO 3: REDISEÑO DEL CATÁLOGO**
>
> 1. Actualiza `ListaScreen.kt` con la nueva paleta.
> 2. Mantén el banner promocional.
> 3. Rediseña el buscador y los filtros.
> 4. Conserva la cuadrícula y el ordenamiento.
> 5. Evita fondos blancos innecesarios.
>
> **REQUISITO 4: TARJETAS DE PRODUCTOS**
>
> 1. Modifica `ProductoCard.kt` y `ProductoGridCard.kt`.
> 2. Utiliza tarjetas azul oscuro con bordes discretos.
> 3. Mantén esquinas redondeadas.
> 4. Mejora la legibilidad de nombres, marcas y precios.
> 5. Diferencia visualmente los favoritos seleccionados.
>
> **REQUISITO 5: DETALLE Y FAVORITOS**
>
> 1. Rediseña `DetalleScreen.kt`.
> 2. Rediseña `FavoritosScreen.kt`.
> 3. Mantén el mismo estilo visual del catálogo.
> 4. Conserva las imágenes y los datos de productos.
> 5. No elimines las acciones de favoritos.
>
> **REQUISITO 6: PANEL ADMINISTRATIVO**
>
> 1. Rediseña `AdminScreen.kt` como un dashboard.
> 2. Organiza las estadísticas en tarjetas.
> 3. Mejora la presentación de usuarios registrados.
> 4. Muestra nombres, correos y roles.
> 5. Conserva los datos obtenidos desde el ViewModel.
> 6. Mantén la opción de cerrar sesión.
>
> **REQUISITO 7: NAVEGACIÓN**
>
> 1. Revisa `AppNav.kt`.
> 2. Conserva las rutas existentes.
> 3. Mantén los permisos de navegación según el rol.
> 4. No alteres el comportamiento de las sesiones.
>
> **ARCHIVOS PRINCIPALES**
>
> - `ui/theme/Color.kt`
> - `ui/login/LoginScreen.kt`
> - `ui/lista/ListaScreen.kt`
> - `ui/components/ProductoCard.kt`
> - `ui/components/ProductoGridCard.kt`
> - `ui/detalle/DetalleScreen.kt`
> - `ui/favoritos/FavoritosScreen.kt`
> - `ui/admin/AdminScreen.kt`
> - `ui/navigation/AppNav.kt`
> - `res/drawable/login_smartcatalog.png`
>
> **RESTRICCIONES TÉCNICAS**
>
> - No reemplaces la arquitectura MVVM.
> - No elimines funcionalidades existentes.
> - No modifiques innecesariamente los repositorios.
> - Mantén la autenticación y los permisos por rol.
> - Utiliza componentes compatibles con Material Design 3.
> - Evita introducir errores de compilación.
> - No realices commits automáticamente.
>
> **CRITERIOS DE ACEPTACIÓN**
>
> - Todas las pantallas comparten una identidad visual coherente.
> - El inicio de sesión continúa funcionando.
> - El catálogo mantiene búsqueda, filtros y ordenamiento.
> - Los favoritos siguen funcionando.
> - El administrador conserva las estadísticas y la lista de usuarios.
> - La navegación entre pantallas funciona correctamente.
>
> **INSTRUCCIÓN FINAL**
>
> Inspecciona los componentes existentes antes de modificarlos. Aplica los cambios visuales directamente en los archivos correspondientes, reutiliza la paleta de colores y conserva toda la lógica funcional. Comprueba la compilación antes de finalizar.

## 3.2. Archivos registrados en el commit

| Archivo | Modificación |
|---|---|
| `AdminScreen.kt` | Rediseño del dashboard |
| `ProductoCard.kt` | Actualización visual |
| `ProductoGridCard.kt` | Actualización visual |
| `DetalleScreen.kt` | Rediseño de detalle |
| `FavoritosScreen.kt` | Rediseño de favoritos |
| `ListaScreen.kt` | Actualización del catálogo |
| `LoginScreen.kt` | Rediseño del login |
| `AppNav.kt` | Ajustes de navegación |
| `Color.kt` | Definición de colores |
| `login_smartcatalog.png` | Incorporación del recurso gráfico |

## 3.3. Resultado obtenido

Se implementó una identidad visual Dark Tech Premium para unificar la apariencia de SmartCatalog 2.0.

Se rediseñaron las pantallas de inicio de sesión, catálogo, favoritos, detalle y administración.

También se centralizó la paleta de colores para facilitar su reutilización.

**Validación funcional:** Se comprobó que las pantallas continuaran funcionando y que el rediseño no afectara la autenticación, navegación ni gestión de favoritos.

---

# 4. PROMPT 03 — Comparador de celulares

**Commit:** `fa1b964`  
**Mensaje:** `Implementar comparador de celulares y mejoras Dark Tech`

## 4.1. Prompt dirigido al agente de IA

> **ROL DEL AGENTE**
>
> Actúa como un desarrollador Android senior especializado en Kotlin, Jetpack Compose, Navigation Compose y arquitectura MVVM.
>
> **CONTEXTO DEL PROYECTO**
>
> Estoy desarrollando SmartCatalog 2.0, una aplicación Android que permite consultar celulares, buscarlos por nombre o marca, ordenarlos por precio y gestionar favoritos.
>
> El proyecto utiliza Firebase, Retrofit, Room y un repositorio de productos.
>
> La interfaz ya cuenta con una identidad visual Dark Tech Premium.
>
> Necesito implementar una funcionalidad adicional que aporte valor al usuario y permita comparar dos celulares del catálogo.
>
> **OBJETIVO PRINCIPAL**
>
> Crear una pantalla que permita seleccionar dos modelos de celulares, visualizar su información y determinar cuál tiene el menor precio.
>
> **REQUISITO 1: NUEVA PANTALLA**
>
> 1. Crea `ui/comparador/ComparadorScreen.kt`.
> 2. Implementa la interfaz mediante Jetpack Compose.
> 3. Agrega el título "Comparador de celulares".
> 4. Incorpora un botón para regresar.
> 5. Utiliza la paleta Dark Tech Premium.
>
> **REQUISITO 2: SELECCIÓN DE PRODUCTOS**
>
> 1. Implementa dos menús desplegables independientes.
> 2. Muestra los celulares disponibles.
> 3. Permite seleccionar un modelo en cada menú.
> 4. Actualiza la comparación cuando cambie una selección.
>
> **REQUISITO 3: COMPARACIÓN**
>
> 1. Muestra las imágenes de ambos celulares.
> 2. Presenta nombre, marca y precio.
> 3. Calcula la diferencia absoluta entre los precios.
> 4. Identifica cuál es más económico.
> 5. Muestra un resultado claro para el usuario.
>
> **REQUISITO 4: REUTILIZACIÓN DE DATOS**
>
> 1. Utiliza el modelo `Item`.
> 2. Obtén los productos mediante `ItemRepository`.
> 3. Reutiliza el flujo de productos existente.
> 4. Evita duplicar fuentes de datos.
> 5. No crees una base de datos independiente.
>
> **REQUISITO 5: NAVEGACIÓN**
>
> 1. Registra la ruta `comparador` en `AppNav.kt`.
> 2. Conecta el comparador con el catálogo.
> 3. Permite regresar a la pantalla anterior.
> 4. Conserva las demás rutas y el flujo de autenticación.
>
> **REQUISITO 6: ACCESO DESDE EL CATÁLOGO**
>
> 1. Modifica `ListaScreen.kt`.
> 2. Agrega una sección destacada "Comparar celulares".
> 3. Incorpora un botón "Ir".
> 4. Conecta el botón con la ruta `comparador`.
> 5. Mantén el buscador, filtros, ordenamiento y favoritos.
>
> **REQUISITO 7: AJUSTES VISUALES**
>
> 1. Revisa `ProductoGridCard.kt`.
> 2. Conserva el diseño oscuro de las tarjetas.
> 3. Corrige el fondo del botón de favoritos.
> 4. Mantén los bordes redondeados.
> 5. Reutiliza los colores del tema.
>
> **ARCHIVOS PRINCIPALES**
>
> - `ui/comparador/ComparadorScreen.kt`
> - `ui/navigation/AppNav.kt`
> - `ui/lista/ListaScreen.kt`
> - `ui/components/ProductoGridCard.kt`
>
> **RESTRICCIONES TÉCNICAS**
>
> - Utiliza Kotlin y Jetpack Compose.
> - Mantén la arquitectura MVVM.
> - Reutiliza `ItemRepository` y `Item`.
> - No modifiques innecesariamente Firebase, Retrofit ni Room.
> - No elimines las rutas existentes.
> - No afectes favoritos ni filtros.
> - Mantén el estilo Dark Tech Premium.
> - No realices commits automáticamente.
>
> **CRITERIOS DE ACEPTACIÓN**
>
> - Aparece la opción "Comparar celulares" en el catálogo.
> - El botón "Ir" abre el comparador.
> - Es posible seleccionar dos modelos.
> - Se muestran imágenes, nombres, marcas y precios.
> - Se calcula la diferencia de precios.
> - Se identifica el celular más económico.
> - El botón de regreso funciona.
> - Las funcionalidades anteriores permanecen operativas.
>
> **INSTRUCCIÓN FINAL**
>
> Analiza la arquitectura y los archivos existentes. Crea la pantalla del comparador, intégrala con el repositorio y la navegación, revisa las referencias entre componentes y verifica la compilación. No realices commits ni push hasta que se confirme el funcionamiento.

## 4.2. Archivos registrados en el commit

| Archivo | Modificación |
|---|---|
| `ComparadorScreen.kt` | Creación de pantalla de comparación |
| `ProductoGridCard.kt` | Ajustes visuales |
| `ListaScreen.kt` | Incorporación del acceso al comparador |
| `AppNav.kt` | Registro de la ruta |

## 4.3. Resultado obtenido

Se incorporó una nueva pantalla para seleccionar dos celulares y comparar sus características disponibles y precios.

La funcionalidad reutiliza los productos del repositorio existente y mantiene el diseño Dark Tech Premium.

**Validación funcional:** Se comprobó la selección de modelos, visualización de información, cálculo de diferencias y navegación hacia el catálogo.

---

# 5. PROMPT 04 — Panel de administrador interactivo

**Commit:** Pendiente de generar  
**Mensaje previsto:** `feat: mejorar panel administrador y documentar prompts IA`

## 5.1. Prompt dirigido al agente de IA

> **ROL DEL AGENTE**
>
> Actúa como un desarrollador Android senior especializado en Kotlin, Jetpack Compose, Material Design 3, arquitectura MVVM y Firebase Firestore.
>
> **CONTEXTO DEL PROYECTO**
>
> Estoy trabajando en SmartCatalog 2.0, una aplicación Android con autenticación Firebase, catálogo de celulares, favoritos y un panel de administración.
>
> El panel administrativo ya dispone de un dashboard con estadísticas de productos, usuarios y favoritos, además de una lista de usuarios registrados obtenida desde Cloud Firestore.
>
> Sin embargo, actualmente funciona principalmente como una pantalla informativa.
>
> Necesito que inspecciones directamente `AdminScreen.kt` y `AdminViewModel.kt` para agregar herramientas interactivas sin modificar la arquitectura ni eliminar funcionalidades.
>
> **OBJETIVO PRINCIPAL**
>
> Transformar el panel de administrador en una interfaz más interactiva, incorporando búsqueda de usuarios, filtros por rol, consulta de detalles y actualización manual de estadísticas.
>
> **REQUISITO 1: BUSCADOR DE USUARIOS**
>
> 1. Agrega un campo de búsqueda en `AdminScreen.kt`.
> 2. Permite buscar usuarios por nombre.
> 3. Permite buscar usuarios por correo electrónico.
> 4. Actualiza los resultados conforme el administrador escribe.
> 5. Ignora diferencias entre mayúsculas y minúsculas.
> 6. Agrega un botón para limpiar la búsqueda.
> 7. Muestra un mensaje cuando no existan coincidencias.
>
> **REQUISITO 2: FILTROS POR ROL**
>
> 1. Incorpora las opciones "Todos", "Admin" y "Usuario".
> 2. Filtra los usuarios según su rol.
> 3. Permite combinar filtros y búsqueda.
> 4. Resalta visualmente el filtro seleccionado.
> 5. Muestra la cantidad de usuarios encontrados.
>
> **REQUISITO 3: DETALLES DEL USUARIO**
>
> 1. Convierte las tarjetas de usuarios en elementos interactivos.
> 2. Abre una ventana de detalles al seleccionar una tarjeta.
> 3. Muestra el nombre del usuario.
> 4. Muestra su correo electrónico.
> 5. Muestra su rol.
> 6. Muestra su identificador UID de Firebase.
> 7. Incorpora un botón "Cerrar".
> 8. No permitas modificar ni eliminar datos desde esta ventana.
>
> **REQUISITO 4: ACTUALIZACIÓN DE ESTADÍSTICAS**
>
> 1. Agrega un botón de actualización junto al título "Resumen general".
> 2. Reutiliza el método `cargarDatos()` del ViewModel.
> 3. Consulta nuevamente la cantidad de productos.
> 4. Consulta nuevamente la cantidad de favoritos.
> 5. Consulta nuevamente los usuarios de Firestore.
> 6. Muestra un indicador de carga.
> 7. Evita ejecutar actualizaciones simultáneas desde el botón.
>
> **REQUISITO 5: MEJORAS DEL VIEWMODEL**
>
> 1. Mantén `AdminUiState.Loading`.
> 2. Mantén `AdminUiState.Success`.
> 3. Mantén `AdminUiState.Error`.
> 4. Conserva la consulta a la colección `usuarios`.
> 5. Ordena los usuarios alfabéticamente por nombre.
> 6. Conserva los conteos existentes.
> 7. Mantén el manejo de errores.
>
> **REQUISITO 6: DISEÑO DARK TECH PREMIUM**
>
> 1. Conserva el encabezado del dashboard.
> 2. Mantén las cuatro tarjetas de estadísticas.
> 3. Utiliza `FondoPremium` como fondo principal.
> 4. Utiliza `SuperficiePremium` para tarjetas y campos.
> 5. Utiliza `AzulElectrico` y `AzulBrillante` para elementos interactivos.
> 6. Mantén bordes redondeados y colores coherentes.
> 7. Conserva los botones de cerrar sesión.
>
> **ARCHIVOS PRINCIPALES**
>
> - `ui/admin/AdminScreen.kt`
> - `ui/admin/AdminViewModel.kt`
>
> **RESTRICCIONES TÉCNICAS**
>
> - Mantén Kotlin y Jetpack Compose.
> - Respeta la arquitectura MVVM.
> - No elimines usuarios.
> - No modifiques Firebase Authentication.
> - No alteres las reglas de seguridad de Firestore.
> - No elimines las estadísticas existentes.
> - No consultes Firestore por cada letra escrita.
> - Realiza la búsqueda y los filtros localmente sobre los usuarios cargados.
> - Mantén la navegación y el cierre de sesión.
> - No realices commits automáticamente.
>
> **CRITERIOS DE ACEPTACIÓN**
>
> - El buscador encuentra usuarios por nombre.
> - El buscador encuentra usuarios por correo.
> - Los filtros por rol funcionan.
> - Se pueden combinar búsqueda y filtros.
> - Las tarjetas permiten consultar detalles.
> - La ventana muestra nombre, correo, rol y UID.
> - El botón de actualización recarga los datos.
> - Las estadísticas continúan funcionando.
> - El cierre de sesión funciona correctamente.
>
> **INSTRUCCIÓN FINAL**
>
> Inspecciona los archivos existentes y modifica directamente `AdminScreen.kt` y `AdminViewModel.kt`. Conserva la arquitectura, la identidad visual y las funcionalidades anteriores. Comprueba la compilación y valida cada nueva interacción antes de finalizar.

## 5.2. Archivos modificados

| Archivo | Modificación |
|---|---|
| `AdminScreen.kt` | Buscador, filtros por rol, detalles de usuario y botón de actualización |
| `AdminViewModel.kt` | Ordenamiento alfabético y reutilización de carga de datos |

## 5.3. Resultado obtenido

Se incorporaron herramientas de gestión interactiva al panel de administrador.

Ahora es posible buscar usuarios por nombre o correo, filtrarlos por rol y consultar sus detalles mediante una ventana emergente.

También se agregó un botón para actualizar las estadísticas y la lista de usuarios.

La búsqueda y los filtros se realizan sobre los datos previamente cargados, evitando consultas adicionales a Firestore por cada interacción.

**Validación funcional:** Se confirmó el funcionamiento de las nuevas herramientas del administrador y la conservación de las estadísticas y del cierre de sesión.

---

# 6. Metodología de trabajo con inteligencia artificial

Durante el desarrollo de SmartCatalog 2.0 se utilizó inteligencia artificial como herramienta de apoyo para implementar funcionalidades, mejorar interfaces y resolver problemas de integración.

El proceso siguió las siguientes etapas:

1. **Análisis del requerimiento:** Identificación de la funcionalidad que debía implementarse.
2. **Estructuración del prompt:** Definición del contexto, objetivos, requisitos y restricciones.
3. **Revisión del proyecto:** Identificación de los archivos y componentes involucrados.
4. **Implementación:** Generación y modificación del código Kotlin.
5. **Integración:** Incorporación de los cambios en Android Studio.
6. **Corrección:** Resolución de errores de compilación y referencias.
7. **Pruebas:** Ejecución de la aplicación y verificación de funcionalidades.
8. **Control de versiones:** Registro de las mejoras mediante commits en Git.

La inteligencia artificial proporcionó apoyo técnico durante el desarrollo. Los cambios fueron revisados y probados en la aplicación antes de registrarse en GitHub.

---

# 7. Resumen de commits

| N.º | Commit | Descripción | Archivos de código o recursos |
|---|---|---|---|
| 1 | `0e281d8` | Búsqueda, filtros y catálogo premium | 4 |
| 2 | `cd7be74` | Rediseño Dark Tech Premium | 10 |
| 3 | `fa1b964` | Comparador de celulares | 4 |
| 4 | Pendiente | Panel administrativo interactivo | 2 |

El cuarto commit también incorporará este archivo `PROMPTS.md` como documentación del uso de inteligencia artificial.

## Rama de trabajo

`mejora-ia-smart`

## Comandos utilizados para verificar el repositorio

```bash
git status
git log -3 --oneline
```

## Últimos commits confirmados

```text
fa1b964 Implementar comparador de celulares y mejoras Dark Tech
cd7be74 Redisenar interfaz SmartCatalog con estilo Dark Tech
0e281d8 Mejorar catalogo con busqueda filtros y diseno premium
```

Los tres commits anteriores fueron confirmados en la rama local y remota `mejora-ia-smart`.

El cuarto commit se registrará después de incorporar esta documentación.

---

# 8. Conclusiones

La utilización de inteligencia artificial permitió organizar y desarrollar mejoras progresivas en SmartCatalog 2.0 mediante instrucciones técnicas estructuradas.

Primero se mejoró la experiencia del catálogo incorporando búsqueda, filtros y ordenamiento de productos.

Posteriormente, se implementó una identidad visual Dark Tech Premium para mantener coherencia entre las pantallas.

Como funcionalidad adicional, se desarrolló un comparador de celulares que permite consultar dos modelos y evaluar sus diferencias de precio.

Finalmente, se mejoró el panel administrativo mediante herramientas de búsqueda, filtros por rol, consulta de detalles y actualización de estadísticas.

Estas implementaciones permitieron reforzar conocimientos de Kotlin, Jetpack Compose, arquitectura MVVM, Firebase, reutilización de componentes, navegación y control de versiones con Git.

El registro de prompts facilita la trazabilidad del desarrollo al relacionar las instrucciones proporcionadas a la inteligencia artificial con los cambios realizados y los commits registrados en GitHub.