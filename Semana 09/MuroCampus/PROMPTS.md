
# MuroCampus - Fase 2: Mejoras asistidas por IA

## 1. Información del proyecto

- Proyecto: MuroCampus
- Laboratorio: Semana 09
- Curso: Programación en Móviles
- Rama base: main
- Rama de mejoras: mejora-ia-muro
- Lenguaje: Kotlin
- Interfaz: Jetpack Compose
- Arquitectura: MVVM + Repository
- Comunicación HTTP: Retrofit y OkHttp
- Servicio REST: JSONPlaceholder

## 2. Objetivo de la fase 2

Implementar mejoras funcionales y visuales sobre el proyecto
MuroCampus utilizando asistencia de inteligencia artificial,
manteniendo la arquitectura original y la separación de
responsabilidades entre las diferentes capas.

Los siguientes prompts están reconstruidos a partir de las
instrucciones y mejoras trabajadas durante esta fase.

## 3. Reglas generales para el agente de IA

- Trabajar directamente sobre el proyecto existente.
- Revisar la estructura y el código antes de modificar archivos.
- Trabajar únicamente en la rama mejora-ia-muro.
- No modificar la rama main.
- Mantener la arquitectura MVVM + Repository.
- No colocar llamadas directas a la API en los Composables.
- No utilizar Retrofit, PostApi ni HttpException directamente
  dentro del ViewModel.
- Mantener el manejo de errores mediante Result y UiState.
- No utilizar GlobalScope.
- No ignorar las excepciones mediante bloques catch vacíos.
- No modificar funcionalidades ajenas a la mejora solicitada.
- No utilizar dependencias innecesarias.
- No realizar commits ni push automáticamente.
- Verificar la compilación después de cada mejora.
- Informar los archivos modificados y explicar los cambios.

---

# PROMPT 01 - Búsqueda de publicaciones por título

## Rol del agente

Actúa como un desarrollador Android senior especializado
en Kotlin, Jetpack Compose, StateFlow y arquitectura MVVM.

Tienes acceso directo al proyecto MuroCampus y debes
implementar los cambios directamente sobre sus archivos.

## Contexto

El proyecto MuroCampus permite consultar publicaciones
mediante la API REST de JSONPlaceholder.

Actualmente cuenta con un listado de publicaciones,
un filtro por usuario y una pantalla de detalle.

El proyecto utiliza Retrofit, Repository, ViewModel
y estados de interfaz mediante UiState.

La rama de trabajo es mejora-ia-muro.

## Objetivo

Implementar un buscador que permita filtrar las publicaciones
por su título sin realizar nuevas peticiones a la API
cada vez que el usuario escriba.

## Archivos permitidos

- viewmodel/PostsViewModel.kt
- ui/PantallaLista.kt

Si necesitas modificar otro archivo, justifica primero
por qué es necesario.

## Requisitos funcionales

1. Agregar un campo de búsqueda en la pantalla de listado.
2. Permitir escribir cualquier texto para buscar publicaciones.
3. Filtrar las publicaciones utilizando su título.
4. Ignorar diferencias entre mayúsculas y minúsculas.
5. Actualizar los resultados mientras el usuario escribe.
6. Permitir limpiar el contenido del buscador.
7. Mostrar nuevamente las publicaciones correspondientes
   cuando la búsqueda esté vacía.
8. Mantener el filtro por usuario existente.
9. Permitir utilizar la búsqueda y el filtro simultáneamente.
10. Mostrar un mensaje cuando no existan resultados.
11. Mantener la navegación hacia el detalle.
12. Mostrar la cantidad de publicaciones encontradas.

## Requisitos técnicos

- Administrar el texto de búsqueda desde el ViewModel.
- Utilizar StateFlow para exponer el estado.
- Realizar el filtrado sobre las publicaciones descargadas.
- No realizar llamadas HTTP por cada carácter ingresado.
- Mantener una fuente de datos que permita recuperar
  las publicaciones cuando se limpie la búsqueda.
- Utilizar collectAsStateWithLifecycle en la interfaz.
- Mantener los estados Cargando, Exito y Error.

## Requisitos visuales

- Incorporar un campo de búsqueda con icono de lupa.
- Agregar un icono para limpiar el texto.
- Mantener una distribución ordenada.
- Conservar los filtros por usuario.
- Mostrar un contador de resultados.
- Diseñar un estado vacío cuando no existan coincidencias.

## Restricciones

- No modificar PostApi.kt.
- No modificar RetrofitClient.kt.
- No modificar PostRepository.kt.
- No cambiar los modelos existentes.
- No eliminar funcionalidades implementadas anteriormente.
- No trasladar la lógica de filtrado a los Composables.
- No realizar commits ni push.

## Criterios de aceptación

- Una palabra existente en un título muestra coincidencias.
- Una palabra inexistente muestra un estado vacío.
- Limpiar la búsqueda recupera las publicaciones.
- El filtro por usuario continúa funcionando.
- La búsqueda puede combinarse con el filtro por usuario.
- El detalle de las publicaciones continúa funcionando.
- El proyecto compila correctamente.

