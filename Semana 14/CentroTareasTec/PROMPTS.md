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
- Room y KSP
- Base de datos SQLite local

## 2. Objetivo

En esta fase se utilizó inteligencia artificial como
apoyo para implementar nuevas funcionalidades en
CentroTareasTec.

Se trabajó en tres mejoras principales y dos mejoras adicionales:

1. Tiempo máximo de carga.
2. Reintentos automáticos.
3. Comparación de tiempos de ejecución.
4. Navegación inferior entre Tareas e Historial.
5. Historial persistente con Room y estadísticas.

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

## 20. Cambios realizados

### TareasViewModel.kt

Se agregó `ComparacionTiempos`
para almacenar las duraciones de ambas cargas.

También se incorporó `compararTiempos()`,
que ejecuta las dos modalidades y mide
sus tiempos reales utilizando
`SystemClock.elapsedRealtime()`.

Los resultados se exponen mediante StateFlow.

### PantallaTareas.kt

Se agregó una tarjeta
"Comparación de rendimiento" con:

- Botón "Comparar tiempos".
- Indicador de procesamiento.
- Tabla de tiempos.
- Barras comparativas.
- Resultado del análisis.
- Porcentaje de ahorro.

### Archivos modificados

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

## 23. Commit realizado

`feat: agrega comparacion visual de tiempos de carga`

**Estado:** Commit creado en la rama `mejora-ia-tareas`. El identificador inicial fue `535c707`; si se cambió el mensaje con `git commit --amend`, debe actualizarse con el hash definitivo de `git log`.

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
| Comparación de tiempos | `535c707` (hash inicial, verificar si hubo amend) | Commit creado |
| Navegación inferior | Verificar en `git log` | Implementada |
| Historial Room y estadísticas | Por registrar tras commit | Implementada; verificar pruebas completas |

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


---

# MEJORA 4: NAVEGACIÓN INFERIOR PROFESIONAL

## 27. Funcionalidad

Se implementó una barra de navegación inferior con dos secciones: **Tareas** e **Historial**. La pantalla Tareas mantiene las opciones de configuración, cargas y comparación; la pantalla Historial permite consultar la actividad sin sobrecargar la vista principal.

## 28. Prompt 4 preparado para Gemini

### Rol y contexto

Actúa como desarrollador Android senior especializado en Kotlin, Jetpack Compose, Material 3 y MVVM. Proyecto CentroTareasTec, rama `mejora-ia-tareas`, carpeta `Semana 14/CentroTareasTec`.

### Objetivo y requerimientos

Implementa una navegación inferior profesional con `NavigationBar` y `NavigationBarItem` de Material 3, con dos destinos: **Tareas** e **Historial**. Mantén el ViewModel y el estado de las operaciones al cambiar de pestaña. Resalta el destino seleccionado y conserva los colores azules y las tarjetas redondeadas. La nueva pantalla Historial debe mostrar inicialmente un estado vacío, sin inventar registros. No agregues Room todavía, ya que se integrará en una mejora posterior. No elimines funciones anteriores, no modifiques otros laboratorios y no realices commits automáticamente.

### Archivos previstos

- `MainActivity.kt`
- `ui/PantallaPrincipal.kt` (nuevo)
- `ui/PantallaHistorial.kt` (nuevo)

## 29. Respuesta de Gemini

**Pendiente:** No se ha proporcionado la respuesta literal de Gemini para documentarla. El código se preparó y revisó durante el desarrollo asistido.

## 30. Cambios realizados y decisiones

- `MainActivity.kt` pasó a mostrar `PantallaPrincipal`.
- `PantallaPrincipal.kt` controla la selección de las dos pestañas.
- `PantallaHistorial.kt` se creó inicialmente con tarjetas de estadísticas en cero y un estado vacío.
- Se optó por dos pantallas en lugar de colocar el historial debajo de todas las tareas, para mantener una interfaz ordenada.
- En una revisión posterior se reemplazaron los símbolos provisionales por iconos Material de Tareas e Historial.

## 31. Pruebas

| Prueba | Resultado esperado | Estado documentado |
|---|---|---|
| Abrir Tareas | Conserva la pantalla de operaciones | Vista mostrada en captura |
| Abrir Historial | Muestra la segunda pestaña | Pendiente de confirmar individualmente |
| Cambiar de pestaña | No reinicia el ViewModel | Pendiente de prueba específica |
| Barra inferior | Se mantiene visible | Vista mostrada en captura |

