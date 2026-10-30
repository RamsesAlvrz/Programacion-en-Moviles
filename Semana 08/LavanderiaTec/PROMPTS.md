# Prompts utilizados – LavaCampus

**Proyecto:** LavaCampus – Aplicación de gestión de lavandería  
**Tecnologías:** Kotlin, Jetpack Compose, Room, DataStore y arquitectura MVVM  
**Rama de trabajo:** `mejora-ia-lavaplus`

> **Nota:** Los siguientes prompts se han reconstruido y organizado a partir de las cuatro mejoras implementadas. Están escritos como instrucciones que podrían entregarse a un agente de IA con acceso al proyecto; no son transcripciones literales de los mensajes originales.

---

## Prompt 1 – Mejora A: Identificación de pedidos atrasados

### Texto enviado a la IA (prompt estructurado)

> Actúa como un desarrollador Android especializado en Kotlin, Jetpack Compose, Room y arquitectura MVVM. Tienes acceso directo a mi proyecto **LavaCampus**, por lo que debes revisar los archivos existentes y realizar los cambios necesarios sin crear un proyecto nuevo.
>
> **Objetivo:** implementar la identificación de pedidos atrasados en el módulo del administrador.
>
> **Requisitos:**
> 1. Considera atrasado un pedido cuando su fecha prevista de entrega ya pasó y su estado **no** es `ENTREGADO` ni `CANCELADO`.
> 2. En la bandeja de pedidos del administrador, identifica visualmente los pedidos atrasados mediante un indicador o texto de color rojo que sea fácil de reconocer.
> 3. En el panel principal del administrador, muestra un contador de pedidos atrasados que se actualice con la información almacenada en Room.
> 4. Mantén la lógica de consulta y clasificación en las capas correspondientes (DAO, repositorio o ViewModel); la interfaz Compose debe encargarse de mostrar los resultados.
> 5. Respeta los estados de pedido que ya existen y no alteres los flujos de gestión, entrega, cancelación ni cobro.
> 6. Reutiliza los componentes, nombres y convenciones actuales del proyecto. Modifica únicamente los archivos necesarios.
>
> **Verificación:** prueba un pedido cuya fecha ya venció y que continúa pendiente; debe aparecer marcado y aumentar el contador. Comprueba también que los pedidos entregados o cancelados no se contabilicen como atrasados aunque tengan fechas anteriores.
>
> Al finalizar, indica qué archivos modificaste, por qué fue necesario y cómo comprobar el resultado desde la aplicación.

### Qué generó la IA

Se incorporó la detección de pedidos atrasados, su identificación visual en la bandeja del administrador y un indicador de cantidad en el panel. La solución aprovechó la información de los pedidos almacenada en Room y la presentó mediante los ViewModels y pantallas existentes.

### Qué tuve que corregir yo

Revisé que el criterio de atraso utilizara la fecha prevista y excluyera expresamente los estados `ENTREGADO` y `CANCELADO`. También comprobé que el indicador visual y el contador mostraran resultados coherentes durante las pruebas.

### ¿Respeta MVVM?

**Sí.** La información se obtiene desde la capa de datos, se procesa o expone mediante el ViewModel y se representa en las pantallas Compose. La interfaz no necesita consultar directamente la base de datos.

---

## Prompt 2 – Mejora B: Descuento del 10 % por volumen

### Texto enviado a la IA (prompt estructurado)

