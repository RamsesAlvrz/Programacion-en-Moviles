# PROMPTS.md — Fase 2: Mejoras con IA

## Proyecto: CentroTareasTec

**Rama:** `mejora-ia-tareas`  
**Tecnologías:** Kotlin, Jetpack Compose, MVVM, Repository y corrutinas.  
**Herramienta de IA:** Gemini en Android Studio.

## Objetivo

En esta segunda fase utilizamos inteligencia artificial como apoyo para mejorar la aplicación desarrollada durante la Fase 1.

El objetivo es implementar nuevas funcionalidades, revisar las propuestas de Gemini y comprobar que los cambios respeten la arquitectura MVVM y el uso correcto de corrutinas.

---

## Mejora 1: Tiempo máximo de carga

### 1. Funcionalidad solicitada

Implementar un límite opcional de 2 segundos para las cargas secuenciales y paralelas mediante `withTimeout`.

### 2. Prompt enviado a Gemini

**Rol:** Actúa como un desarrollador Android senior especializado en Kotlin, Jetpack Compose, MVVM y corrutinas.

**Contexto:** Estoy trabajando en el proyecto CentroTareasTec, ubicado en `Semana 14/CentroTareasTec`, en la rama `mejora-ia-tareas`. La aplicación ya tiene cargas secuenciales y paralelas, manejo de errores, reintentos manuales, cálculo de primos y tareas con progreso.

**Tarea:** Revisa el código existente e implementa directamente una mejora para controlar el tiempo máximo de las cargas.

**Requisitos:**

1. Agregar un interruptor llamado "Límite de tiempo".
2. Utilizar `withTimeout(2000L)` cuando el límite esté activado.
3. Cancelar la operación si supera los 2 segundos y mostrar un mensaje de error.
4. Mantener las cargas originales cuando el límite esté desactivado.
5. Conservar el interruptor de error simulado y el botón Reintentar.
6. Evitar la ejecución simultánea de varias cargas.
7. Mejorar la presentación visual utilizando componentes Material 3.

**Archivos permitidos:**

- `viewmodel/TareasViewModel.kt`
- `ui/PantallaTareas.kt`

**Restricciones:**

- Mantener la arquitectura MVVM + Repository.
- Utilizar `viewModelScope` y corrutinas estructuradas.
- Manejar correctamente `TimeoutCancellationException` y `CancellationException`.
- No utilizar `GlobalScope`, `runBlocking` ni `Thread.sleep`.
- No eliminar funcionalidades anteriores.
- No modificar otros laboratorios.
- No realizar commits automáticamente.

**Resultado solicitado:** Aplica los cambios directamente en el proyecto, revisa los imports y explica qué modificaste. Si puedes ejecutar la compilación, verifica que no existan errores.

### 3. Respuesta de Gemini

*Pendiente de completar con un resumen de la respuesta real de Gemini.*

### 4. Cambios implementados

La mejora contempla los siguientes cambios:

- Incorporación de un interruptor para activar el tiempo máximo.
- Uso de `withTimeout(2000L)` para limitar las cargas.
- Manejo de errores cuando se supera el tiempo permitido.
- Conservación de las cargas secuenciales y paralelas.
- Actualización de la interfaz con tarjetas, indicadores y mensajes de estado.

**Archivos modificados:**

- `TareasViewModel.kt`
- `PantallaTareas.kt`

### 5. Revisión y decisiones tomadas

Se debe comprobar que el límite de tiempo cancele la operación sin bloquear la interfaz y que las excepciones de cancelación se manejen correctamente.

También se debe verificar que las funciones anteriores continúen disponibles y que no se agreguen dependencias innecesarias.

### 6. Pruebas de funcionamiento

| Prueba | Resultado esperado |
|---|---|
| Límite desactivado + Una por una | Carga exitosa en aproximadamente 5 segundos |
| Límite desactivado + A la vez | Carga exitosa en aproximadamente 3 segundos |
| Límite activado | Mensaje de tiempo máximo superado |
| Desactivar límite y reintentar | La carga puede completarse |
| Revisar interfaz | La aplicación permanece operativa |

**Resultado real:** Pendiente de registrar después de ejecutar las pruebas.

### 7. Commit correspondiente

`feat: agrega tiempo maximo de carga con withTimeout`

---

## Próximas mejoras

Las siguientes mejoras se documentarán aquí cuando se implementen y prueben.

- Mejora 2: Reintento automático.
- Mejora 3: Comparación de tiempos.

## Conclusión de la fase

*Se completará al finalizar las tres mejoras y verificar su funcionamiento.*