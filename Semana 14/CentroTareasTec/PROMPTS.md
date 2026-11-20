
# PROMPTS.md - Fase 2: Mejoras con IA

## 1. Información del proyecto

**Proyecto:** CentroTareasTec  
**Laboratorio:** Semana 14 - Programación en Móviles  
**Fase:** 2 - Mejoras con inteligencia artificial  
**Rama:** `mejora-ia-tareas`  
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

## 2. Objetivo

En esta fase se utilizó inteligencia artificial como
apoyo para implementar nuevas funcionalidades en
CentroTareasTec.

Se trabajó en tres mejoras:

1. Tiempo máximo de carga.
2. Reintentos automáticos.
3. Comparación de tiempos de ejecución.

El objetivo fue mejorar el manejo de errores y
el rendimiento de las cargas sin perder las
funcionalidades desarrolladas en la Fase 1.

Las propuestas de IA deben revisarse antes de
incorporarlas al proyecto.

---

# MEJORA 1: TIEMPO MÁXIMO DE CARGA

## 3. Funcionalidad

Implementar un límite opcional de 2 segundos
para las cargas secuenciales y paralelas,
utilizando `withTimeout(2000L)`.

Cuando la operación supera ese tiempo, se
cancela y se muestra un mensaje de error.

## 4. Prompt 1 enviado a Gemini

### Rol

Actúa como desarrollador Android senior
especializado en Kotlin, Jetpack Compose,
MVVM, Repository y corrutinas.

### Contexto

Estoy desarrollando CentroTareasTec, ubicado
en `Semana 14/CentroTareasTec`, dentro de la
rama `mejora-ia-tareas`.

La aplicación ya implementa cargas
secuenciales y paralelas, manejo de errores,
reintentos manuales, cálculo de números
primos y tareas con progreso y cancelación.

### Objetivo

Implementa directamente en el proyecto
una mejora que permita establecer un tiempo
máximo de 2 segundos para las cargas.

### Requerimientos

1. Agrega un interruptor "Límite de tiempo".
2. Utiliza `withTimeout(2000L)` cuando esté activo.
3. Cancela la operación si supera 2 segundos.
4. Muestra un mensaje indicando que se superó
   el tiempo máximo.
5. Mantén el funcionamiento original cuando
   el límite esté desactivado.
6. Conserva la simulación de errores.
7. Conserva el botón de reintento manual.
8. Evita ejecutar dos cargas simultáneamente.
9. No elimines funcionalidades anteriores.

### Interfaz

Mejora el diseño utilizando Material Design 3.

Incluye un encabezado azul, tarjetas con
bordes redondeados, controles organizados,
indicadores de carga y mensajes de resultado.

### Archivos permitidos

- `viewmodel/TareasViewModel.kt`
- `ui/PantallaTareas.kt`

### Restricciones

- Mantener MVVM + Repository.
- Utilizar `viewModelScope`.
- Utilizar corrutinas estructuradas.
- Manejar `TimeoutCancellationException`.
- Propagar correctamente otras cancelaciones.
- No utilizar `GlobalScope`.
- No utilizar `runBlocking`.
- No utilizar `Thread.sleep`.
- No bloquear el hilo principal.
- No realizar commits automáticamente.

### Instrucciones al agente

Inspecciona los archivos existentes y
aplica directamente las modificaciones.

Verifica los imports, conserva las funciones
anteriores y comprueba la compilación si
tienes acceso a ella.

Al finalizar, explica los cambios realizados
y las pruebas recomendadas.

## 5. Respuesta de Gemini

**Pendiente:** Agregar un resumen de la
respuesta real obtenida en Gemini.

## 6. Cambios realizados

### TareasViewModel.kt

Se agregó un estado `limiteActivo` mediante
StateFlow para controlar el límite de tiempo.

Se utilizó `withTimeout(2000L)` para limitar
la duración de las operaciones.

También se agregó el manejo de
`TimeoutCancellationException` para mostrar
un error cuando se supera el tiempo permitido.

### PantallaTareas.kt

Se agregó un interruptor para activar
o desactivar el límite de tiempo.

También se mejoró el diseño de la pantalla
con componentes Material 3.

### Archivos modificados

- `TareasViewModel.kt`
- `PantallaTareas.kt`