**Commit:** `feat: agrega navegacion inferior entre tareas e historial` (verificar en `git log` si se registró por separado).

---

# MEJORA 5: HISTORIAL PERSISTENTE CON ROOM Y ESTADÍSTICAS

## 32. Funcionalidad

Se incorporó una base de datos Room para guardar los resultados de las ejecuciones, observarlos mediante Flow y mostrar estadísticas reales en Historial. Se registran las cargas secuenciales y paralelas y las comparaciones, tanto exitosas como fallidas. Cada registro contiene tipo, estado, descripción, duración y fecha.

## 33. Prompt 5 preparado para Gemini

### Rol

Actúa como desarrollador Android senior especializado en Kotlin, Jetpack Compose, Material 3, Room, StateFlow, corrutinas y MVVM + Repository. Aplica los cambios directamente en CentroTareasTec, rama `mejora-ia-tareas`.

### Objetivo

Implementa un historial persistente de ejecuciones utilizando Room, conectado a las operaciones existentes y a la pestaña Historial.

### Requerimientos

1. Crear entidad Room con `id`, `tipo`, `estado`, `descripcion`, `duracionMs` y `fecha`.
2. Crear DAO con inserción, consulta ordenada por fecha y eliminación total.
3. Exponer registros mediante `Flow<List<EjecucionEntity>>`.
4. Implementar `HistorialRepository` y `HistorialViewModel`.
5. Registrar una sola vez cada carga o comparación terminada, exitosa o fallida.
6. No registrar operaciones en curso ni duplicarlas por recomposición.
7. Mostrar total de ejecuciones, exitosas, fallidas y duración promedio de las exitosas.
8. Agregar filtros Todas, Exitosas y Fallidas.
9. Agregar confirmación antes de limpiar el historial.
10. Mostrar fecha, duración, descripción y estado en tarjetas Material 3.
11. Conservar los registros tras cerrar y volver a abrir la aplicación.
12. Mantener todas las funciones anteriores y la navegación inferior.

### Restricciones

Usa corrutinas y arquitectura MVVM + Repository. No utilices `GlobalScope`, `runBlocking` ni `Thread.sleep`. No bloquees el hilo principal, no inventes estadísticas, no modifiques otros laboratorios y no realices commits automáticamente. Revisa compatibilidad de Room, KSP y Gradle antes de compilar.

## 34. Respuesta de Gemini

**Pendiente:** Agregar la respuesta real de Gemini si se utilizó para esta mejora. Los cambios de código también se trabajaron mediante asistencia en el chat; no se atribuyen respuestas no verificadas a Gemini.

## 35. Archivos creados

| Archivo | Función |
|---|---|
| `data/local/EjecucionEntity.kt` | Modelo de la tabla de ejecuciones |
| `data/local/EjecucionDao.kt` | Consultas, inserción y borrado |
| `data/local/CentroTareasDatabase.kt` | Base de datos Room |
| `data/HistorialRepository.kt` | Acceso a datos del historial |
| `viewmodel/HistorialViewModel.kt` | Estado observable y eliminación |
| `viewmodel/HistorialViewModelFactory.kt` | Creación del ViewModel de historial |

## 36. Archivos modificados

| Archivo | Modificación |
|---|---|
| `gradle/libs.versions.toml` | Dependencias Room, KSP e iconos Material |
| `build.gradle.kts` (raíz) | Registro del plugin KSP |
| `app/build.gradle.kts` | Aplicación de KSP y dependencias Room |
| `gradle.properties` | Configuración de compatibilidad con el sistema de compilación |
| `MainActivity.kt` | Creación de Room y conexión de repositorios y ViewModels |
| `viewmodel/TareasViewModel.kt` | Registro de resultados exitosos y fallidos |
| `viewmodel/TareasViewModelFactory.kt` | Inyección de HistorialRepository |
| `ui/PantallaPrincipal.kt` | Navegación e iconos Material |
| `ui/PantallaHistorial.kt` | Estadísticas, filtros, tarjetas y diálogo de borrado |

## 37. Revisión y decisiones técnicas

