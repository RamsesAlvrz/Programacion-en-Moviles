
# PROMPTS.md - Fase 2: Mejoras con IA

## 1. Información del proyecto

**Proyecto:** CentroTareasTec  
**Laboratorio:** Hilos y corrutinas en Android  
**Fase:** 2 - Mejoras con inteligencia artificial  
**Rama de trabajo:** `mejora-ia-tareas`  
**Herramienta de IA:** Gemini en Android Studio

### Tecnologías utilizadas

- Kotlin
- Jetpack Compose
- Material Design 3
- Arquitectura MVVM
- Repository
- Corrutinas de Kotlin
- StateFlow
- Git y GitHub

## 2. Objetivo de la Fase 2

El objetivo de esta fase es mejorar la aplicación CentroTareasTec utilizando inteligencia artificial como apoyo durante el desarrollo.

Para ello, se plantearon nuevas funcionalidades relacionadas con el manejo de corrutinas, el control de tiempos y la recuperación de errores.

Las propuestas deben revisarse antes de incorporarlas, comprobando que mantengan la arquitectura MVVM y que no afecten las funcionalidades desarrolladas en la Fase 1.

---

# MEJORA 1: TIEMPO MÁXIMO DE CARGA

## 3. Descripción de la mejora

Se implementó un límite opcional de 2 segundos para las operaciones de carga.

Esta funcionalidad permite cancelar una operación cuando supera el tiempo establecido, evitando que el usuario espere indefinidamente.

Para realizarlo se utilizó `withTimeout(2000L)`.

## 4. Prompt 1 enviado a Gemini

### Rol

Actúa como un desarrollador Android senior especializado en Kotlin, Jetpack Compose, arquitectura MVVM, Repository y corrutinas.

### Contexto

Estoy desarrollando CentroTareasTec, ubicado en `Semana 14/CentroTareasTec`, dentro de la rama `mejora-ia-tareas`.

La aplicación ya implementa cargas secuenciales y paralelas, manejo de errores, reintentos manuales, cálculo de números primos y tareas con progreso y cancelación.

### Objetivo

Implementa directamente en el proyecto una mejora que permita establecer un tiempo máximo de 2 segundos para las cargas simuladas.

### Requerimientos funcionales

1. Agregar un interruptor llamado "Límite de tiempo".
2. Utilizar `withTimeout(2000L)` cuando el límite esté activado.
3. Cancelar correctamente la operación cuando supere los 2 segundos.
4. Mostrar el mensaje "Tiempo máximo superado. La carga tardó más de 2 segundos".
5. Cuando el límite esté desactivado, conservar el funcionamiento original de las cargas.
6. Mantener la simulación de errores y el botón Reintentar.
7. Evitar que se ejecuten dos cargas simultáneamente.
8. Conservar las funcionalidades desarrolladas en la Fase 1.

### Requerimientos de interfaz

Mejora la presentación visual utilizando Material Design 3 y Jetpack Compose.

La interfaz debe incluir:

- Encabezado azul.
- Tarjetas con bordes redondeados.
- Controles organizados.
- Indicador de carga.
- Mensajes visuales de éxito y error.
- Diseño limpio y profesional.

### Archivos permitidos

- `viewmodel/TareasViewModel.kt`
- `ui/PantallaTareas.kt`

### Restricciones técnicas

- Mantener la arquitectura MVVM + Repository.
- Utilizar `viewModelScope`.
- Utilizar corrutinas estructuradas.
- Manejar correctamente `TimeoutCancellationException`.
- Propagar las cancelaciones que no correspondan al tiempo máximo.
- No utilizar `GlobalScope`, `runBlocking` ni `Thread.sleep`.
- No bloquear el hilo principal.
- No eliminar funcionalidades existentes.
- No realizar commits automáticamente.

### Instrucciones para el agente

1. Inspecciona los archivos actuales.
2. Aplica directamente las modificaciones.
3. Verifica los imports y las referencias.
4. Comprueba la compilación si tienes acceso a ella.
5. Explica los cambios realizados y las pruebas necesarias.

## 5. Respuesta de Gemini - Mejora 1

**Registro de respuesta:**

Pendiente de completar con la respuesta real obtenida en Gemini.

## 6. Cambios implementados

### TareasViewModel.kt

Se incorporó un estado para activar o desactivar el límite de tiempo.

También se utilizó `withTimeout(2000L)` para controlar la duración de las cargas.

Cuando se supera el tiempo permitido, la aplicación muestra un estado de error.

