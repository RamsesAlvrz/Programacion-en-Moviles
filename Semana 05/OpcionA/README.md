# 🏥 Clínica Salud — Opción A (Laboratorio Semana 5)

Este directorio contiene el proyecto **Clínica Salud** (`com.alvarez.clinicasalud`), una aplicación móvil nativa para Android desarrollada con **Kotlin** y **Jetpack Compose**. El objetivo de este laboratorio es implementar y comparar la evolución de un proyecto mediante dos metodologías de desarrollo: la programación manual tradicional (*Sin IA*) y el desarrollo asistido por **Inteligencia Artificial** (*Con IA*).

A diferencia de organizar el proyecto en carpetas separadas, la **Opción A** utiliza el sistema de control de versiones **Git** para alternar entre ambas soluciones dentro del mismo repositorio.

---

## 🌿 Estructura de Ramas en Git

El código se encuentra dividido en dos ramas principales para permitir una comparación directa de la arquitectura y la calidad del código:

| Rama | Tipo | Descripción |
| :--- | :--- | :--- |
| 📌 **`main`** | **Sin IA** | Código base desarrollado de forma manual y tradicional. Implementa la lógica fundamental y pantallas principales de la clínica. |
| 🤖 **`mejora-ia-v2`** | **Con IA** | aVersión optimizada con apoyo de asistentes de IA. Incluye refactorización de código, mejora en la UI/UX, modularización y mejor manejo de estados. |

### 🛠️ Comandos para alternar entre versiones

Abre la terminal en la raíz de `OpcionA` para cambiar entre las ramas:

```bash
# Ver la versión optimizada asistida por IA
git checkout mejora-ia-v2

# Volver a la versión base desarrollada manualmente
git checkout main
```

---

## 📱 Módulos y Pantallas de la Aplicación

La aplicación **Clínica Salud** gestiona la navegación y visualización de servicios médicos a través de los siguientes componentes ubicados en `app/src/main/java/com/alvarez/clinicasalud/`:

* 🏠 **`screens/HomeScreen.kt`**: Pantalla principal que muestra el catálogo de especialidades médicas, accesos rápidos y panel de bienvenida.
* 📅 **`screens/ScheduleScreen.kt`**: Módulo para la reserva y programación de citas médicas.
* 👤 **`screens/ProfileScreen.kt`**: Vista general del perfil del usuario y accesos a configuraciones.
* 📋 **`screens/UserProfileScreen.kt`**: Formulario e historial detallado de la información del paciente.
* ➕ **`screens/ExtraScreens.kt`**: Pantallas secundarias y complementarias (confirmaciones, información institucional, soporte).
* 🗺️ **`model/ModelsAndRoutes.kt`**: Archivo que define las entidades de datos (médicos, citas, especialidades) y las rutas fuertemente tipadas para el grafo de navegación Compose.
* 🎨 **`ui/theme/`** (`Color.kt`, `Theme.kt`, `Type.kt`): Definición del sistema de diseño basado en **Material Design 3**.

---

## 🛠️ Tecnologías y Requisitos

* **Lenguaje:** Kotlin
* **UI Framework:** Jetpack Compose (Material Design 3)
* **Navegación:** Navigation Compose
* **Arquitectura Build:** Gradle con Kotlin DSL (`build.gradle.kts`) y Version Catalogs (`libs.versions.toml`)
* **Versión mínima de Android:** API Level 24 (Android 7.0 Nougat)
* **Entorno recomendado:** Android Studio Ladybug o superior / JDK 17+

---

## 📂 Estructura Completa del Proyecto

```text
OpcionA/
├── app/
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/alvarez/clinicasalud/
│   │   │   │   ├── model/
│   │   │   │   │   └── ModelsAndRoutes.kt      # Rutas y Modelos del dominio médico
│   │   │   │   ├── screens/
│   │   │   │   │   ├── ExtraScreens.kt         # Vistas secundarias y diálogos
│   │   │   │   │   ├── HomeScreen.kt           # Vista principal
│   │   │   │   │   ├── ProfileScreen.kt        # Vista general de perfil
│   │   │   │   │   ├── ScheduleScreen.kt       # Agendamiento de citas
│   │   │   │   │   └── UserProfileScreen.kt    # Detalle de perfil de usuario
│   │   │   │   ├── ui/theme/
│   │   │   │   │   ├── Color.kt                # Paleta de colores
│   │   │   │   │   ├── Theme.kt                # Tema global Compose
│   │   │   │   │   └── Type.kt                 # Tipografías del sistema
│   │   │   │   └── MainActivity.kt             # Punto de entrada de la app
│   │   │   └── AndroidManifest.xml
│   │   └── test/                               # Pruebas unitarias
│   └── build.gradle.kts                        # Configuración del módulo app
├── gradle/
│   ├── wrapper/
│   └── libs.versions.toml                      # Catálogo de dependencias
├── build.gradle.kts                            # Configuración global del proyecto
└── settings.gradle.kts
```

---

## 🚀 Instrucciones de Ejecución

1. **Abrir el proyecto:** Inicia **Android Studio** y selecciona la carpeta `OpcionA`.
2. **Sincronizar Gradle:** Espera a que el entorno descargue las dependencias y presiona **Sync Project with Gradle Files**.
3. **Seleccionar la rama deseada:** Desde la barra inferior de Android Studio (Git branch manager) o desde la terminal, selecciona `main` o `mejora-ia-v2`.
4. **Ejecutar:** Presiona `Shift + F10` o el botón **Run** para desplegar la aplicación en un emulador o dispositivo físico.