## 7. Revisión y decisiones tomadas

Se decidió que el límite de tiempo fuera
opcional para poder seguir comparando las
cargas secuenciales y paralelas.

Se mantuvo `viewModelScope` para que las
operaciones respeten el ciclo de vida
del ViewModel.

Se evitó bloquear el hilo principal.

## 8. Pruebas

| Prueba | Resultado esperado |
|---|---|
| Límite desactivado y carga secuencial | Aproximadamente 5 segundos |
| Límite desactivado y carga paralela | Aproximadamente 3 segundos |
| Límite activado | Error después de 2 segundos |
| Desactivar límite y reintentar | Carga exitosa |
| Interfaz durante la carga | Continúa respondiendo |

**Resultado real:** Pendiente de registrar
según las pruebas ejecutadas.

## 9. Commit

`7254070 - Agrega tiempo maximo de carga con withTimeout`

---

# MEJORA 2: REINTENTO AUTOMÁTICO

## 10. Funcionalidad

Implementar un sistema de reintentos
automáticos cuando una carga presenta errores.

La aplicación realiza un máximo de
3 intentos en total.

Entre los intentos se utilizan esperas
progresivas:

- Intento 1: ejecución inicial.
- Intento 2: espera previa de 1 segundo.
- Intento 3: espera previa de 2 segundos.

## 11. Prompt 2 enviado a Gemini

### Rol

Actúa como desarrollador Android senior
especializado en Kotlin, Jetpack Compose,
corrutinas y arquitectura MVVM.

### Contexto

Estoy trabajando en CentroTareasTec,
ubicado en `Semana 14/CentroTareasTec`,
dentro de la rama `mejora-ia-tareas`.

El proyecto ya tiene cargas secuenciales,
paralelas, manejo de errores, reintentos
manuales y un límite opcional de 2 segundos.

### Objetivo

Implementa directamente en el proyecto
un sistema de reintentos automáticos
para las cargas simuladas.

### Requerimientos

1. Agrega un interruptor "Reintento automático".
2. Debe estar desactivado inicialmente.
3. Permite hasta 3 intentos en total.
4. Espera 1 segundo antes del segundo intento.
5. Espera 2 segundos antes del tercer intento.
6. Muestra el intento actual en pantalla.
7. Si la operación funciona, detén los reintentos.
8. Si fallan los 3 intentos, muestra un error.
9. Conserva el reintento manual.
10. Mantén el límite de tiempo anterior.
11. No reintentes automáticamente los errores
    por tiempo máximo.
12. Conserva las cargas secuenciales y paralelas.
13. Mantén la simulación de errores.
14. No elimines funciones de la Fase 1.

### Interfaz

Conserva el diseño profesional Material 3.

Agrega una sección que muestre:

- Estado de los intentos.
- Número de intento actual.
- Tiempo de espera antes del siguiente.
- Resultado final.
- Mensaje de error.

### Archivos permitidos

- `viewmodel/TareasViewModel.kt`
- `ui/PantallaTareas.kt`

### Restricciones

- Mantener MVVM + Repository.
- Utilizar `viewModelScope`.
- Utilizar `StateFlow`.
- Utilizar `delay()` para las esperas.
- No utilizar `GlobalScope`.
- No utilizar `runBlocking`.
- No utilizar `Thread.sleep`.
- No bloquear el hilo principal.
- No tratar `CancellationException` como
  un error recuperable.
- Evitar cargas simultáneas.
- No realizar commits automáticamente.

### Instrucciones al agente

Revisa el código actual e implementa
directamente los reintentos automáticos.

Conserva las funcionalidades anteriores,
actualiza la interfaz y comprueba que
el límite de tiempo siga funcionando.

Revisa los imports y verifica la
compilación si tienes acceso a ella.

Al finalizar, explica qué modificaste.

## 12. Respuesta de Gemini

**Pendiente:** Agregar un resumen de la
respuesta real obtenida en Gemini.

## 13. Cambios realizados

### TareasViewModel.kt

Se agregaron los estados:

- `reintentoAutomatico`
- `intentoActual`
- `mensajeReintento`

Estos estados permiten controlar los
reintentos y mostrar su progreso.

También se modificó la ejecución de cargas
para permitir hasta 3 intentos.

