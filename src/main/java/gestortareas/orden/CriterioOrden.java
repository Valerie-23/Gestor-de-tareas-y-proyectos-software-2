package gestortareas.orden;

import gestortareas.modelo.Tarea;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

// Patrón Strategy: rol Estrategia. Cada forma de ordenar es una clase que implementa esta interfaz.
public interface CriterioOrden {

    // nombre que se muestra en el menú y en la ventana
    String getNombre();

    // regla de comparación de este criterio
    Comparator<Tarea> comparador();

    // devuelve una copia ordenada; no modifica la lista original
    default List<Tarea> ordenar(List<Tarea> tareas) {
        List<Tarea> copia = new ArrayList<>(tareas);
        copia.sort(comparador());
        return copia;
    }
}
