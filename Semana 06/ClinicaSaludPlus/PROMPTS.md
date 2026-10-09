<!-- COMMIT 1 — Crea este archivo dentro de ClinicaSaludPlus:
     Semana 06/ClinicaSaludPlus/PROMPTS.md -->

# Prompts utilizados — Fase 2 de Clínica SaludPlus

Rama de trabajo: `mejora-ia-clinicaplus`.

## Avance 1: calendario dinámico y horarios reactivos

### Prompt utilizado

Actúa como desarrollador de Android con Kotlin y Jetpack Compose.

Tengo una aplicación de pacientes con un flujo de reserva de citas
que ya funciona. Actualmente, la pantalla de fecha y hora muestra
fechas fijas. Necesito sustituirlas por un calendario dinámico.

Adapta la solución al paquete, componentes y funciones existentes
de mi proyecto. Conserva la firma de la pantalla y sus callbacks
para no romper la navegación.

Requisitos funcionales:

1. Usa java.time.LocalDate para obtener la fecha actual.
2. Muestra exactamente los próximos cinco días hábiles desde hoy,
   incluyendo hoy si es de lunes a viernes.
3. Excluye sábados, domingos y fechas anteriores a hoy.
4. Agrega flechas para desplazar el periodo una semana.
5. Impide retroceder más allá del periodo inicial.
6. Actualiza el encabezado del mes y año según las fechas visibles.
   Si el periodo cruza de mes, muestra ambos meses.
7. Al cambiar de fecha, limpia la hora seleccionada y vuelve
   a consultar los horarios del médico para la nueva fecha.
8. Al cambiar de periodo, limpia las selecciones de fecha y hora.
9. Conserva la consulta al repositorio en memoria y el bloqueo
   de horarios reservados para el mismo médico y fecha.
10. Usa LazyVerticalGrid con tres columnas para los horarios.
11. Habilita Continuar únicamente con fecha y hora disponibles.
12. Revalida la disponibilidad antes de ejecutar el callback.
13. Conserva las fechas en formato ISO yyyy-MM-dd al navegar.

Requisitos visuales:

- Fondo claro, tarjeta celeste con el nombre y especialidad del médico.
- Encabezado de mes y año centrado entre las flechas.
- Cinco tarjetas de días con abreviatura en español y número de día.
- Día y horario seleccionados en azul con texto blanco.
- Horarios sin seleccionar con fondo blanco y borde suave.
- Botón azul de Continuar en la parte inferior.
- No agregues todavía fotografías ni dependencias de imágenes.

Entrega el contenido completo del archivo, con sus imports.
No agregues bases de datos ni cambies el modelo de las citas.

### Respuesta resumida

Se propuso reemplazar FechaHoraScreen.kt por una pantalla que genera
cinco días hábiles mediante LocalDate. Incluye desplazamiento semanal,
encabezado dinámico, selección de fecha y hora, y consulta reactiva
de disponibilidad al repositorio.

La fecha enviada a la siguiente pantalla conserva el formato ISO.

### Ajustes incluidos en la propuesta

- Se utilizó el paquete real repository para importar Repositorio.
- Se evitó memorizar la lista de horarios para conservar su reacción
  a las reservas y cancelaciones.
- Se contemplaron periodos que cruzan de mes o año.
- Se limpian las selecciones al cambiar de periodo.
- Se revalida el horario antes de continuar.

### Correcciones posteriores

Se detectaron errores de compatibilidad al utilizar LocalDate
porque el proyecto admite versiones anteriores a Android API 26.
Se activó isCoreLibraryDesugaringEnabled y se agregó la dependencia
desugar_jdk_libs en app/build.gradle.kts.
Después de sincronizar Gradle, desaparecieron los errores del editor.

### Pruebas pendientes

- Confirmar que aparecen cinco días hábiles y ninguna fecha pasada.
- Confirmar que la flecha anterior está deshabilitada al inicio.
- Avanzar un periodo y regresar al inicial.
- Avanzar hasta cruzar de mes y comprobar el encabezado.
- Seleccionar una hora, cambiar de día y comprobar que se limpia.
- Reservar una cita y volver al mismo médico y fecha:
  el horario reservado debe desaparecer.
- Cancelar esa cita y comprobar que el horario vuelve a estar disponible.
- Comprobar que Continuar exige fecha y hora disponibles.