## Instrucciones de ejecución

Revisa los archivos existentes e implementa directamente
la mejora dentro del proyecto.

No entregues únicamente ejemplos de código o sugerencias.

Al finalizar, indica:

- Archivos modificados.
- Cambios realizados.
- Funcionamiento del filtrado.
- Pruebas ejecutadas.
- Resultado de la compilación.

No avances hacia otra mejora.

## Respuesta de la IA

La IA propuso administrar el texto de búsqueda desde el
ViewModel y aplicar un filtro sobre las publicaciones
que ya se encontraban descargadas.

También propuso agregar un campo de búsqueda,
un botón para limpiar el texto y un mensaje
cuando no existieran resultados.

## Cambios y decisiones

Se integró la búsqueda con el filtro por usuario.

Se incorporó un contador de resultados y se mantuvo
la navegación hacia el detalle de cada publicación.

## Justificación

La búsqueda local evita realizar peticiones HTTP
innecesarias y permite mantener la lógica de filtrado
fuera de la interfaz.

## Commit

Agrega busqueda de publicaciones por titulo

---

# PROMPT 02 - Interceptor con encabezado Accept

## Rol del agente

Actúa como un desarrollador Android senior especializado
en Retrofit, OkHttp y consumo de servicios REST.

Tienes acceso directo al proyecto MuroCampus y debes
modificar su configuración de comunicación HTTP.

## Contexto

MuroCampus utiliza Retrofit para consumir la API
de JSONPlaceholder y OkHttp como cliente HTTP.

El proyecto ya cuenta con un archivo RetrofitClient.kt
que centraliza la configuración de la comunicación.

La búsqueda por título implementada anteriormente
debe mantenerse funcionando.

La rama de trabajo es mejora-ia-muro.

## Objetivo

Implementar un interceptor de OkHttp que agregue
automáticamente el encabezado:

Accept: application/json

a las peticiones HTTP realizadas por la aplicación.

## Archivo permitido

- data/remote/RetrofitClient.kt

## Requisitos funcionales

1. Crear un interceptor de OkHttp.
2. Obtener la petición HTTP original.
3. Construir una petición que conserve sus propiedades.
4. Agregar el encabezado Accept: application/json.
5. Continuar la ejecución de la petición modificada.
6. Registrar el interceptor en el cliente OkHttp.
7. Mantener el funcionamiento de las peticiones existentes.
8. Permitir verificar el encabezado mediante Logcat.

## Requisitos técnicos

- Utilizar un Interceptor de OkHttp.
- Mantener la URL base de JSONPlaceholder.
- Mantener la configuración de serialización JSON.
- Conservar ignoreUnknownKeys.
- Mantener el interceptor de registro existente.
- Configurar el interceptor de manera centralizada.
- Evitar agregar manualmente el encabezado
  en cada método de PostApi.
- No modificar el cuerpo de las peticiones.
- No modificar los métodos HTTP existentes.

## Restricciones

- No modificar PostApi.kt.
- No modificar PostRepository.kt.
- No modificar PostsViewModel.kt.
- No modificar las pantallas.
- No agregar dependencias innecesarias.
- No cambiar la arquitectura.
- No realizar commits ni push.

## Criterios de aceptación

- Las peticiones incluyen Accept: application/json.
- El encabezado puede observarse en Logcat.
- El listado de publicaciones continúa funcionando.
- El filtro por usuario continúa funcionando.
- La búsqueda continúa funcionando.
- Las operaciones GET y POST se mantienen operativas.
- El proyecto compila correctamente.

## Instrucciones de ejecución

Inspecciona RetrofitClient.kt y modifica directamente
su configuración de OkHttp.

Conserva todos los componentes existentes que no estén
relacionados con la mejora solicitada.

Al finalizar, informa:

- Archivo modificado.
- Funcionamiento del interceptor.
- Orden de los interceptores.
- Procedimiento para verificar el encabezado.
- Resultado de la compilación.

No avances hacia otra mejora.

## Respuesta de la IA

La IA propuso utilizar un interceptor que obtiene
la petición original, agrega el encabezado Accept
y continúa la comunicación mediante chain.proceed.

## Cambios y decisiones

Se incorporó el interceptor en RetrofitClient.kt
sin modificar las demás capas de la aplicación.

Se mantuvo el registro de peticiones mediante OkHttp
para comprobar el encabezado en Logcat.

## Justificación

Centralizar el encabezado permite evitar código repetido
en cada método de la API y mantener una configuración
uniforme para todas las peticiones.

## Evidencia

En Logcat se verificó la presencia del encabezado:

Accept: application/json

## Commit

Agrega interceptor con encabezado Accept

---

# PROMPT 03 - Eliminación de publicaciones con DELETE

## Rol del agente

Actúa como un desarrollador Android senior especializado
en Kotlin, Retrofit, Jetpack Compose y arquitectura
MVVM + Repository.

Tienes acceso directo al proyecto y debes implementar
la funcionalidad solicitada sobre sus archivos existentes.

## Contexto

MuroCampus permite listar publicaciones, consultar
sus detalles y comentarios, filtrar por usuario,
buscar por título y crear publicaciones.

