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

## Avance 3: rediseño visual de Clínica SaludPlus

### Objetivo

Adaptar las pantallas de bienvenida, registro, inicio de sesión,
Inicio y Especialidades al estilo visual de la guía.

Utilizar colores suaves, botones azules, ilustraciones médicas,
iconos grandes y una distribución que aproveche el espacio
disponible de la pantalla.

Conservar la navegación, las validaciones de los formularios,
la autenticación, el repositorio en memoria y el calendario
dinámico implementado en el avance 1.

### Nota sobre los prompts

Los siguientes prompts están estructurados para reproducir
los cambios realizados. No constituyen una transcripción literal
de todos los mensajes de la conversación.

---

### 1. Logo de Clínica SaludPlus

**Prompt para reproducir el recurso:**

Genera un logo para una aplicación móvil llamada Clínica SaludPlus.

Utiliza una cruz médica de bordes redondeados en tonos azul
y turquesa, con un corazón blanco integrado en el centro.

El diseño debe ser limpio y legible en una pantalla móvil.
Entrega únicamente el símbolo, centrado, sin texto, sin marcas
de agua, sin un marco de teléfono y con fondo transparente.

**Respuesta aplicada:**

Se incorporó el recurso logo_saludplus.png en drawable-nodpi.

El logo se utiliza en la pantalla de bienvenida y en Login.

---

### 2. Ilustración del médico para la bienvenida

**Prompt para reproducir el recurso:**

Genera una ilustración de un médico amable para la pantalla
de bienvenida de una aplicación móvil de salud.

El personaje debe vestir una bata blanca, llevar un estetoscopio
y sostener una carpeta azul.

Utiliza un estilo de ilustración moderno y suave, con colores
azul, turquesa y blanco, acompañado de pequeños elementos
vegetales decorativos.

Entrega la ilustración centrada, sin texto, sin marcas de agua,
sin interfaz de aplicación y con fondo transparente.

La composición debe funcionar en una pantalla vertical
y permitir que el personaje completo sea visible.

**Respuesta aplicada:**

Se incorporó el recurso medico_splash.png en drawable-nodpi.

**Corrección realizada:**

Se utilizaron nombres de recursos válidos para Android:
letras minúsculas y guiones bajos.

---

### 3. Pantalla de bienvenida: SplashScreen

**Prompt para reproducir la modificación:**

Rediseña SplashScreen.kt de Clínica SaludPlus utilizando
Kotlin y Jetpack Compose.

Conserva los callbacks onRegistro y onLogin.

Organiza la pantalla con:

- Fondo degradado en tonos celestes suaves.
- Logo de SaludPlus en la parte superior.
- Nombre Clínica SaludPlus centrado y destacado.
- Texto breve de bienvenida.
- Ilustración del médico en la zona central.
- Botón azul Registrarme.
- Opción Ya tengo una cuenta debajo del botón.

Utiliza logo_saludplus.png y medico_splash.png desde drawable-nodpi.

Conserva las proporciones de las imágenes mediante ContentScale.Fit.
Distribuye el espacio para que las imágenes y las acciones
de acceso sean visibles.

Conserva la navegación hacia Registro y Login.
Entrega el archivo completo.

**Respuesta aplicada:**

Se rediseñó la bienvenida con fondo degradado, logo,
nombre de la clínica, ilustración médica y acciones de acceso.

Se distribuyó el espacio mediante pesos en la columna.

**Archivos involucrados:**

- ui/screens/auth/SplashScreen.kt
- res/drawable-nodpi/logo_saludplus.png
- res/drawable-nodpi/medico_splash.png

---

### 4. Pantalla de registro

**Prompt para reproducir la modificación:**

Rediseña RegistroScreen.kt para que sea consistente
con la bienvenida de Clínica SaludPlus.

Utiliza fondo celeste muy claro, títulos oscuros,
campos redondeados, iconos y un botón azul.

Conserva estos campos:

- Nombre.
- Apellido.
- Correo electrónico.
- Teléfono.
- Contraseña.
- Confirmar contraseña.

Conserva Teléfono; no lo reemplaces por cédula de identidad.
Presenta Nombre y Apellido en una misma fila.

Mantén las validaciones:

- Nombre y apellido obligatorios.
- Correo electrónico con formato válido.
- Teléfono de nueve dígitos.
- Contraseña de al menos seis caracteres.
- Coincidencia entre contraseña y confirmación.
- Control de registro mediante Repositorio.registrarUsuario.
- Mensajes de error correspondientes.

Mantén enmascarados los campos de contraseña.

Retira el checkbox de aceptación de términos.
Utiliza esta distribución final:

1. Campos del formulario.
2. Mensajes de error cuando correspondan.
3. Botón Registrarme.
4. Texto Al registrarte aceptas nuestros.
5. Enlace Términos y condiciones.
6. Separador.
7. Texto ¿Ya tienes cuenta? y enlace Iniciar sesión.

Conserva los callbacks onVolver, onRegistrado y onTerminos.
Añade onLogin para abrir Login desde Iniciar sesión.

Permite desplazamiento vertical y adapta el contenido al teclado.
Entrega el archivo completo.

**Respuesta aplicada:**

Se reorganizaron los campos y se actualizaron sus iconos,
colores y bordes.

Se conservaron las validaciones de los datos.
Se retiró el checkbox y se añadió el acceso directo a Login.

**Correcciones realizadas:**

Se conservó el campo Teléfono y la confirmación de contraseña.

El texto de términos se colocó inicialmente encima del botón.
Se corrigió para que aparezca debajo de Registrarme.

La aceptación dejó de depender de un checkbox y se presenta
mediante el texto asociado al registro.

**Archivo involucrado:**

- ui/screens/auth/RegistroScreen.kt

---

### 5. Navegación desde Registro

**Prompt para reproducir la modificación:**

Actualiza la llamada a RegistroScreen en AppNavigation.kt
para incorporar el callback onLogin.

Al seleccionar Iniciar sesión, navega hacia Login.
Al completar correctamente el registro, navega hacia Login.

Conserva la acción Volver y la navegación hacia Términos.
Evita acumular copias de Login y retira Registro de la pila
cuando corresponda.

Conserva las demás rutas y el flujo existente.

**Respuesta aplicada:**

Se actualizó la llamada a RegistroScreen con onLogin.
Se utilizaron popUpTo y launchSingleTop en la navegación a Login.

**Archivo involucrado:**

- navigation/AppNavigation.kt

---

### 6. Pantalla de inicio de sesión

**Prompt para reproducir la modificación:**

Rediseña LoginScreen.kt con el mismo estilo de SplashScreen
y RegistroScreen.

Conserva los callbacks onVolver, onAcceso y onRegistro.

Incluye:

- Opción Volver.
- Logo de SaludPlus.
- Título Iniciar sesión.
- Mensaje de bienvenida.
- Campo de correo electrónico.
- Campo de contraseña.
- Acción Ver u Ocultar contraseña.
- Mensajes de error.
- Botón azul Ingresar.
- Separador.
- Texto ¿No tienes cuenta? y enlace Regístrate.

Utiliza fondo claro, campos redondeados e iconos consistentes
con Registro.

Conserva la validación de campos vacíos, el formato del correo
y la autenticación mediante Repositorio.iniciarSesion.

Ejecuta onAcceso cuando las credenciales sean correctas.
Muestra el error correspondiente cuando sean incorrectas.

La acción Ver u Ocultar solo debe modificar la visibilidad
de la contraseña.

Permite desplazamiento y adapta el contenido al teclado.
Entrega el archivo completo.

**Respuesta aplicada:**

Se incorporó el logo y se actualizaron campos, botones,
textos e iconos.

Se añadió la opción de mostrar u ocultar la contraseña.
Se conservaron la autenticación y los mensajes de error.

**Archivo involucrado:**

- ui/screens/auth/LoginScreen.kt

---

### 7. Iconos reutilizables de la aplicación

**Prompt para reproducir la modificación:**

Crea IconoSaludPlus.kt como componente reutilizable
de Jetpack Compose.

Dibuja mediante Canvas estos iconos:

- Calendario.
- Persona.
- Documento.
- Inicio.
- Campana.
- Corazón.
- Cruz médica.

Permite configurar el tipo, color y tamaño.
Utiliza formas reconocibles y consistentes con una aplicación médica.
Evita añadir una dependencia de iconos adicionales.

**Respuesta aplicada:**

Se creó IconoSaludPlus para utilizarlo en los accesos
de Inicio y en la barra inferior.

**Archivo involucrado:**

- ui/components/IconoSaludPlus.kt

---

### 8. Ilustraciones de especialidades

**Prompt para reproducir la modificación:**

Crea IconoEspecialidad.kt como componente reutilizable
de Jetpack Compose.

Dibuja mediante Canvas una ilustración para cada especialidad
real del proyecto:

- Identificador 1: Medicina general, profesional de salud.
- Identificador 2: Cardiología, corazón con pulso.
- Identificador 3: Dermatología, mano con una marca en la piel.
- Identificador 4: Pediatría, rostro de bebé.
- Identificador 5: Traumatología, hueso.
- Identificador 6: Oftalmología, ojo.

Utiliza fondos suaves de esquinas redondeadas y un color
principal para cada especialidad.

Permite configurar el tamaño.
Utiliza el mismo componente en Inicio y Especialidades.

