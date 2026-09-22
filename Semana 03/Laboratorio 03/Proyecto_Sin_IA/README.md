# Laboratorio 03: Registro de Producto (Parte A - Sin IA)

**Estudiante:** Ramses Alvarez  
**Curso:** Programación en Móviles  
**Institución:** Tecsup

---

## 📱 Descripción del Proyecto
Aplicación móvil desarrollada en **Android Studio** utilizando **Kotlin** y **Jetpack Compose**. La aplicación permite registrar la información de un producto (nombre, precio unitario y cantidad) para realizar el cálculo automático del importe total y mostrar un resumen detallado utilizando componentes modernos de Material Design 3.

---

## ✨ Características Implementadas
- **Restricción de entradas (Validación de tipeo):**
    - **Nombre del producto:** Acepta únicamente letras, números y espacios.
    - **Precio:** Acepta números decimales.
    - **Cantidad:** Acepta exclusivamente números enteros.
- **Cálculo de importe:** Multiplica el precio por la cantidad ingresada.
- **Visualización de resumen:** Muestra el resultado dentro de un componente `Card` estilizado.
- **Confirmación visual:** Muestra un mensaje en color verde (`✓ Producto registrado correctamente`).

---

## ✨ Remember
Es una función de Jetpack Compose que guarda un valor en memoria para que no se borre ni se vuelva a calcular desde cero cada vez que la pantalla se redibuja (recomposición).

---

## 📷 Resultado en Emulador

<img width="373" height="832" alt="image" src="https://github.com/user-attachments/assets/8bcca9c0-6602-4b2a-8bf1-a82f9ae8add8" />


<img width="950" height="427" alt="image" src="https://github.com/user-attachments/assets/532eb329-915c-43f3-abe8-0f85fdcd913f" />