La aplicación utiliza JSONPlaceholder como servicio REST.

Actualmente no existe una funcionalidad para eliminar
publicaciones desde la pantalla de detalle.

La rama de trabajo es mejora-ia-muro.

## Objetivo

Implementar la eliminación de publicaciones mediante
el método HTTP DELETE, respetando la arquitectura
MVVM + Repository.

## Archivos permitidos

- data/remote/PostApi.kt
- data/PostRepository.kt
- viewmodel/PostsViewModel.kt
- ui/PantallaDetalle.kt
- MainActivity.kt

Si es necesario modificar algún componente adicional,
explica el motivo y limita los cambios a esta mejora.

## Requisitos de la API

Agregar una operación DELETE utilizando la ruta:

posts/{id}

El identificador debe enviarse mediante @Path.

Utilizar Response<Unit> para comprobar el resultado
de la operación HTTP.

## Requisitos del Repository

- Incorporar una operación para eliminar publicaciones.
- Implementar la operación en PostRepositoryRemote.
- Invocar el método DELETE de PostApi.
- Comprobar si la respuesta HTTP fue exitosa.
- Devolver Result.success cuando corresponda.
- Devolver Result.failure cuando ocurra un error.
- Respetar la cancelación de corrutinas.
- Mantener las operaciones existentes.

## Requisitos del ViewModel

- Incorporar un estado para la eliminación.
- Ejecutar la operación mediante el Repository.
- Administrar los estados Cargando, Exito y Error.
- Actualizar las publicaciones disponibles
  después de una eliminación exitosa.
- Mantener la búsqueda y el filtro por usuario.
- Evitar operaciones duplicadas mientras se elimina.
- Permitir limpiar el estado de eliminación.
- No acceder directamente a Retrofit ni PostApi.

## Requisitos de la interfaz

1. Agregar un botón Eliminar en el detalle.
2. Utilizar un color que identifique la acción destructiva.
3. Mostrar una ventana de confirmación.
4. Permitir cancelar la operación.
5. Mostrar un indicador mientras se elimina.
6. Deshabilitar acciones mientras la operación está activa.
7. Mostrar un mensaje cuando ocurra un error.
8. Mostrar una confirmación cuando la operación sea exitosa.
9. Regresar al listado después de eliminar correctamente.
10. Evitar que la publicación eliminada vuelva a aparecer
    durante la sesión actual.

## Consideraciones sobre JSONPlaceholder

JSONPlaceholder simula las operaciones DELETE.

La respuesta exitosa no significa que el recurso
haya sido eliminado permanentemente del servidor.

Por ello, mantener el resultado de la eliminación
localmente durante la sesión de la aplicación.

No presentar esta funcionalidad como una eliminación
persistente en una base de datos real.

## Restricciones

- La pantalla no debe llamar directamente a la API.
- El ViewModel solo debe comunicarse con el Repository.
- No utilizar GlobalScope.
- No ignorar excepciones.
- No eliminar funcionalidades anteriores.
- No agregar una base de datos innecesariamente.
- No realizar commits ni push.

## Criterios de aceptación

- El botón Eliminar aparece en el detalle.
- La ventana de confirmación permite cancelar.
- Confirmar ejecuta la petición DELETE.
- Una respuesta exitosa actualiza el listado.
- La publicación deja de aparecer durante la sesión.
- Los errores se muestran correctamente.
- Las demás funcionalidades continúan operativas.
- El proyecto compila correctamente.

## Instrucciones de ejecución

Revisa la implementación existente de PostApi,
PostRepository y PostsViewModel.

Implementa directamente la funcionalidad en el proyecto,
conectando las capas correspondientes.

Al finalizar, informa:

- Archivos modificados.
- Flujo de eliminación entre las capas.
- Manejo de respuestas HTTP.
- Manejo de errores.
- Limitaciones de JSONPlaceholder.
- Pruebas realizadas.
- Resultado de la compilación.

No avances hacia otra mejora.

## Respuesta de la IA

La IA propuso incorporar el método DELETE en PostApi,
procesar su respuesta desde el Repository y administrar
el estado de eliminación mediante el ViewModel.

También propuso agregar una confirmación en la interfaz
y actualizar el listado después de eliminar.

## Cambios y decisiones

Se incorporó una ventana de confirmación antes
de ejecutar la eliminación.

Se mantuvo la eliminación local durante la sesión
debido a las limitaciones de JSONPlaceholder.

## Justificación

La confirmación evita eliminaciones accidentales.

El Repository centraliza la operación HTTP y permite
que el ViewModel trabaje con Result sin depender
directamente de Retrofit.

## Commit

Permite eliminar publicaciones con DELETE

---

# PROMPT 04 - Edición de publicaciones con PATCH

## Rol del agente

Actúa como un desarrollador Android senior especializado
en Kotlin, Retrofit, Jetpack Compose y arquitectura MVVM.

Tienes acceso directo al proyecto MuroCampus y debes
implementar la funcionalidad de edición sobre el código
existente.

## Contexto