Se utilizó `delay()` para implementar
esperas progresivas sin bloquear la interfaz.

### PantallaTareas.kt

Se agregó el interruptor
"Reintento automático".

También se incorporó una sección que
muestra el estado de los intentos.

Se mantuvieron las tarjetas y los
controles de la mejora anterior.

### Archivos modificados

- `TareasViewModel.kt`
- `PantallaTareas.kt`

## 14. Revisión y decisiones tomadas

Se estableció un máximo de 3 intentos
para evitar repeticiones indefinidas.

Se utilizaron esperas progresivas
para separar los intentos.

Se decidió no reintentar automáticamente
cuando se supera el límite de tiempo.

Se mantuvo la arquitectura MVVM
y el uso de corrutinas estructuradas.

## 15. Pruebas

| Prueba | Resultado esperado |
|---|---|
| Reintento desactivado | Un solo intento |
| Error simulado y reintento activo | Tres intentos |
| Esperas progresivas | 1 y 2 segundos |
| Estado visual | Muestra el intento actual |
| Tres intentos fallidos | Error definitivo |
| Límite de tiempo activo | No realiza reintentos adicionales |
| Carga normal | Operación exitosa |

**Resultado real:** Pendiente de registrar
según las pruebas ejecutadas.

## 16. Commit

`a6a3e6c - Implementa reintentos automaticos con espera progresiva`

---

# MEJORA 3: COMPARACIÓN DE TIEMPOS

## 17. Funcionalidad

Implementar una comparación de rendimiento
entre las cargas secuenciales y paralelas.

La aplicación debe medir los tiempos reales
de ambas modalidades y mostrar:

- Tabla de tiempos.
- Barras comparativas.
- Modalidad más rápida.
- Diferencia de tiempo.
- Porcentaje de ahorro.

## 18. Prompt 3 enviado a Gemini

### Rol

Actúa como desarrollador Android senior
especializado en Kotlin, Jetpack Compose,
Material 3, MVVM, Repository y corrutinas.

### Contexto

Estoy desarrollando CentroTareasTec,
ubicado en `Semana 14/CentroTareasTec`,
dentro de la rama `mejora-ia-tareas`.

El proyecto ya implementa cargas secuenciales
y paralelas, un límite opcional de 2 segundos,
reintentos automáticos con esperas progresivas
y manejo de errores.

### Objetivo

Implementa directamente en el proyecto
una tercera mejora que permita comparar
los tiempos de ejecución de las cargas
secuenciales y paralelas.

### Requerimientos funcionales

1. Registra los tiempos reales de las cargas.
2. Agrega un botón "Comparar tiempos".
3. Ejecuta ambas modalidades para medirlas.
4. Muestra una tabla con los tiempos en ms.
5. Calcula la diferencia entre los tiempos.
6. Calcula el porcentaje de ahorro.
7. Identifica la modalidad más rápida.
8. Agrega barras comparativas en Compose.
9. No utilices tiempos fijos o inventados.
10. No muestres resultados válidos si
    alguna de las operaciones falla.
11. Evita ejecutar varias cargas simultáneamente.
12. Mantén las funcionalidades anteriores.

### Requerimientos de interfaz

Mantén el diseño profesional Material 3.

Agrega una tarjeta llamada
"Comparación de rendimiento".

Debe incluir:

- Botón para iniciar la comparación.
- Indicador de procesamiento.
- Tabla con tiempos.
- Barras de comparación.
- Resultado del análisis.
- Porcentaje de ahorro.

### Archivos permitidos

- `viewmodel/TareasViewModel.kt`
- `ui/PantallaTareas.kt`

### Restricciones técnicas

- Mantener MVVM + Repository.
- Utilizar `viewModelScope`.
- Utilizar `StateFlow`.
- Utilizar corrutinas estructuradas.
- Medir tiempos con `SystemClock.elapsedRealtime()`.
- No utilizar `Thread.sleep`.
- No utilizar `runBlocking`.
- No utilizar `GlobalScope`.
- No bloquear el hilo principal.
- Propagar correctamente `CancellationException`.
- Respetar el límite de tiempo.
- Respetar los reintentos automáticos.
- No modificar otros laboratorios.
- No realizar commits automáticamente.

