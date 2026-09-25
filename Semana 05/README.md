# Semana 5: [Nombre del Proyecto / Laboratorio]

Bienvenido al apartado correspondiente a la **Semana 5**. En esta sección se aborda la solución de un proyecto desarrollado mediante diferentes enfoques, comparando la metodología tradicional de programación con el desarrollo asistido por **Inteligencia Artificial (IA)**.

---

## 📁 Contenido del apartado

El directorio de esta semana se encuentra estructurado de las siguientes formas para facilitar su revisión:

### 1. 📂 `Proyecto_Sin_IA/`
* **Descripción:** Implementación del proyecto desarrollada de forma totalmente manual y tradicional, sin el uso de herramientas o asistentes de IA.
* **Propósito:** Funcionar como código base (*baseline*) para comparar la lógica, la estructura y los patrones aplicados sin asistencia externa.

### 2. 🤖 `Proyecto_Con_IA/`
* **Descripción:** Implementación del mismo proyecto, pero utilizando asistentes de IA para la generación, optimización o refactorización del código.
* **Propósito:** Demostrar el impacto de la IA en la velocidad de desarrollo, arquitectura y aplicación de mejores prácticas.

### 3. 🌿 Opción A (Gestión por Ramas)
* **Descripción:** Enfoque alternativo donde el mismo proyecto se gestiona mediante el control de versiones con **Git**, dividiendo las soluciones en distintas ramas para evidenciar la evolución del código.
* **Estructura de Ramas:**
  * `main` / `base`: Contiene el desarrollo base realizado sin IA.
  * `mejora-ia-v2` *(o el nombre de tu rama)*: Contiene las iteraciones, aportaciones e integración de componentes trabajados con asistencia de IA.
* **Propósito:** Facilitar la visualización paso a paso de los cambios (*diffs*) realizados por la IA a lo largo del historial de commits.

---

## 🛠️ Instrucciones de navegación

1. **Exploración por carpetas:** Puedes entrar directamente a las carpetas `Proyecto_Sin_IA/` o `Proyecto_Con_IA/` para revisar los módulos de cada enfoque.
2. **Exploración por ramas (Opción A):** Para ver la evolución del código con IA mediante Git, ejecuta los siguientes comandos en tu terminal:

```bash
# Cambiar a la rama trabajada con IA
git checkout mejora-ia-v2

# Para volver a la versión original sin IA
git checkout main