MuroCampus permite consultar, buscar, crear y eliminar
publicaciones utilizando JSONPlaceholder.

La aplicación ya cuenta con una pantalla de detalle
y un ViewModel que administra las operaciones.

Se necesita permitir la modificación de publicaciones
sin reemplazar completamente el recurso.

La rama de trabajo es mejora-ia-muro.

## Objetivo

Implementar la edición de publicaciones mediante
el método HTTP PATCH.

Agregar una pantalla de edición que permita modificar
el título y el contenido de una publicación existente.

## Archivos permitidos

- model/Modelos.kt
- data/remote/PostApi.kt
- data/PostRepository.kt
- viewmodel/PostsViewModel.kt
- ui/PantallaDetalle.kt
- ui/PantallaEditar.kt
- MainActivity.kt

## Requisitos de los modelos

- Incorporar un modelo serializable para los datos
  que serán enviados durante la edición.
- Utilizar los nombres de propiedades esperados
  por JSONPlaceholder.
- No modificar innecesariamente los modelos existentes.
- Enviar únicamente los datos correspondientes
  a la actualización.

## Requisitos de la API

- Incorporar el método HTTP PATCH.
- Utilizar la ruta posts/{id}.
- Enviar el identificador mediante @Path.
- Enviar los datos modificados mediante @Body.
- Procesar la publicación devuelta por el servicio.

## Requisitos del Repository

- Agregar una operación de edición.
- Implementar la operación en PostRepositoryRemote.
- Ejecutar la petición PATCH mediante PostApi.
- Devolver el resultado utilizando Result.
- Manejar correctamente las excepciones.
- Respetar la cancelación de corrutinas.
- Mantener las operaciones anteriores.

## Requisitos del ViewModel

- Incorporar un estado para la edición.
- Ejecutar la actualización mediante el Repository.
- Administrar los estados Cargando, Exito y Error.
- Actualizar los datos locales después de editar.
- Mantener actualizado el detalle de la publicación.
- Mantener la búsqueda y el filtro por usuario.
- Evitar solicitudes duplicadas.
- Permitir limpiar el estado de edición.
- No utilizar Retrofit directamente.

## Requisitos de la interfaz

1. Agregar un botón Editar en la pantalla de detalle.
2. Crear una pantalla denominada PantallaEditar.kt.
3. Mostrar el título actual de la publicación.
4. Mostrar el contenido actual de la publicación.
5. Permitir modificar ambos campos.
6. Validar que los campos no estén vacíos.
7. Agregar un botón para guardar los cambios.
8. Mostrar un indicador mientras se guarda.
9. Mostrar los errores correspondientes.
10. Informar cuando la edición sea exitosa.
11. Permitir regresar a la pantalla de detalle.
12. Mostrar los datos actualizados durante la sesión.

## Requisitos visuales

- Mantener la identidad visual de la aplicación.
- Utilizar campos de texto claramente identificados.
- Incorporar un icono para la acción de editar.
- Diferenciar las acciones Editar y Eliminar.
- Mantener una distribución ordenada.
- Mostrar mensajes de confirmación comprensibles.

## Consideraciones sobre JSONPlaceholder

Las operaciones PATCH son simuladas.

El servicio devuelve una respuesta con los datos
modificados, pero no los almacena permanentemente.

La aplicación debe reflejar los cambios localmente
durante la sesión.

## Restricciones

- No reemplazar PATCH por POST.
- No realizar llamadas HTTP desde los Composables.
- No introducir Retrofit en el ViewModel.
- No modificar funcionalidades ajenas a la edición.
- No eliminar la funcionalidad DELETE.
- No modificar innecesariamente las dependencias.
- No realizar commits ni push.

## Criterios de aceptación

- El botón Editar permite abrir la pantalla de edición.
- Los campos contienen los datos actuales.
- Los campos vacíos no permiten guardar.
- Guardar ejecuta una petición PATCH.
- Una respuesta exitosa actualiza los datos locales.
- Los errores se muestran correctamente.
- La navegación funciona correctamente.
- El resto de las mejoras continúa funcionando.
- El proyecto compila correctamente.

## Instrucciones de ejecución

Inspecciona los modelos, la API, el Repository,
el ViewModel y las pantallas existentes.

Implementa directamente los cambios necesarios.

No entregues solamente ejemplos de implementación.

Al finalizar, informa:

- Archivos creados.
- Archivos modificados.
- Flujo de la operación PATCH.
- Validaciones incorporadas.
- Manejo de estados y errores.
- Pruebas realizadas.
- Resultado de la compilación.

No avances hacia otra mejora.

## Respuesta de la IA

La IA propuso incorporar una operación PATCH en la API,
un modelo para los datos de actualización y una nueva
pantalla de edición.

También propuso utilizar el Repository y el ViewModel
para mantener el flujo de actualización separado
de la interfaz.

## Cambios y decisiones

Se incorporaron campos con los datos actuales
de la publicación.

Se agregaron validaciones, estados de carga
y mensajes de confirmación.

Se mantuvieron los cambios localmente durante la sesión.

## Justificación

PATCH permite actualizar determinados datos de un recurso
sin necesidad de reemplazarlo completamente.

