
# Registro de prompts - AvisosCampus

## Información del proyecto

- Estudiante: Ramses Alvarez Contreras
- Curso: Programación en Móviles
- Laboratorio: 15
- Proyecto: AvisosCampus
- Rama de trabajo: mejora-ia-avisos
- Lenguaje: Kotlin
- Interfaz: Jetpack Compose y Material 3
- Arquitectura: MVVM + Repository

## Objetivo

Mejorar la aplicación desarrollada durante la primera fase
mediante el apoyo de inteligencia artificial, manteniendo
la arquitectura existente y realizando cambios independientes
para cada funcionalidad.

---

## Prompt 1 - Implementación de dos canales de notificaciones

### Rol

Actúa como un desarrollador Android especializado en Kotlin,
Jetpack Compose, notificaciones locales y arquitectura MVVM.

### Contexto

El proyecto AvisosCampus ya cuenta con un canal de
notificaciones llamado Avisos, un ViewModel que administra
los eventos y un repositorio que registra el historial.

Actualmente, todas las notificaciones utilizan el mismo
canal, independientemente de su origen.

### Objetivo

Implementa un segundo canal llamado Urgentes para diferenciar
los recordatorios de las notificaciones generales.

### Instrucciones

1. Inspecciona la estructura y el código actual del proyecto
   antes de realizar cualquier modificación.

2. Trabaja exclusivamente en la rama mejora-ia-avisos.

3. Modifica directamente los siguientes archivos:

    - notificacion/Notificador.kt
    - viewmodel/AvisosViewModel.kt

4. Conserva el canal Avisos con IMPORTANCE_DEFAULT.

5. Crea el canal Urgentes con IMPORTANCE_HIGH y habilita
   la vibración.

6. Utiliza Origen.RECORDATORIO para identificar los avisos
   que deben enviarse mediante el canal Urgentes.

7. Mantén las notificaciones del botón y del modo avión
   dentro del canal Avisos.

8. Centraliza el registro y la emisión de eventos en el
   ViewModel mediante una función privada reutilizable.

9. Diferencia el recordatorio urgente mediante su título.

10. Mantén la compatibilidad con versiones anteriores
    a Android 8 mediante NotificationCompat.

### Restricciones

- No modificar la rama main.
- No modificar Modelos.kt ni AvisosRepository.kt.
- No agregar Context ni NotificationManager al ViewModel.
- No modificar el diseño de la interfaz.
- No agregar dependencias innecesarias.
- Conservar PendingIntent.FLAG_IMMUTABLE.
- Mantener los identificadores independientes.
- Conservar la validación del permiso de notificaciones.
- No eliminar funcionalidades existentes.
- No realizar commits ni push automáticamente.

### Criterios de aceptación

- La aplicación compila correctamente.
- Existen los canales Avisos y Urgentes.
- El canal Avisos tiene importancia normal.
- El canal Urgentes tiene importancia alta.
- Los recordatorios utilizan el canal Urgentes.
- Las demás notificaciones utilizan el canal Avisos.
- Los avisos continúan registrándose en el historial.
- El ViewModel no depende de componentes de Android.

### Resultado solicitado al agente

Aplica los cambios directamente sobre los archivos autorizados,
ejecuta la compilación del proyecto y presenta un resumen de
las modificaciones, las pruebas realizadas y cualquier
problema encontrado.

### Análisis de la mejora

Se decidió reutilizar el atributo origen que ya existía
en el modelo Aviso para identificar los recordatorios.

De esta manera, no fue necesario agregar un nuevo atributo
de prioridad ni modificar el repositorio.

También se centralizó la emisión de avisos en el ViewModel
para evitar repetir la lógica de registro y emisión.

---

## Prompt 2 - Notificaciones expandibles con BigTextStyle

### Rol

Actúa como un desarrollador Android especializado en
NotificationCompat y experiencia de usuario.

### Contexto

AvisosCampus dispone de dos canales de notificaciones:
Avisos y Urgentes.

Las notificaciones muestran actualmente un título y una
descripción corta, pero necesitan presentar información
adicional cuando el usuario las expande.

### Objetivo

Implementa notificaciones expandibles mediante
NotificationCompat.BigTextStyle.

### Instrucciones

1. Inspecciona el archivo Notificador.kt y verifica que
   los dos canales implementados anteriormente se mantengan.

2. Trabaja exclusivamente en la rama mejora-ia-avisos.

3. Modifica directamente el archivo:

    - notificacion/Notificador.kt

4. Implementa NotificationCompat.BigTextStyle dentro
   del constructor de la notificación.

5. Utiliza bigText() para mostrar el contenido completo.

