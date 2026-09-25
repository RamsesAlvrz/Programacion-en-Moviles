# 📱 NavLabIA — Proyecto con Inteligencia Artificial (Semana 5)

Este directorio contiene la implementación de la aplicación **NavLabIA**, desarrollada para Android en **Kotlin** utilizando **Jetpack Compose**. Esta versión fue estructurada y optimizada con el apoyo de herramientas de **Inteligencia Artificial (IA)** para acelerar el flujo de desarrollo, modularizar componentes UI y configurar la arquitectura de navegación.

---

## 🛠️ Tecnologías y Arquitectura

* **Lenguaje:** Kotlin
* **UI Framework:** Jetpack Compose (Material Design 3)
* **Navegación:** Navigation Compose
* **Build System:** Gradle con Kotlin DSL (`build.gradle.kts`) y Version Catalogs (`libs.versions.toml`)
* **Paquete Principal:** `com.alvarez.navlabia`

---

## 🤖 Aportes de la Inteligencia Artificial en esta versión

Durante el desarrollo asistido por IA se lograron los siguientes avances respecto a la versión manual:

1. **Arquitectura de Navegación Clean:** Estructuración del flujo de pantallas en `navigation/AppNavigation.kt` y centralización de rutas con un `sealed class` en `navigation/Screen.kt`.
2. **Componentes Reutilizables:** Creación de componentes UI independientes como `UserAvatar.kt`.
3. **Manejo de Datos y Modelos:** Modelado de la entidad `Alumno.kt` y generación de datos de prueba estructurados en `DummyData.kt`.
4. **Diseño y Temas:** Configuración del sistema de diseño (colores, tipografía y tema global) en el paquete `ui/theme/`.

---

## 🧩 Pantallas y Módulos del Proyecto

La aplicación consta de las siguientes pantallas (`screens/`):

* 🔐 **`LoginScreen.kt`**: Pantalla de autenticación inicial.
* 🏠 **`HomeScreen.kt`**: Pantalla principal tras iniciar sesión.
* 📋 **`ListScreen.kt`**: Muestra el listado de alumnos (`Alumno.kt`) generados desde `DummyData.kt`.
* 🔍 **`DetailScreen.kt` / `DetailsScreen.kt`**: Vista de detalle para información específica.
* 👤 **`ProfileScreen.kt`**: Perfil del usuario con componente avatar (`UserAvatar.kt`).

---

## 📂 Estructura del Código Fuente

```text
app/src/main/java/com/alvarez/navlabia/
├── components/          # Componentes UI reutilizables (UserAvatar.kt)
├── model/               # Modelos de datos (Alumno.kt) y mock data (DummyData.kt)
├── navigation/          # Rutas y grafo de navegación (AppNavigation.kt, Screen.kt)
├── screens/             # Pantallas principales de la app (Login, Home, List, Detail, Profile)
├── ui/theme/            # Configuración de colores, tipografía y tema Compose
└── MainActivity.kt      # Punto de entrada de la aplicación