Mantener la actualización dentro del Repository y el
ViewModel permite conservar la arquitectura del proyecto.

## Commit

Permite modificar publicaciones con PATCH

---

# PROMPT 05 - Rediseño profesional de MuroCampus

## Rol del agente

Actúa como un desarrollador Android senior y diseñador
de interfaces especializado en Jetpack Compose,
Material Design 3, experiencia de usuario y accesibilidad.

Tienes acceso directo al proyecto MuroCampus.

Debes implementar un rediseño visual profesional
modificando directamente los archivos del proyecto.

## Contexto

MuroCampus es una aplicación de publicaciones
orientada a una comunidad universitaria.

Actualmente cuenta con las siguientes funcionalidades:

- Listado de publicaciones.
- Filtro por usuario.
- Búsqueda por título.
- Consulta de detalles.
- Consulta de comentarios.
- Creación de publicaciones.
- Eliminación mediante DELETE.
- Edición mediante PATCH.
- Interceptor con encabezado Accept.
- Manejo de estados de carga y error.

Todas las funcionalidades deben conservarse.

La rama de trabajo es mejora-ia-muro.

## Objetivo

Rediseñar la interfaz de MuroCampus para conseguir
una identidad visual propia, moderna, ordenada
y profesional.

Evitar que la aplicación conserve únicamente
los colores, formas y tipografías predeterminados
del proyecto base.

## Archivos permitidos

- MainActivity.kt
- ui/Componentes.kt
- ui/PantallaDetalle.kt
- ui/theme/Color.kt
- ui/theme/Theme.kt
- ui/theme/Type.kt

No modificar archivos de otras capas.

## Identidad visual

Implementar una identidad visual basada en una
combinación de azul institucional, blanco y tonos
neutros.

Utilizar la siguiente paleta como referencia:

- Azul principal: #176B98
- Azul oscuro: #124B70
- Azul claro: #E4EEF5
- Azul profundo: #102F46
- Fondo general: #F1F5F9
- Blanco: #FFFFFF
- Bordes suaves: #DCE5ED
- Superficie secundaria: #EAF0F6
- Texto principal: #202B38
- Texto secundario: #64748B
- Rojo de error: #B3261E
- Fondo de error: #FCE8E6
- Verde de éxito: #287D46
- Fondo de éxito: #E8F5E9

Mantener suficiente contraste entre los elementos.

## Requisitos del tema

- Personalizar el esquema de colores de Material 3.
- Definir colores para fondos y superficies.
- Personalizar los colores de botones.
- Definir formas y bordes redondeados.
- Evitar el esquema de colores predeterminado.
- Mantener consistencia entre todas las pantallas.

## Requisitos de tipografía

- Definir una jerarquía visual propia.
- Diferenciar títulos principales y secundarios.
- Utilizar tamaños adecuados para el contenido.
- Diferenciar los textos informativos y las acciones.
- Utilizar pesos tipográficos coherentes.
- Mantener una lectura cómoda.

## Requisitos de navegación

- Rediseñar la barra superior.
- Incorporar un icono relacionado con la comunidad
  universitaria.
- Mantener el nombre MuroCampus.
- Mostrar títulos según la pantalla actual.
- Incorporar una acción clara para regresar.
- Rediseñar el botón de creación de publicaciones.
- Mantener las rutas y acciones existentes.

## Requisitos del listado

- Utilizar tarjetas blancas con bordes suaves.
- Incorporar avatares de usuario.
- Diferenciar los usuarios mediante colores.
- Mostrar el identificador del usuario.
- Mostrar el título con una jerarquía clara.
- Mostrar una vista previa del contenido.
- Incorporar una sección de acceso al detalle.
- Mantener la búsqueda por título.
- Mantener el filtro por usuario.
- Mantener el contador de resultados.

## Requisitos del detalle

- Mostrar claramente el autor de la publicación.
- Incorporar un avatar.
- Mostrar el identificador de la publicación.
- Presentar el título y contenido completos.
- Diferenciar visualmente el contenido de las acciones.
- Organizar los botones Editar y Eliminar.
- Utilizar colores adecuados para cada acción.
- Mantener la confirmación de eliminación.
- Mantener los estados de carga y error.

## Requisitos de comentarios

- Diseñar un encabezado para la sección.
- Mostrar la cantidad de comentarios.
- Presentar cada comentario en una tarjeta individual.
- Incorporar avatares con iniciales obtenidas del correo.
- Diferenciar los avatares mediante colores.
- Mostrar el correo del comentarista.
- Mostrar el título del comentario.
- Mostrar el contenido completo.
- Numerar los comentarios.
- Mantener una separación visual adecuada.
- Mostrar un estado cuando no existan comentarios.

## Requisitos de estados de interfaz

Rediseñar los componentes de carga y error.

El estado de carga debe incluir:

- Indicador de progreso.
- Mensaje informativo.
- Distribución centrada.

El estado de error debe incluir:

- Icono de advertencia.
- Mensaje comprensible.
- Botón Reintentar.
- Colores que identifiquen el error.

## Requisitos técnicos