### Instrucciones al agente

Inspecciona los archivos actuales y
aplica directamente las modificaciones.

Conserva el diseño profesional y las
funcionalidades existentes.

Verifica los imports y la compilación
si tienes acceso a ella.

Al terminar, explica los cambios
realizados y las pruebas recomendadas.

### Criterios de aceptación

1. La comparación presenta tiempos reales.
2. La tabla muestra ambas modalidades.
3. Las barras representan los tiempos medidos.
4. Se identifica la modalidad más rápida.
5. El porcentaje se calcula correctamente.
6. Los errores no producen resultados falsos.
7. La interfaz continúa respondiendo.
8. Las mejoras anteriores siguen funcionando.

## 19. Respuesta de Gemini

**Pendiente:** Agregar un resumen de la
respuesta real obtenida en Gemini.

## 20. Cambios propuestos

### TareasViewModel.kt

Se propone agregar `ComparacionTiempos`
para almacenar las duraciones de ambas cargas.

También se incorpora `compararTiempos()`,
que ejecuta las dos modalidades y mide
sus tiempos reales utilizando
`SystemClock.elapsedRealtime()`.

Los resultados se exponen mediante StateFlow.

### PantallaTareas.kt

Se propone agregar una tarjeta
"Comparación de rendimiento" con:

- Botón "Comparar tiempos".
- Indicador de procesamiento.
- Tabla de tiempos.
- Barras comparativas.
- Resultado del análisis.
- Porcentaje de ahorro.

### Archivos por modificar

- `TareasViewModel.kt`
- `PantallaTareas.kt`

## 21. Revisión y decisiones técnicas

Se decidió medir los tiempos reales
en lugar de utilizar valores fijos.

La comparación ejecuta primero la carga
secuencial y después la paralela.

Se mantienen las corrutinas estructuradas
para evitar bloquear el hilo principal.

Si alguna operación falla, no se debe
mostrar una comparación exitosa.

También se conserva el límite de tiempo
y el sistema de reintentos automáticos.

## 22. Pruebas de la Mejora 3

| Prueba | Resultado esperado |
|---|---|
| Comparación normal | Tabla con dos tiempos |
| Carga secuencial | Aproximadamente 5 segundos |
| Carga paralela | Aproximadamente 3 segundos |
| Barras comparativas | Representan los tiempos medidos |
| Modalidad más rápida | Se identifica correctamente |
| Porcentaje de ahorro | Se calcula automáticamente |
| Error simulado | No muestra comparación exitosa |
| Límite de tiempo activo | Interrumpe la comparación |
| Interfaz | Permanece operativa |

**Resultado real:** Pendiente de registrar
después de ejecutar la aplicación.

## 23. Commit previsto

`feat: agrega comparacion visual de tiempos de carga`

**Estado:** Pendiente de compilación,
verificación y commit.

---

# 24. Revisión general de las propuestas de IA

Durante la Fase 2 se revisaron las mejoras
propuestas para mantener la organización
y el funcionamiento del proyecto.

Los principales criterios fueron:

1. Conservar la arquitectura MVVM + Repository.
2. Mantener las funciones de la Fase 1.
3. Utilizar corrutinas estructuradas.
4. Evitar operaciones bloqueantes.
5. Manejar correctamente las cancelaciones.
6. Evitar cargas simultáneas.
7. No agregar dependencias innecesarias.
8. Probar cada mejora antes del commit.
9. Mantener los cambios separados en Git.

## 25. Resumen de commits

| Mejora | Commit | Estado |
|---|---|---|
| Tiempo máximo | `7254070` | Registrado |
| Reintento automático | `a6a3e6c` | Registrado |
| Comparación de tiempos | Pendiente | Por verificar |

## 26. Conclusiones

1. La inteligencia artificial ayudó a plantear
   mejoras para el proyecto, pero sus propuestas
   deben revisarse y probarse antes de incorporarlas.

2. El uso de `withTimeout` y `delay` permite
   controlar los tiempos de ejecución y manejar
   errores sin bloquear la interfaz principal.

3. La comparación de tiempos permite demostrar
   las diferencias entre ejecutar tareas de forma
   secuencial y paralela, aplicando los conceptos
   de corrutinas desarrollados en el laboratorio.