> Actúa como un agente de desarrollo Android con acceso completo al código de **LavaCampus**. Antes de modificar archivos, analiza cómo se registran los servicios, se calculan los importes y se guardan los pedidos. Trabaja sobre la arquitectura actual con Kotlin, Room, Jetpack Compose y MVVM.
>
> **Objetivo:** aplicar automáticamente un **10 % de descuento** cuando el cliente solicite el servicio **«Lavado y secado»** con una cantidad **mayor a 5 kg**.
>
> **Requisitos:**
> 1. El descuento se activa únicamente cuando la cantidad del servicio «Lavado y secado» supera los **5 kg**; con exactamente 5 kg no corresponde aplicar el descuento.
> 2. Calcula el 10 % sobre el importe del servicio que cumple la condición, **no** sobre otros servicios adicionales incluidos en el pedido.
> 3. Muestra en el resumen de Nuevo pedido el subtotal, el descuento aplicado y el total final de forma clara.
> 4. Asegúrate de que el importe final calculado se conserve correctamente al registrar el pedido en Room y al consultarlo posteriormente.
> 5. Ubica las reglas de cálculo en la capa de negocio o en el ViewModel, evitando duplicar operaciones dentro de componentes visuales.
> 6. No modifiques los precios base de los servicios ni las demás reglas de registro, validación y navegación.
> 7. Mantén el diseño actual y cambia solamente los archivos necesarios.
>
> **Verificación:** comprueba pedidos de 5 kg y de más de 5 kg, tanto con el servicio de lavado solo como combinado con otros servicios. Confirma que el descuento sea exactamente el 10 % del lavado elegible y que el total mostrado coincida con el registrado.
>
> Al terminar, resume los archivos modificados y los casos de prueba ejecutados.

### Qué generó la IA

Se incorporó el cálculo automático del descuento por volumen y su presentación en el resumen del pedido. El importe final quedó integrado al flujo de registro, de manera que el usuario puede conocer cuánto ahorra antes de confirmar la solicitud.

### Qué tuve que corregir yo

Verifiqué el límite de **más de 5 kg** y que el descuento se aplicara exclusivamente al servicio «Lavado y secado», sin reducir el precio de servicios adicionales. También revisé que el resumen y el importe guardado fueran consistentes.

### ¿Respeta MVVM?

**Sí.** La regla del descuento pertenece a la lógica de negocio y se comunica a la interfaz a través del ViewModel. Room conserva los datos del pedido, mientras que Compose presenta el cálculo al usuario.

---

## Prompt 3 – Mejora C: Repetir un pedido desde el historial

### Texto enviado a la IA (prompt estructurado)

> Eres un agente de IA encargado de mejorar mi aplicación Android **LavaCampus**. Revisa el flujo existente de historial de pedidos, creación de pedidos y consulta de servicios. Implementa la funcionalidad dentro del proyecto actual, sin reemplazar su arquitectura MVVM ni modificar innecesariamente otras pantallas.
>
> **Objetivo:** permitir que un estudiante repita un pedido anterior desde su historial, sin volver a seleccionar manualmente todos los servicios.
>
> **Requisitos:**
> 1. Agrega la acción **«Pedir de nuevo»** en los pedidos del historial donde corresponda, incluidos los pedidos finalizados o cancelados.
> 2. Cuando el usuario seleccione esa acción, recupera del pedido anterior los servicios y las cantidades solicitadas.
> 3. Abre la pantalla **Nuevo pedido** con esos datos precargados para que el estudiante pueda revisarlos y modificarlos antes de confirmar.
> 4. **No dupliques automáticamente** el pedido anterior ni reutilices su precio histórico. Calcula los importes con las **tarifas vigentes** y aplica las reglas actuales, incluido el descuento por volumen cuando corresponda.
> 5. Al guardar, crea un pedido nuevo con su propia identificación; el pedido original y su historial deben permanecer intactos.
> 6. Si el usuario entra a Nuevo pedido mediante la opción habitual, el formulario debe abrirse vacío y no conservar cantidades de un pedido repetido anteriormente.
> 7. Conserva el comportamiento de navegación y el patrón Repository–ViewModel–Compose del proyecto.
>
> **Verificación:** repite un pedido desde el historial, comprueba que se precarguen sus líneas y cantidades, modifica algún dato y registra la nueva solicitud. Después crea un pedido desde cero y verifica que no se arrastren selecciones anteriores.
>
> Indica los archivos modificados y explica brevemente cómo evitaste alterar el pedido original.