- Utilizar componentes de Jetpack Compose.
- Mantener Material Design 3.
- Reutilizar componentes visuales.
- Utilizar iconos personalizados cuando sea necesario.
- Mantener áreas táctiles adecuadas.
- No agregar dependencias innecesarias.
- Conservar las firmas de las funciones utilizadas
  por otras pantallas.

## Restricciones

- No modificar PostApi.kt.
- No modificar RetrofitClient.kt.
- No modificar PostRepository.kt.
- No modificar PostsViewModel.kt.
- No modificar los modelos de datos.
- No modificar los métodos HTTP.
- No cambiar la lógica de negocio.
- No eliminar funcionalidades existentes.
- No modificar la rama main.
- No realizar commits ni push.

## Criterios de aceptación

- La aplicación presenta una identidad visual propia.
- Los colores y tipografías están personalizados.
- Las tarjetas tienen una presentación organizada.
- Los comentarios tienen un diseño diferenciado.
- Los botones Editar y Eliminar funcionan.
- La búsqueda y los filtros continúan funcionando.
- La creación de publicaciones continúa funcionando.
- La navegación se mantiene operativa.
- Los estados de carga y error se muestran correctamente.
- El proyecto compila correctamente.

## Instrucciones de ejecución

Revisa primero la estructura visual del proyecto.

Modifica directamente los archivos autorizados.

No entregues únicamente sugerencias de diseño,
imágenes de referencia o ejemplos aislados.

Al finalizar, informa:

- Archivos modificados.
- Paleta de colores aplicada.
- Cambios de tipografía.
- Componentes reutilizables incorporados.
- Cambios en la navegación.
- Mejoras de accesibilidad.
- Funcionalidades conservadas.
- Resultado de la compilación.

No realices commits automáticamente.

## Respuesta de la IA

La IA propuso personalizar los colores, tipografías,
formas, tarjetas, iconos y encabezados.

También propuso utilizar componentes reutilizables
para los avatares, estados de carga y errores.

## Cambios y decisiones

Se aplicó una identidad visual basada en colores azules,
fondos claros y tarjetas blancas.

Se incorporaron avatares con colores diferenciados,
iconos personalizados y una jerarquía visual propia.

Durante la revisión se identificaron observaciones
relacionadas con la interacción de las tarjetas
y la presentación de los comentarios.

Estas observaciones se corrigieron dentro
del mismo commit de rediseño.

---

# PROMPT 05.1 - Corrección de la interacción de las tarjetas

## Rol del agente

Actúa como un desarrollador Android especializado
en Jetpack Compose y experiencia de usuario.

Tienes acceso directo al proyecto MuroCampus.

Debes corregir el comportamiento de las tarjetas
sin modificar las funcionalidades de otras pantallas.

## Contexto

Las tarjetas de publicaciones permiten acceder
a la pantalla de detalle.

Actualmente toda la tarjeta es pulsable y además
existe una sección inferior con el texto
Ver publicación y una flecha.

Esto genera una interacción redundante.

## Objetivo

Conservar el texto Ver publicación y la flecha,
pero permitir acceder al detalle únicamente
mediante esa sección inferior.

## Archivo permitido

- ui/Componentes.kt

## Requisitos

1. Conservar el diseño actual de la tarjeta.
2. Conservar el avatar y los datos del usuario.
3. Conservar el título y contenido.
4. Conservar el texto Ver publicación.
5. Conservar la flecha hacia la derecha.
6. Eliminar el evento de clic de la tarjeta completa.
7. Trasladar el evento onClick a la sección inferior.
8. Permitir pulsar tanto el texto como la flecha.
9. Evitar que el título, contenido o avatar
   abran la pantalla de detalle.
10. Incorporar una respuesta visual al pulsar.
11. Mantener un área táctil mínima de 48 dp.
12. Mantener la firma existente de TarjetaPost.

## Restricciones

- No eliminar el texto Ver publicación.
- No eliminar la flecha.
- No modificar la navegación existente.
- No modificar el ViewModel.
- No modificar la API.
- No modificar el Repository.
- No crear un commit independiente.
- Mantener esta corrección dentro del commit 5.

## Criterios de aceptación

- Pulsar el título no abre el detalle.
- Pulsar el contenido no abre el detalle.
- Pulsar el avatar no abre el detalle.
- Pulsar Ver publicación abre el detalle.
- Pulsar la flecha abre el detalle.
- La tarjeta conserva su diseño.
- El proyecto compila correctamente.

## Instrucciones de ejecución

Modifica directamente Componentes.kt.

Utiliza una tarjeta sin evento onClick y aplica
Modifier.clickable únicamente a la sección inferior.

Conserva los demás componentes existentes.

Al finalizar, informa el cambio realizado y verifica
que no se haya modificado la navegación.

## Respuesta de la IA

Inicialmente, la IA interpretó que debía eliminar
el texto Ver publicación y la flecha, conservando
toda la tarjeta como área pulsable.

Esa propuesta no correspondía a la interacción
que se buscaba implementar.

## Qué cambié o descarté

Se descartó la eliminación del texto y la flecha.