6. Utiliza setBigContentTitle() para conservar el título
   cuando la notificación se encuentre expandida.

7. Utiliza setSummaryText() para identificar la aplicación.

8. Genera un texto complementario según el origen del aviso:

    - BOTON: información relacionada con las entregas.
    - RECORDATORIO: información sobre actividades pendientes.
    - MODO_AVION: información sobre la conectividad.

9. Mantén el texto original en setContentText() para
   la presentación contraída de la notificación.

10. Conserva el funcionamiento de los canales, permisos,
    identificadores y apertura de la aplicación.

### Restricciones

- Modificar únicamente Notificador.kt.
- No modificar el ViewModel ni el Repository.
- No modificar los modelos existentes.
- No modificar el diseño de la aplicación.
- No instalar nuevas dependencias.
- No eliminar los canales Avisos y Urgentes.
- Mantener PendingIntent.FLAG_IMMUTABLE.
- No modificar el contenido original del historial.
- No realizar commits ni push automáticamente.

### Criterios de aceptación

- El proyecto compila correctamente.
- Las notificaciones pueden expandirse.
- Al expandir una notificación se muestra el texto completo.
- El título se conserva en ambas presentaciones.
- Los recordatorios mantienen el canal Urgentes.
- Las notificaciones normales mantienen el canal Avisos.
- El historial continúa funcionando sin modificaciones.
- Al tocar una notificación se abre la aplicación.

### Resultado solicitado al agente

Implementa la mejora directamente en Notificador.kt,
compila el proyecto y presenta un resumen de los cambios
junto con las instrucciones para comprobar la expansión
de las notificaciones en el emulador.

### Análisis de la mejora

Se incorporó BigTextStyle sin modificar el ViewModel,
el modelo ni el repositorio.

Para demostrar la expansión se agregaron textos
complementarios según el origen de cada aviso.

Se conservó el mensaje original en el historial y se
mantuvieron los dos canales implementados anteriormente.

---

## Prompt 3 - Receptor de conexión y desconexión del cargador

### Rol

Actúa como un desarrollador Android especializado en
BroadcastReceiver, Jetpack Compose, corrutinas y
arquitectura MVVM.

### Contexto

AvisosCampus cuenta con un BroadcastReceiver que detecta
los cambios del modo avión.

La interfaz utiliza un switch para activar o desactivar
la escucha y el ViewModel registra los eventos mediante
el repositorio.

Además, la aplicación cuenta con dos canales de
notificaciones y soporte para BigTextStyle.

### Objetivo

Implementa un receptor independiente que detecte la conexión
y desconexión del cargador, genere notificaciones locales
y registre los eventos en el historial.

### Archivos autorizados

Crear:

- receiver/CargadorReceiver.kt

Modificar:

- model/Modelos.kt
- viewmodel/AvisosViewModel.kt
- ui/PantallaAvisos.kt
- ui/HistorialAvisos.kt
- notificacion/Notificador.kt

### Instrucciones

1. Inspecciona la implementación existente del receptor
   del modo avión y reutiliza su estructura como referencia.

2. Trabaja exclusivamente en la rama mejora-ia-avisos.

3. Crea CargadorReceiver como una clase que extienda
   BroadcastReceiver.

4. Detecta las siguientes acciones del sistema:

    - Intent.ACTION_POWER_CONNECTED
    - Intent.ACTION_POWER_DISCONNECTED

5. Comunica el estado del cargador mediante un callback
   de tipo (Boolean) -> Unit.

6. Mantén onReceive() corto y sin operaciones bloqueantes.

7. Agrega CARGADOR al enum Origen para identificar
   correctamente los nuevos avisos.

8. Implementa un StateFlow<Boolean> en el ViewModel
   para controlar la escucha del cargador.

9. Agrega los métodos necesarios para activar la escucha
   y procesar los cambios de conexión.

10. Reutiliza la función emitirAviso() para registrar
    y emitir los eventos del cargador.

11. Agrega un switch independiente con el texto
    Escuchar el cargador en la tarjeta de receptores.

12. Registra el receptor dinámicamente mediante
    ContextCompat.registerReceiver() y DisposableEffect.

13. Utiliza RECEIVER_NOT_EXPORTED para el registro
    del receptor de eventos del sistema.

14. Libera el receptor mediante unregisterReceiver()
    dentro de onDispose.

15. Actualiza el historial para identificar los avisos
    del cargador mediante un distintivo verde.

16. Actualiza los bloques when que utilizan Origen
    para contemplar el nuevo valor CARGADOR.

17. Conserva BigTextStyle y utiliza el canal Avisos
    para las notificaciones del cargador.

18. Reutiliza los colores, tipografías y componentes
    visuales existentes en la aplicación.

### Restricciones

