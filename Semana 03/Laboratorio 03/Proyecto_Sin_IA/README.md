# Laboratorio 03: Registro de Producto

**Estudiante:** Ramses Alvarez  
**Curso:** Programación en Móviles  
**Institución:** Tecsup

---

## 📱 Descripción del Proyecto
Aplicación móvil desarrollada en **Android Studio** utilizando **Kotlin** y **Jetpack Compose**. La aplicación permite registrar la información de un producto (nombre, precio unitario y cantidad) para realizar el cálculo automático del importe total y mostrar un resumen detallado utilizando componentes de Material Design 3.

---

## ✨ Características Implementadas
- **Restricción de entradas (Validación de tipeo):**
    - **Nombre del producto:** Acepta únicamente letras, números y espacios.
    - **Precio:** Acepta números decimales.
    - **Cantidad:** Acepta exclusivamente números enteros.
- **Cálculo de importe:** Multiplica el precio por la cantidad ingresada.
- **Visualización de resumen:** Muestra el resultado dentro de un componente `Card`.
- **Confirmación visual:** Muestra un mensaje en color verde (`✓ Producto registrado correctamente`).

---

## 📷 Resultado en Emulador

![img.png](img.png)

---

## 🤖 Mejora con IA

| Prompt que usé | Qué generó Gemini | Qué acepté o corregí (y por qué) |
| :--- | :--- | :--- |
| "Agrega validación de campos vacíos mostrando un mensaje de error en rojo en lugar de la Card, y un botón Limpiar para vaciar el formulario." | Código con la variable `mensajeError`, renderizado condicional del mensaje en rojo y un botón `OutlinedButton` para reiniciar estados. | Acepté la estructura lógica. Agregué la propiedad `isError` para resaltar bordes en rojo, personalicé el texto de advertencia con `⚠️` y añadí `Locale` para eliminar warnings de compilación. |