Se solicitó conservar ambos elementos y trasladar
el evento onClick desde el Card hacia el Row inferior.

También se incorporó Modifier.clickable y una altura
mínima de 48 dp para la sección pulsable.

## Justificación

La sección Ver publicación identifica claramente
la acción de navegación.

Limitar el área pulsable permite diferenciar
el contenido informativo de las acciones disponibles.

---

# PROMPT 05.2 - Rediseño del detalle y comentarios

## Rol del agente

Actúa como un desarrollador Android senior y diseñador
de interfaces especializado en Jetpack Compose.

Tienes acceso directo al proyecto MuroCampus.

Debes mejorar la presentación de la pantalla de detalle
y sus comentarios sin modificar la lógica de negocio.

## Contexto

La pantalla de detalle muestra correctamente
las publicaciones y sus comentarios.

Sin embargo, la presentación de los comentarios
resulta demasiado simple y necesita una mejor
distribución visual.

Los botones de edición y eliminación también
pueden organizarse de una manera más clara.

## Objetivo

Rediseñar la pantalla de detalle y la sección
de comentarios para conseguir una presentación
más profesional y consistente con MuroCampus.

## Archivos permitidos

- ui/PantallaDetalle.kt
- ui/Componentes.kt

## Requisitos de la publicación

- Mostrar el avatar del autor.
- Mostrar el identificador del usuario.
- Mostrar el identificador de la publicación.
- Diferenciar el título del contenido.
- Utilizar una tarjeta blanca con bordes redondeados.
- Mostrar la cantidad de comentarios.
- Separar visualmente la sección de acciones.
- Organizar Editar y Eliminar horizontalmente.
- Mantener los estados de carga y error.

## Requisitos de los comentarios

- Crear tarjetas individuales para cada comentario.
- Incorporar avatares circulares.
- Obtener las iniciales desde el correo electrónico.
- Utilizar colores diferenciados para los avatares.
- Mostrar el identificador derivado del correo.
- Mostrar el correo electrónico completo
  según el espacio disponible.
- Mostrar el título del comentario.
- Mostrar el contenido del comentario.
- Numerar los comentarios.
- Incorporar separadores visuales.
- Mostrar un encabezado con icono y contador.
- Mostrar un mensaje cuando no existan comentarios.
- Incorporar un indicador al finalizar el listado.

## Requisitos de las acciones

- Mantener la funcionalidad de edición.
- Mantener la funcionalidad de eliminación.
- Rediseñar la ventana de confirmación.
- Mostrar un indicador mientras se elimina.
- Deshabilitar acciones durante una operación.
- Mostrar errores cuando corresponda.

## Restricciones

- No modificar los modelos de datos.
- No modificar PostApi.kt.
- No modificar PostRepository.kt.
- No modificar PostsViewModel.kt.
- No cambiar los métodos HTTP.
- No agregar dependencias.
- No eliminar funcionalidades.
- No crear un commit independiente.
- Mantener los cambios dentro del commit 5.

## Criterios de aceptación

- La publicación presenta una mejor jerarquía visual.
- Los comentarios tienen avatares y numeración.
- Los títulos y contenidos se muestran correctamente.
- Los botones Editar y Eliminar funcionan.
- La confirmación de eliminación funciona.
- Los estados de carga y error se mantienen.
- La navegación continúa funcionando.
- El proyecto compila correctamente.

## Instrucciones de ejecución

Revisa los componentes visuales existentes.

Implementa directamente el rediseño en los archivos
permitidos.

Reutiliza los colores y avatares definidos
en Componentes.kt y en el tema de la aplicación.

Al finalizar, informa los archivos modificados
y verifica que no se haya alterado la lógica
de las operaciones existentes.

## Respuesta de la IA

La IA propuso utilizar tarjetas blancas,
avatares personalizados, separadores,
numeración y una mejor distribución del contenido.

También propuso organizar horizontalmente
las acciones de edición y eliminación.

## Qué cambié o adapté

Se incorporaron avatares con iniciales derivadas
del correo electrónico.

Se agregaron colores diferenciados, un contador
de comentarios y tarjetas individuales.

Se mantuvieron las funcionalidades de edición
y eliminación.

## Justificación

La separación visual permite distinguir mejor
la publicación principal de sus comentarios.

Los avatares, la numeración y la jerarquía tipográfica
facilitan la lectura y la identificación de cada elemento.

## Commit correspondiente a los prompts 05, 05.1 y 05.2

Rediseño profesional de la interfaz de MuroCampus.

Los tres prompts corresponden a una misma mejora
y se trabajaron dentro del mismo commit.

---

# 4. Tabla de análisis de los prompts

