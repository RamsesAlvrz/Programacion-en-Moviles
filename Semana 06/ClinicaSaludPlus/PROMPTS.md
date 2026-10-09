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

## Avance 2: completar Confirmar cita y guardar el motivo

### Prompt inicial

Mejora ConfirmarCitaScreen de una aplicación Android desarrollada
con Kotlin y Jetpack Compose. Conserva sus parámetros medicoId,
fecha, hora, onVolver y onConfirmada.

Muestra la fecha recibida en formato ISO como fecha completa en
español. Conserva el formato ISO al guardar la cita.
Comprueba fechas hábiles desde hoy y disponibilidad del horario.
Evita envíos repetidos, conserva el repositorio en memoria y
mantén la navegación después de una reserva correcta.

Utiliza fondo claro, tarjeta celeste del médico, iconos azules
y un botón de confirmación. Entrega el código con sus imports.

### Respuesta inicial resumida

Se propuso una pantalla con fecha en español, iconos dibujados
con Canvas y revalidación de fecha y disponibilidad.
La primera propuesta no incluía el motivo de consulta.

### Prompt de ampliación

Completa la pantalla de confirmación siguiendo la referencia
visual de la guía, conservando el flujo existente.

Adapta los nombres y paquetes a mi proyecto. Agrega:

1. Fotografía local del médico cuando exista un recurso asociado;
   conserva iniciales para los médicos sin fotografía.
2. Nombre y especialidad reales del repositorio.
3. Fecha completa en español y hora elegida.
4. Tipo de atención: Consulta presencial.
5. Dirección de demostración: Av. Los Olivos 123 · Lince.
6. Campo Motivo de consulta opcional.
7. Guardado del motivo en el modelo Cita y en agendarCita.
8. Visualización del motivo en DetalleCitaScreen.
9. Valor predeterminado vacío para mantener compatibles
   las llamadas que no envían motivo.
10. Eliminación de espacios al principio y final del motivo.
11. Conservación del bloqueo de horarios reservados, de la
    sesión y del proceso de cancelación.
12. Desplazamiento vertical y espacio para el teclado.
13. No inventar matrículas médicas ni cambiar especialidades
    únicamente para imitar los textos del ejemplo.

Centraliza el tipo de atención, la dirección y el formato de fecha
en un archivo reutilizable. Mantén el almacenamiento en memoria.
No modifiques AppNavigation ni las pantallas de acceso.

Entrega los archivos completos y, para Repositorio, únicamente
la función que debo reemplazar.

### Prompt de generación de la fotografía

Genera un retrato fotográfico cuadrado de una médica ficticia
para una aplicación académica de clínica.

Mujer latina de aproximadamente 35 años, cabello castaño oscuro
hasta los hombros, sonrisa natural, bata blanca sobre blusa
turquesa y estetoscopio. Encuadre de cabeza y hombros, centrado,
con espacio alrededor del cabello para recorte circular.
Fondo celeste claro e iluminación suave.
Sin texto, logotipos, matrículas, bordes ni marcas de agua.

### Respuesta de la ampliación resumida

Se añadió motivoConsulta a Cita con valor predeterminado vacío.
La función agendarCita recibe el motivo, lo recorta y conserva
la comprobación de disponibilidad antes de guardar la reserva.

Confirmar cita muestra fotografía para Ana Torres, fecha en español,
tipo de atención, dirección y un campo opcional de motivo.
Detalle de cita muestra el motivo guardado o No especificado.

DatosConsulta centraliza la dirección de demostración,
el tipo de atención y el formato de fecha.

### Correcciones realizadas durante el desarrollo

- La primera propuesta omitía el motivo de consulta de la referencia.
  Se añadió el campo y su guardado real en memoria.
- Se sustituyeron las iniciales de Ana Torres por una fotografía
  ficticia generada con IA.
- Se añadieron tipo de atención y dirección.
- Se mantuvo la especialidad real del repositorio.
- No se añadió una matrícula médica sin datos existentes.

### Pruebas pendientes

- Reservar una cita con motivo y comprobarlo en su detalle.
- Reservar otra cita sin motivo y comprobar No especificado.
- Comprobar fecha en español, fotografía y dirección.
- Comprobar que el teclado permite acceder al botón.
- Confirmar que un horario reservado desaparece del calendario.
- Cancelar una cita y comprobar que su horario vuelve a aparecer.
- Comprobar el regreso a Inicio y Mis citas después de reservar.