- Se eligió **Room** en lugar de guardar los registros solamente en memoria, porque el historial debe persistir.
- Se utiliza `Flow` para que la interfaz reciba los cambios de la base de datos.
- El registro de ejecuciones se realiza desde el ViewModel, no desde la recomposición de Compose.
- Se mantuvo la separación entre la lógica de tareas y el acceso a los datos.
- Se conservaron los controles de tiempo máximo, reintento automático y comparación de rendimiento.
- Se solucionaron problemas de sincronización Gradle relacionados con KSP y el uso de `kotlin.sourceSets` en el entorno de Android Gradle Plugin 9.3.3. La configuración de compatibilidad deberá revisarse si se actualizan los plugins.
- Se corrigió la franja clara de la barra de estado de Android, dibujando el fondo azul detrás de los indicadores del sistema mediante el ajuste de `MainActivity.kt` y `PantallaPrincipal.kt`.

## 38. Pruebas de la mejora

| Prueba | Resultado esperado | Estado documentado |
|---|---|---|
| Sincronizar Gradle y ejecutar | La app abre correctamente | Confirmado por captura |
| Barra de estado azul | No queda franja blanca superior | Confirmado por el usuario |
| Navegación inferior | Tareas e Historial disponibles | Barra visible en captura |
| Carga exitosa | Se crea un registro exitoso | Pendiente de evidencia específica |
| Carga fallida | Se crea un registro fallido | Pendiente de evidencia específica |
| Comparación | Se guarda su resultado | Pendiente de evidencia específica |
| Filtros | Separan exitosas y fallidas | Pendiente de evidencia específica |
| Reiniciar la aplicación | El historial se conserva | Pendiente de evidencia específica |
| Limpiar historial | Pide confirmación y elimina registros | Pendiente de evidencia específica |

## 39. Commit

Mensaje preparado:

`feat: implementa historial persistente con Room y estadisticas`

**Estado:** Código integrado y aplicación abierta. Registrar hash y confirmación del commit después de ejecutar `git log -5 --oneline`. Si la navegación se incluyó en el mismo commit, no atribuirle otro hash.

---

# 40. Registro de revisión de IA solicitado por el laboratorio

| Prompt que escribí | Qué respondió Gemini | Qué cambié o descarté | Por qué |
|---|---|---|---|
| Tiempo máximo con `withTimeout(2000L)` | Pendiente de transcribir respuesta real | Se dejó el límite como opción activable | Permite comparar las cargas sin límite |
| Reintentos automáticos hasta 3 intentos | Pendiente de transcribir respuesta real | Se descartó reintentar automáticamente los timeouts | Evita repetir operaciones que exceden el límite configurado |
| Comparación secuencial y paralela | Pendiente de transcribir respuesta real | Se usaron tiempos medidos, no valores fijos | Para comparar rendimiento real |
| Barra inferior Tareas / Historial | Pendiente de confirmar uso y respuesta de Gemini | Se separó el historial en otra pantalla y se reemplazaron símbolos por iconos Material | Mejora la navegación y el diseño |
| Historial persistente con Room | Pendiente de confirmar uso y respuesta de Gemini | Se utilizó Room con Flow, filtros y estadísticas reales | Mantiene datos entre sesiones y actualiza la interfaz |

> Nota: Esta tabla no inventa respuestas de Gemini. Completar la columna correspondiente con lo que realmente respondió el agente, si se utilizó.

# 41. Resumen final de mejoras

| N.º | Mejora | Resultado |
|---|---|---|
| 1 | Tiempo máximo | Control opcional de 2 segundos |
| 2 | Reintentos automáticos | Hasta 3 intentos con espera progresiva |
| 3 | Comparación de rendimiento | Tabla, barras y porcentaje de ahorro |
| 4 | Navegación inferior | Secciones Tareas e Historial |
| 5 | Historial con Room | Registros persistentes, estadísticas y filtros |

# 42. Conclusiones adicionales

1. La navegación inferior organiza las funcionalidades de la aplicación en dos secciones y evita sobrecargar la pantalla principal.
2. Room y Flow permiten almacenar ejecuciones y reflejar cambios del historial sin bloquear la interfaz.
3. El proyecto integra corrutinas, manejo de errores, medición de rendimiento y persistencia local, manteniendo la arquitectura MVVM + Repository.