No cambies el catálogo ni los identificadores del repositorio.

**Respuesta aplicada:**

Se creó un componente compartido con seis ilustraciones.

**Corrección realizada:**

La primera ilustración de Dermatología representaba un rostro.
Se reemplazó por una mano con una marca en la piel para mejorar
su identificación.

**Limitación visual:**

Las ilustraciones son aproximaciones dibujadas con código.
No son las imágenes originales de la guía.

**Archivo involucrado:**

- ui/components/IconoEspecialidad.kt

---

### 9. Pantalla de Inicio

**Prompt para reproducir la modificación:**

Rediseña HomeScreen.kt tomando como referencia Inicio
de la guía.

Conserva estos callbacks:

- onEspecialidades.
- onEspecialidad.
- onCitas.
- onResultados.
- onNotificaciones.

Obtén el usuario actual del repositorio y muestra su nombre
en el saludo. Añade una campana que abra Notificaciones.

Presenta cuatro tarjetas grandes en dos filas:

- Agendar cita: fondo celeste y calendario azul.
- Mis citas: fondo verde y calendario.
- Mis citas: fondo lila y persona.
- Resultados: fondo naranja claro y documento.

Conserva los dos accesos a Mis citas de la referencia.
Ambos deben ejecutar onCitas.

Debajo presenta Especialidades destacadas y Ver todos.
Ver todos debe abrir la pantalla de Especialidades.

Obtén las especialidades mediante especialidadesDestacadas().
Preséntalas en una fila horizontal con ilustración y nombre.
Cada tarjeta debe ejecutar onEspecialidad con su identificador.

Utiliza IconoSaludPlus para los accesos e IconoEspecialidad
para las especialidades.

Mantén los datos reales y toda la navegación.
Entrega el archivo completo.

**Respuesta aplicada:**

Se reorganizó Inicio con saludo, notificaciones,
cuatro accesos y especialidades destacadas.

Se conservaron los callbacks y el repositorio existente.

---

### 10. Tamaño adaptable de Inicio

**Prompt para reproducir la modificación:**

Modifica HomeScreen.kt para aprovechar la altura disponible
y reducir el espacio vacío debajo de las tarjetas.

Conserva Ver todos y la fila horizontal de especialidades.
No conviertas esa sección en una cuadrícula.

Envuelve el contenido en BoxWithConstraints y calcula:

- altoAcceso = ((maxHeight - 220.dp) / 3.4f),
  con un mínimo de 136.dp.
- altoEspecialidad = altoAcceso * 1.4f.
- anchoTarjeta = (maxWidth - 60.dp) / 3.

El cálculo del ancho debe mostrar tres especialidades completas,
considerando 20 dp de margen a cada lado y dos espacios de 10 dp.

Aplica altoAcceso a las cuatro tarjetas de acciones.
Centra sus iconos y textos dentro de la altura disponible.

Utiliza:

- Saludo de 28 sp.
- Iconos de acceso de 48 dp.
- Textos de acceso de 16 sp.
- Ilustraciones de especialidad de 58 dp.
- Nombres de especialidad de 14 sp.

Aplica altoEspecialidad a las tarjetas de especialidades.

Mantén LazyColumn para permitir desplazamiento en pantallas
pequeñas y LazyRow para las especialidades destacadas.

Conserva todas las acciones y entrega el archivo completo.

**Respuesta aplicada:**

Se calcularon las alturas mediante BoxWithConstraints.
Se ampliaron tarjetas, iconos y textos, y se centró el contenido.

Se mantuvo el desplazamiento vertical y horizontal.

**Correcciones realizadas:**

Se reemplazaron las alturas fijas que dejaban espacio vacío.

Se conservó la distribución de la referencia:
cuatro accesos, Ver todos y especialidades en una fila horizontal.

**Archivo involucrado:**

- ui/screens/home/HomeScreen.kt

---

### 11. Barra inferior

**Prompt para reproducir la modificación:**

Actualiza visualmente BarraInferior en Componentes.kt.

Conserva los destinos Inicio, Citas, Resultados y Perfil,
el callback onDestino y las rutas existentes.

Utiliza IconoSaludPlus para los iconos.
Muestra el destino seleccionado en azul y los demás en gris.
Resalta el texto seleccionado mediante negrita.

Utiliza fondo claro y un indicador transparente para eliminar
el fondo de color detrás del icono seleccionado.

Conserva la navegación existente.

**Respuesta aplicada:**

Se actualizaron los iconos, colores y textos.
Se configuró el indicador como transparente.

**Archivo involucrado:**

- ui/components/Componentes.kt

---

### 12. Pantalla de Especialidades

**Prompt para reproducir la modificación:**

Rediseña EspecialidadesScreen.kt tomando como referencia
la pantalla de Especialidades de la guía.