### Qué generó la IA

Se añadió la posibilidad de iniciar un pedido nuevo a partir de uno existente. La aplicación recupera los servicios y cantidades del historial, los carga en el formulario de Nuevo pedido y vuelve a calcular el importe con las condiciones actuales.

### Qué tuve que corregir yo

Comprobé que la acción no generara una copia inmediata del pedido, sino que permitiera revisar los datos antes de guardarlos. También revisé que un pedido nuevo normal iniciara sin cantidades anteriores y que la navegación regresara a la pantalla adecuada.

### ¿Respeta MVVM?

**Sí.** La recuperación de los datos del pedido y la preparación de sus líneas se realiza a través del ViewModel y las capas de datos. Las pantallas Compose gestionan la interacción y la navegación, sin asumir directamente la persistencia.

---

## Prompt 4 – Mejora D: Gráfico de gastos mensuales del estudiante

### Texto enviado a la IA (prompt estructurado)

> Actúa como un desarrollador Android con experiencia en **Kotlin, Jetpack Compose, Room y MVVM**. Tienes acceso al proyecto existente **LavaCampus**. Revisa cómo se consultan los pedidos del estudiante, cómo se almacenan sus importes y cómo funciona la pantalla Perfil. Realiza los cambios directamente en los archivos actuales, manteniendo la estructura del proyecto.
>
> **Objetivo:** incorporar en el **perfil del estudiante** un gráfico de barras que muestre sus **gastos mensuales durante los últimos seis meses**.
>
> **Requisitos:**
> 1. Consulta los pedidos correspondientes al estudiante que tiene la sesión iniciada; no mezcles datos de otros usuarios.
> 2. Agrupa los importes por mes para construir una serie de **seis meses**, ordenada cronológicamente.
> 3. Muestra los meses sin movimientos con valor **cero**, para que el gráfico mantenga siempre el mismo periodo.
> 4. Presenta los resultados mediante un gráfico de barras integrado en `PerfilScreen`, con etiquetas de mes e importes comprensibles.
> 5. Utiliza los importes realmente registrados y define el tratamiento de los pedidos cancelados de acuerdo con la lógica de cobros existente; no los sumes como gasto pagado si no corresponde.
> 6. Coloca la obtención y preparación de datos en el repositorio y/o `PerfilViewModel`; la pantalla Compose debe limitarse a observar el estado y dibujar el gráfico.
> 7. Mantén las opciones existentes del perfil, las preferencias de usuario y la navegación.
> 8. Respeta los colores y el tema de la aplicación para que el gráfico siga siendo legible con la apariencia clara u oscura.
>
> **Verificación:** entra con un estudiante que tenga pedidos registrados, revisa que los importes se asignen al mes correcto y que aparezcan seis meses. Comprueba también un usuario sin movimientos y verifica que la pantalla no falle ni muestre gastos de otra cuenta.
>
> Al finalizar, detalla los archivos modificados y explica cómo se calcula cada barra a partir de los datos de Room.

### Qué generó la IA

Se incorporó al perfil del estudiante una visualización de gastos mensuales mediante barras, alimentada con información de los pedidos. El gráfico permite consultar de forma rápida cómo se distribuyen los importes durante los últimos seis meses.

### Qué tuve que corregir yo

Revisé la presentación del gráfico, su legibilidad y la relación entre los importes y los meses mostrados. Además, comprobé su integración con el perfil y los ajustes visuales de la aplicación para que no afectara las funcionalidades existentes.

### ¿Respeta MVVM?

**Sí.** `PerfilViewModel` prepara y expone la información que necesita la vista; `PerfilScreen` representa los datos. La información de origen se obtiene de la persistencia existente, sin incorporar consultas directas a Room dentro del componente gráfico.

---