### PantallaTareas.kt

Se agregó el interruptor "Límite de tiempo".

Además, se mejoró la presentación utilizando componentes Material 3, tarjetas para los resultados e indicadores visuales.

### Archivos modificados

- `TareasViewModel.kt`
- `PantallaTareas.kt`

## 7. Revisión y decisiones tomadas

Se decidió que el límite de tiempo fuera opcional para conservar las pruebas originales de las cargas secuenciales y paralelas.

También se mantuvo el uso de `viewModelScope`, de manera que las corrutinas estén relacionadas con el ciclo de vida del ViewModel.

Se evitó utilizar operaciones que bloquearan el hilo principal.

## 8. Pruebas de la Mejora 1

| Prueba | Resultado esperado |
|---|---|
| Límite desactivado + Una por una | Carga exitosa en aproximadamente 5 segundos |
| Límite desactivado + A la vez | Carga exitosa en aproximadamente 3 segundos |
| Límite activado | Mensaje de tiempo máximo superado |
| Desactivar límite y reintentar | La operación puede completarse |
| Revisar interfaz | La pantalla permanece operativa |

**Resultado real:** Registrar los resultados observados durante la ejecución.

## 9. Commit de la Mejora 1

`feat: agrega tiempo maximo de carga con withTimeout`

---

# MEJORA 2: REINTENTO AUTOMÁTICO

## 10. Descripción de la mejora

Se incorporó un sistema de reintentos automáticos para las operaciones que presentan errores.

Cuando la funcionalidad está activada, la aplicación puede realizar hasta 3 intentos en total.

Entre los intentos se utiliza una espera progresiva:

- Primer intento: ejecución inicial.
- Segundo intento: espera previa de 1 segundo.
- Tercer intento: espera previa de 2 segundos.

La finalidad es permitir que la aplicación intente recuperarse de un error sin que el usuario tenga que presionar manualmente el botón Reintentar.

## 11. Prompt 2 enviado a Gemini

### Rol

Actúa como un desarrollador Android senior especializado en Kotlin, Jetpack Compose, corrutinas y arquitectura MVVM.

### Contexto

Estoy trabajando en el proyecto CentroTareasTec, ubicado en `Semana 14/CentroTareasTec`, dentro de la rama `mejora-ia-tareas`.

La aplicación ya cuenta con cargas secuenciales y paralelas, manejo de errores, reintentos manuales y un límite opcional de 2 segundos implementado con `withTimeout`.

### Objetivo

Implementa directamente en el proyecto un sistema de reintento automático para las cargas simuladas.

La mejora debe conservar todas las funcionalidades existentes.

### Requerimientos funcionales

1. Agregar un interruptor llamado "Reintento automático".
2. Mantenerlo desactivado inicialmente.
3. Cuando esté activado y ocurra un error, realizar hasta 3 intentos en total.
4. Esperar 1 segundo antes del segundo intento.
5. Esperar 2 segundos antes del tercer intento.
6. Mostrar el intento actual en la interfaz.
7. Si una operación funciona, detener los reintentos y mostrar el resultado.
8. Si fallan los 3 intentos, mostrar un mensaje de error.
9. Conservar el botón de reintento manual.
10. Mantener el límite de tiempo de la mejora anterior.
11. No reintentar automáticamente los errores por tiempo máximo.
12. Conservar las cargas secuenciales y paralelas.
13. Mantener la simulación de errores.
14. No eliminar las funcionalidades de la Fase 1.

### Requerimientos de interfaz

Mantener el diseño profesional implementado anteriormente.

Agregar una sección visual para mostrar:

- Estado del intento.
- Número de intento actual.
- Mensaje de espera antes del siguiente intento.
- Resultado final de la operación.
- Mensaje de error cuando se agoten los intentos.

### Archivos permitidos

- `viewmodel/TareasViewModel.kt`
- `ui/PantallaTareas.kt`

### Restricciones técnicas

- Mantener MVVM + Repository.
- Utilizar `viewModelScope`.
- Utilizar `delay()` para las esperas.
- Utilizar corrutinas estructuradas.
- No utilizar `GlobalScope`.
- No utilizar `runBlocking`.
- No utilizar `Thread.sleep`.
- No bloquear el hilo principal.
- No tratar `CancellationException` como un error recuperable.
- Evitar que se ejecuten varias cargas simultáneamente.
- No modificar otros laboratorios.
- No cambiar de rama ni realizar commits.

### Instrucciones para el agente

