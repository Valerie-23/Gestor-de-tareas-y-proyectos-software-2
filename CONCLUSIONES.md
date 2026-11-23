# Conclusiones del proyecto realizado 

## Qué aprendimos
-En este proyecto aprendimos que los patrones de diseño no son solo algo teórico. Al hacerlo vimos que se usan cuando el código empieza a llenarse de if y switch en las partes que más cambian: el tipo de tarea, el estado, las acciones del usuario y la forma de ordenar.
-Con Composite pudimos tratar igual una tarea simple y una tarea con subtareas. De esta manera el cálculo del avance y la regla de que no se puede terminar una tarea con subtareas pendientes (RF-6) quedaron escritos en un solo lugar.
-Con State cada estado sabe a cuáles puede pasar. Por eso Terminada rechaza cualquier cambio sin que Tarea tenga un switch de mayor tamaño.
-Con Command deshacer y rehacer funcionan igual para cualquier acción. Cada acción es una clase que sabe hacerse y deshacerse, y el historial solo guarda dos pilas.
-Con Strategy agregar una forma de ordenar nueva es crear una clase, sin tocar el resto del código.
-Con Facade la consola y la ventana gráfica usan el mismo núcleo y no repiten la lógica.
También notamos que los patrones traen más clases y que al principio es un poco más complicado ubicarse. En cambio, cada parte se puede probar por separado y es más fácil de expandir.

## Qué cambiaríamos
-Para guardar los datos, todo se almacena en memoria y se pierde al cerrar el programa. Con un archivo o una base de datos sería más útil.
-Implementar lo que dejamos fuera, como los recordatorios por fecha límite, que se podrían hacer con Observer, y las plantillas de tareas recurrentes, con Prototype.
-Editar y eliminar tareas, cada una con su comando para poder deshacerla.
-Reunir las reglas de transición en un solo lugar, están repartidas en cada clase de estado y, para ver todas, toca abrir varias.
-Mejorar cómo se agregan subtareas, ahora hay que revisar que la tarea sea compuesta antes de agregar. Esa es la desventaja de Composite y habría que buscar una forma más práctica.
