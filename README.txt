# Gestor de Tareas y Proyectos

Proyecto final de Ingeniería de Software 2: Patrones de Diseño.

Aplicación en Java para organizar el trabajo personal o de un equipo. Permite crear tareas simples y tareas compuestas por subtareas, cambiar su estado siguiendo reglas definidas, deshacer y rehacer acciones, y ordenar las tareas de distintas formas. Tiene una versión por consola y una versión con interfaz gráfica (Swing), y las dos usan el mismo núcleo.

## Integrantes

- Carol Daniela Muñoz Cubides
- Juan Sebastián Sanchez Rosero
- Valerie Triana Álvarez

## Requisitos

- JDK 17 o superior
- Maven (se puede usar el que trae IntelliJ IDEA)

## Cómo ejecutar

### Desde IntelliJ IDEA

1. Abrir la carpeta del proyecto con **File → Open**. IntelliJ lo reconoce como proyecto Maven por el `pom.xml`.
2. Hacer clic en **Reload Maven Project** para que descargue JUnit 5.
3. Para la **consola**: abrir `src/main/java/gestortareas/consola/AppConsola.java`, clic derecho → **Run 'AppConsola.main()'**.
4. Para la **interfaz gráfica**: abrir `src/main/java/gestortareas/gui/AppGui.java`, clic derecho → **Run 'AppGui.main()'**.
5. Para las **pruebas**: clic derecho sobre la carpeta `src/test/java` → **Run 'All Tests'**.

### Desde la terminal

```bash
mvn test                                                 # compila y ejecuta las pruebas
mvn compile                                              # solo compila
java -cp target/classes gestortareas.consola.AppConsola  # versión por consola
java -cp target/classes gestortareas.gui.AppGui          # versión con interfaz gráfica
```

## Funcionalidades

- Crear tareas simples con título, responsable, prioridad (alta, media o baja) y fecha límite.
- Crear tareas compuestas y agregarles subtareas, que pueden ser simples o compuestas, en varios niveles.
- Ver todas las tareas en forma de árbol, con su estado y su porcentaje de avance.
- Cambiar el estado de una tarea. Solo se permiten estos cambios:

  | Desde | Puede pasar a |
  |---|---|
  | Pendiente | En curso |
  | En curso | Pendiente, En revisión |
  | En revisión | En curso, Terminada |
  | Terminada | Ningún estado |

  Una tarea compuesta no se puede terminar si tiene subtareas sin terminar, y a una tarea terminada no se le pueden agregar subtareas.
- Deshacer y rehacer la creación de tareas y los cambios de estado, varias veces seguidas. Si se hace una acción nueva, lo deshecho ya no se puede rehacer.
- Ordenar las tareas por prioridad, por fecha límite o por responsable.

Todas las funciones están disponibles en la consola y en la interfaz gráfica. Los datos se guardan solo en memoria: al cerrar el programa se pierden.

### Consola

Muestra un menú numerado:

```
1. Ver tareas
2. Crear tarea simple
3. Crear tarea compuesta
4. Cambiar estado de una tarea
5. Deshacer
6. Rehacer
7. Ordenar tareas
0. Salir
```

Las tareas se identifican por su número de id. Para crear una subtarea, se escribe el id de la tarea compuesta padre cuando el programa lo pide (o se deja vacío para crear una tarea principal).

### Interfaz gráfica

Una sola ventana con el árbol de tareas y botones para crear tareas simples o compuestas, cambiar el estado, deshacer, rehacer y elegir el orden. Para crear una subtarea, se selecciona primero la tarea compuesta en el árbol.

## Patrones de diseño

| Patrón | Categoría | Para qué se usa | Clases |
|---|---|---|---|
| Composite | Estructural | Tratar igual a las tareas simples y a las compuestas | `Tarea`, `TareaSimple`, `TareaCompuesta` |
| State | Comportamiento | Controlar qué cambios de estado están permitidos | `EstadoTarea`, `Pendiente`, `EnCurso`, `EnRevision`, `Terminada` |
| Command | Comportamiento | Deshacer y rehacer acciones | `Comando`, `CrearTareaComando`, `CambiarEstadoComando`, `HistorialComandos` |
| Strategy | Comportamiento | Ordenar las tareas con distintos criterios | `CriterioOrden`, `OrdenPorPrioridad`, `OrdenPorFecha`, `OrdenPorResponsable` |
| Facade | Estructural | Dar una sola entrada al núcleo para la consola y la interfaz gráfica | `GestorTareas` |

La explicación de cada patrón (problema, alternativa descartada, ventajas, desventajas y principios SOLID) está en el documento de diseño.

## Estructura del proyecto

| Paquete | Contenido |
|---|---|
| `gestortareas.modelo` | Tareas simples y compuestas, y la prioridad |
| `gestortareas.estado` | Estados de una tarea y sus transiciones |
| `gestortareas.comando` | Comandos e historial para deshacer y rehacer |
| `gestortareas.orden` | Criterios de orden |
| `gestortareas.nucleo` | Fachada `GestorTareas` y registro de tareas |
| `gestortareas.consola` | Interfaz por consola (`MenuConsola`, `AppConsola`) |
| `gestortareas.gui` | Interfaz gráfica (`VentanaPrincipal`, `AppGui`) |

El núcleo no depende de la consola ni de la interfaz gráfica. Las dos interfaces solo usan `GestorTareas`.

## Pruebas

Pruebas unitarias con JUnit 5 para las clases de cada patrón, en `src/test/java`:

| Prueba | Qué revisa |
|---|---|
| `TareaCompuestaTest` | Composite: cálculo del avance en varios niveles y reglas de las tareas compuestas |
| `EstadoTareaTest` | State: cambios de estado permitidos y no permitidos |
| `HistorialComandosTest` | Command: deshacer y rehacer, y que una acción nueva borre lo que se podía rehacer |
| `CriterioOrdenTest` | Strategy: los tres criterios de orden |
| `GestorTareasTest` | Facade: el sistema completo usado solo a través de `GestorTareas` |

## Documentación

- `DOCUMENTACION COMPLETA.docx`: alcance, requisitos, problemas de diseño, patrones elegidos, arquitectura y declaración de uso de inteligencia artificial.
- `DIAGRAMAS GESTOR DE TAREAS.docx`: diagramas UML (casos de uso, clases general, un diagrama por patrón y diagrama de secuencia).
- `CONCLUSIONES.md`: qué aprendimos y qué cambiaríamos del diseño.

## Uso de inteligencia artificial

El uso de herramientas de inteligencia artificial está declarado en la sección 6 de `DOCUMENTACION COMPLETA.docx`.