1. Revisa la implementación actual del ViewModel y la pantalla.
2. Implementa el reintento automático sin eliminar el código anterior.
3. Agrega los estados necesarios utilizando StateFlow.
4. Actualiza la interfaz para mostrar el avance de los intentos.
5. Comprueba que el límite de tiempo siga funcionando.
6. Revisa los imports y corrige posibles errores.
7. Ejecuta la compilación si tienes acceso.
8. Explica los cambios realizados y las pruebas recomendadas.

### Criterios de aceptación

- Sin reintento automático, la carga funciona como antes.
- Con error simulado y reintento automático, se realizan 3 intentos.
- Se muestra el número de intento.
- Las esperas son de 1 y 2 segundos.
- Al agotarse los intentos aparece un error.
- El límite de tiempo funciona de forma independiente.
- La interfaz permanece operativa.

## 12. Respuesta de Gemini - Mejora 2

**Registro de respuesta:**

Pendiente de completar con la respuesta real obtenida en Gemini.

## 13. Cambios implementados

### TareasViewModel.kt

Se agregaron nuevos estados utilizando `MutableStateFlow` y `StateFlow`:

- `reintentoAutomatico`: controla si los reintentos están activados.
- `intentoActual`: almacena el número del intento.
- `mensajeReintento`: muestra información sobre el proceso.

También se modificó la ejecución de cargas para permitir hasta 3 intentos.

Se utilizó `delay()` para establecer las esperas progresivas sin bloquear el hilo principal.

Los errores por tiempo máximo se manejan por separado para evitar reintentos innecesarios.

### PantallaTareas.kt

Se agregó un interruptor para activar el reintento automático.

También se incorporó una sección que muestra el estado de los intentos, utilizando colores y mensajes informativos.

Se mantuvieron las tarjetas de resultados, los botones de carga y el diseño de la mejora anterior.

### Archivos modificados

- `TareasViewModel.kt`
- `PantallaTareas.kt`

## 14. Revisión y decisiones tomadas

Se decidió utilizar un máximo de 3 intentos para evitar que la aplicación repita indefinidamente una operación fallida.

Las esperas progresivas permiten dejar un intervalo entre intentos sin bloquear la interfaz.

También se decidió que los errores por tiempo máximo no generen nuevos intentos, porque esto podría aumentar innecesariamente el tiempo de espera.

Se conservaron las funcionalidades de la Fase 1 y la mejora de tiempo máximo.

## 15. Pruebas de la Mejora 2

| Prueba | Resultado esperado |
|---|---|
| Reintento automático desactivado | Se ejecuta un solo intento |
| Reintento automático activado + error simulado | Se realizan 3 intentos |
| Espera entre intentos | Se esperan 1 y 2 segundos |
| Visualización de intentos | Aparece el número del intento actual |
| Tres intentos fallidos | Se muestra el error definitivo |
| Límite de tiempo activado | Se interrumpe la carga sin reintentos adicionales |
| Carga sin errores | La operación termina correctamente |

**Resultado real:** Registrar los resultados observados durante la ejecución.

## 16. Commit de la Mejora 2

`feat: implementa reintentos automaticos con espera progresiva`

---

# 17. Revisión general del código

Durante el desarrollo se deben revisar las propuestas de la inteligencia artificial antes de incorporarlas.

Los aspectos principales son:

1. Mantener la arquitectura MVVM + Repository.
2. Evitar operaciones que bloqueen el hilo principal.
3. Utilizar corrutinas estructuradas.
4. Respetar el ciclo de vida del ViewModel.
5. Manejar correctamente los errores y las cancelaciones.
6. Conservar las funcionalidades anteriores.
7. Verificar el comportamiento de cada mejora antes de realizar el commit.

---

# 18. Próxima mejora

## Mejora 3: Comparación de tiempos

La siguiente mejora consistirá en mostrar una comparación visual entre los tiempos de ejecución de las cargas secuenciales y paralelas.

Esta sección se completará cuando se implemente y pruebe la funcionalidad.

---

# 19. Conclusiones

1. El uso de inteligencia artificial puede ayudar a proponer nuevas funcionalidades y organizar los cambios del proyecto, pero es necesario revisar el código antes de incorporarlo.

2. El control del tiempo máximo y los reintentos automáticos permiten mejorar el manejo de operaciones que tardan demasiado o presentan errores.

3. Mantener la arquitectura MVVM y utilizar correctamente las corrutinas ayuda a desarrollar una aplicación más organizada y con una interfaz que continúa respondiendo durante las tareas.