Conserva onVolver y onEspecialidad.

Incluye:

- Flecha para volver.
- Título Especialidades centrado.
- Campo Buscar especialidad.
- Lista vertical de especialidades.

Reemplaza las tarjetas anteriores por filas seleccionables.
Cada fila debe mostrar:

- Ilustración a la izquierda.
- Nombre destacado.
- Descripción debajo del nombre.
- Flecha a la derecha.
- Separador inferior.

Utiliza IconoEspecialidad para mantener los mismos dibujos
y colores que en Inicio.

Conserva la búsqueda mediante Repositorio.buscarEspecialidades.
Muestra un mensaje cuando no existan coincidencias.

Al seleccionar una fila, ejecuta onEspecialidad con su identificador.

Conserva el catálogo real y entrega el archivo completo.

**Respuesta aplicada:**

Se sustituyeron las tarjetas por filas con ilustraciones,
nombre, descripción, flecha y separador.

Se conservaron la búsqueda y la navegación hacia los médicos.

---

### 13. Tamaño adaptable de Especialidades

**Prompt para reproducir la modificación:**

Adapta las seis filas de Especialidades al espacio vertical
disponible para reducir el espacio vacío debajo de la lista.

Coloca BoxWithConstraints en el área de la lista,
después del encabezado y del campo de búsqueda.

Calcula:

altoFila = ((maxHeight - 16.dp) / 6f),
con un mínimo de 92.dp.

Utiliza esa altura como mínimo de cada fila, considerando
el separador inferior de 1 dp.

Mantén el divisor de seis al buscar para que los resultados
no cambien de tamaño según la cantidad de coincidencias.

Utiliza:

- Título de 24 sp.
- Campo de búsqueda con texto de 16 sp.
- Iconos de 60 dp.
- Nombres de especialidad de 18 sp.
- Descripciones de 14 sp.
- Flecha derecha de 32 sp.

Mantén LazyColumn para permitir desplazamiento en pantallas
pequeñas o cuando el contenido necesite más espacio.

Conserva la búsqueda, los separadores y la navegación.
Entrega el archivo completo.

**Respuesta aplicada:**

Se calculó la altura mínima de las filas con BoxWithConstraints.
Se ampliaron los iconos, nombres y descripciones.

La lista mantiene desplazamiento y conserva el tamaño
de las filas durante la búsqueda.

**Archivo involucrado:**

- ui/screens/agendamiento/EspecialidadesScreen.kt

---

### Archivos del avance 3

Rutas relativas a app/src/main/java/com/alvarez/saludplus/:

- ui/screens/auth/SplashScreen.kt
- ui/screens/auth/RegistroScreen.kt
- ui/screens/auth/LoginScreen.kt
- navigation/AppNavigation.kt
- ui/screens/home/HomeScreen.kt
- ui/screens/agendamiento/EspecialidadesScreen.kt
- ui/components/Componentes.kt
- ui/components/IconoSaludPlus.kt
- ui/components/IconoEspecialidad.kt

Recursos:

- app/src/main/res/drawable-nodpi/logo_saludplus.png
- app/src/main/res/drawable-nodpi/medico_splash.png

Documentación:

- PROMPTS.md

Las rutas deben corresponder a la ubicación real de cada archivo
en el proyecto.

### Alcance

El avance contiene principalmente modificaciones visuales.

También se añadió onLogin a RegistroScreen y se retiró
el checkbox de términos por solicitud del usuario.

Se conservaron las validaciones de los campos, la autenticación,
el repositorio en memoria, los callbacks de las pantallas
y el calendario dinámico del avance 1.

Las especialidades reales se conservaron aunque sus nombres
difieren de los mostrados en la referencia.

### Validación pendiente

Actualizar este apartado después de realizar las comprobaciones.
No considerar una prueba aprobada hasta ejecutarla.

- Compilar y ejecutar la aplicación.
- Revisar el logo y la ilustración de bienvenida.
- Abrir Registro y Login desde la bienvenida.
- Probar las validaciones de Registro.
- Abrir Términos desde Registro.
- Abrir Login desde Iniciar sesión en Registro.
- Probar credenciales correctas e incorrectas.
- Probar Ver y Ocultar contraseña.
- Revisar la distribución de Inicio.
- Probar los cuatro accesos de Inicio.
- Abrir Especialidades mediante Ver todos.
- Seleccionar una especialidad desde Inicio.
- Revisar las seis filas de Especialidades.
- Probar búsquedas con resultados y sin resultados.
- Seleccionar una especialidad y abrir sus médicos.
- Comprobar la barra inferior.
- Comprobar el desplazamiento cuando sea necesario.
- Verificar que el calendario dinámico y el agendamiento
  continúan funcionando.