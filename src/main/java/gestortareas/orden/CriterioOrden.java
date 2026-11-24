package gestortareas.orden;

import gestortareas.modelo.Tarea;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

//patron Strategy
//una forma de ordenar las tareas. Cada criterio es una clase distinta

public interface CriterioOrden {

    //nombre del criterio para mostrarlo en el menu y en la ventana.
    String getNombre();

    //regla de comparacion de este criterio.
    Comparator<Tarea> comparador();

    //devuelve una copia ordenada, no modifica la lista original.
    default List<Tarea> ordenar(List<Tarea> tareas) {
        List<Tarea> copia = new ArrayList<>(tareas);
        copia.sort(comparador());
        return copia;
    }
}