- No modificar la rama main.
- No modificar el Repository.
- No agregar Context al ViewModel.
- No agregar NotificationManager al ViewModel.
- No registrar receptores permanentes en el Manifest.
- No dejar receptores registrados después de onDispose.
- No generar avisos cuando el switch esté desactivado.
- No eliminar el receptor del modo avión.
- No eliminar los canales de notificaciones existentes.
- No eliminar BigTextStyle.
- No instalar dependencias innecesarias.
- No realizar commits ni push automáticamente.

### Criterios de aceptación

- La aplicación compila correctamente.
- Existe un switch independiente para el cargador.
- Al conectar el cargador se genera una notificación.
- Al desconectarlo se genera otra notificación.
- Ambos eventos se registran en el historial.
- Los avisos del cargador tienen su propio distintivo.
- Al desactivar el switch no se generan nuevos avisos.
- El receptor se libera correctamente mediante onDispose.
- El modo avión y los recordatorios siguen funcionando.
- El ViewModel mantiene su independencia de Android.

### Pruebas solicitadas

Utiliza un emulador Android para comprobar los eventos
del cargador mediante ADB:

```powershell
adb shell dumpsys battery unplug
adb shell dumpsys battery set ac 1
adb shell dumpsys battery reset
```

Comprueba la conexión, desconexión y desactivación
del receptor.

### Resultado solicitado al agente

Crea y modifica directamente los archivos autorizados.
Ejecuta la compilación y presenta un resumen de los cambios,
las pruebas realizadas y los posibles errores encontrados.

Si no tienes acceso al emulador, indica qué pruebas deben
realizarse manualmente sin afirmar que fueron ejecutadas.

### Análisis de la mejora

Se reutilizó la estructura del receptor del modo avión
para implementar el receptor del cargador.

Fue necesario agregar un nuevo origen al modelo y actualizar
Notificador e HistorialAvisos para mantener la compatibilidad
de los bloques when.

Se mantuvo el registro dinámico mediante DisposableEffect
y la liberación del receptor mediante onDispose.

---

## Registro y análisis de los prompts

Los siguientes prompts son versiones estructuradas para
simular instrucciones dirigidas a un agente de programación
con acceso directo al proyecto.

La asistencia utilizada para preparar las implementaciones
fue ChatGPT. No se atribuyen respuestas a Gemini que no
hayan sido obtenidas realmente de esa herramienta.

| Prompt que escribí | Respuesta de la IA | Qué cambié o descarté | Por qué |
|---|---|---|---|
| Implementación de dos canales de notificaciones | Propuso crear el canal Urgentes y diferenciar los recordatorios de los avisos normales. | Descarté agregar un atributo urgente al modelo y modificar el repositorio. Reutilicé Origen.RECORDATORIO y mantuve los cambios en Notificador y ViewModel. | Para evitar modificaciones innecesarias y respetar las capas indicadas en la guía. |
| Notificaciones expandibles con BigTextStyle | Propuso utilizar BigTextStyle para mostrar información adicional al expandir una notificación. | Limité los cambios a Notificador.kt y conservé el mensaje original del historial. | Para mantener la independencia de la mejora y no modificar la lógica del ViewModel. |
| Receptor del cargador | Propuso crear un BroadcastReceiver, un switch independiente y registrar los eventos en el historial. | Reutilicé emitirAviso(), agregué CARGADOR al enum Origen y mantuve el registro dinámico mediante DisposableEffect. | Para evitar duplicar código, mantener la arquitectura MVVM y liberar correctamente el receptor. |

---

## Control de cambios

| Commit | Descripción |
|---|---|
| 1 | feat(notificacion): agrega canal urgente para recordatorios |
| 2 | feat(notificacion): agrega texto expandible con BigTextStyle |
| 3 | feat(receptor): agrega deteccion de conexion y desconexion del cargador |

## Validación técnica

Durante las mejoras se mantuvieron los siguientes criterios:

- Separación de responsabilidades mediante MVVM.
- Uso del Repository para registrar los avisos.
- ViewModel sin Context ni NotificationManager.
- PendingIntent inmutable.
- Registro dinámico de los receptores.
- Liberación de receptores mediante onDispose.
- Conservación de las funcionalidades de la primera fase.
- Cambios independientes para cada mejora.
- Conservación de la rama main.

## Conclusión

El apoyo de la inteligencia artificial permitió plantear
mejoras sobre la aplicación existente.

Fue importante revisar las propuestas antes de aplicarlas,
evitar modificaciones innecesarias y mantener la arquitectura
del proyecto.

Las mejoras se organizaron en commits independientes para
facilitar su revisión y explicar los cambios realizados
durante la sustentación.