| Prompt que escribí | Qué respondió la IA | Qué cambié o descarté | Por qué |
|---|---|---|---|
| Prompt 01: Implementar búsqueda por título. | Propuso filtrar las publicaciones descargadas desde el ViewModel. | Se combinó la búsqueda con el filtro por usuario y se incorporó un contador. | Para evitar peticiones HTTP innecesarias y mantener la lógica fuera de la interfaz. |
| Prompt 02: Implementar un interceptor Accept. | Propuso modificar las peticiones mediante un interceptor de OkHttp. | Se centralizó el encabezado en RetrofitClient y se comprobó mediante Logcat. | Para evitar repetir el encabezado en cada método de la API. |
| Prompt 03: Implementar DELETE. | Propuso agregar la operación en API, Repository, ViewModel e interfaz. | Se incorporó una confirmación y la eliminación local durante la sesión. | Para evitar eliminaciones accidentales y considerar las limitaciones del servicio. |
| Prompt 04: Implementar PATCH. | Propuso agregar una operación de actualización y una pantalla de edición. | Se incorporaron campos precargados, validaciones y manejo de estados. | Para modificar los datos sin reemplazar completamente el recurso. |
| Prompt 05: Rediseñar MuroCampus. | Propuso personalizar colores, tipografías, tarjetas e iconos. | Se aplicó una identidad visual azul y componentes reutilizables. | Para conseguir una interfaz propia y profesional. |
| Prompt 05.1: Corregir la interacción de las tarjetas. | Inicialmente propuso eliminar Ver publicación y la flecha. | Se descartó esa propuesta y se trasladó el clic a la sección inferior. | Para diferenciar el contenido informativo de la acción de navegación. |
| Prompt 05.2: Mejorar el detalle y comentarios. | Propuso avatares, tarjetas individuales y numeración. | Se incorporaron iniciales, colores diferenciados y acciones organizadas. | Para facilitar la lectura y mejorar la experiencia de usuario. |

---

# 5. Resumen de mejoras implementadas

| N. | Mejora | Capas modificadas |
|---|---|---|
| 1 | Búsqueda por título | ViewModel y View |
| 2 | Interceptor Accept | RetrofitClient |
| 3 | Eliminación con DELETE | API, Repository, ViewModel y View |
| 4 | Edición con PATCH | Model, API, Repository, ViewModel y View |
| 5 | Rediseño profesional | View y Theme |

---

# 6. Arquitectura conservada

El proyecto mantiene la arquitectura MVVM + Repository.

Flujo de una operación HTTP:

Usuario
|
v
View / Jetpack Compose
|
v
PostsViewModel
|
v
PostRepository
|
v
PostRepositoryRemote
|
v
PostApi
|
v
Retrofit / OkHttp
|
v
JSONPlaceholder

Flujo de respuesta:

JSONPlaceholder
|
v
PostApi
|
v
Repository (Result)
|
v
ViewModel (UiState)
|
v
View

La interfaz recibe las acciones del usuario y muestra
los estados correspondientes.

El ViewModel administra los estados y se comunica
con el Repository.

El Repository ejecuta las operaciones mediante la API
y transforma los resultados en Result.

RetrofitClient centraliza la configuración HTTP.

---

# 7. Limitaciones del servicio REST

JSONPlaceholder es un servicio REST utilizado
para realizar pruebas y aprender el funcionamiento
de las peticiones HTTP.

Las operaciones POST, PATCH y DELETE son simuladas.

Aunque el servicio devuelve respuestas exitosas,
los cambios no se almacenan permanentemente.

Por esta razón, las modificaciones y eliminaciones
se reflejan localmente durante la sesión.

En una aplicación real sería necesario utilizar
un servicio que almacene los cambios en una base
de datos persistente.

---

# 8. Pruebas de aceptación

Completar el registro después de comprobar cada
funcionalidad en el emulador.

- [ ] Búsqueda por título.
- [ ] Búsqueda combinada con filtro por usuario.
- [ ] Estado sin resultados.
- [x] Encabezado Accept verificado mediante Logcat.
- [ ] Confirmación de eliminación.
- [ ] Eliminación mediante DELETE.
- [ ] Edición mediante PATCH.
- [ ] Validación de campos vacíos.
- [ ] Actualización local de publicaciones.
- [ ] Navegación hacia el detalle.
- [ ] Interacción limitada a Ver publicación.
- [ ] Visualización de comentarios.
- [ ] Creación de publicaciones.
- [ ] Estado de carga.
- [ ] Estado de error y reintento.
- [ ] Compilación final.

---

# 9. Conclusiones

1. La implementación de las mejoras permitió ampliar
   las funcionalidades de MuroCampus mediante la
   búsqueda, edición y eliminación de publicaciones.

2. La arquitectura MVVM + Repository permitió mantener
   separadas las responsabilidades de la interfaz,
   los estados y las operaciones HTTP.

3. El uso de inteligencia artificial permitió obtener
   propuestas de implementación y diseño que fueron
   revisadas y adaptadas según las necesidades
   del proyecto.

4. El rediseño visual permitió conseguir una identidad
   propia mediante colores, tipografías, avatares
   y componentes personalizados.

5. Las propuestas generadas por la IA requieren revisión,
   ya que pueden interpretar incorrectamente algunos
   requisitos, como ocurrió con el área pulsable
   de las tarjetas de publicaciones.

6. JSONPlaceholder permitió comprender el funcionamiento
   de los métodos HTTP sin necesidad de implementar
   un servidor propio, considerando que las operaciones
   de escritura no son persistentes.
