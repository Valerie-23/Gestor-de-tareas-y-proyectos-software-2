package gestortareas.orden;

import gestortareas.modelo.Tarea;

import java.util.Comparator;

//patron Strategy.
//ordena de la prioridad mas alta a la mas baja

public class OrdenPorPrioridad implements CriterioOrden {

    @Override
    public String getNombre() {
        return "Por prioridad";
    }

    @Override
    public Comparator<Tarea> comparador() {
        return (a, b) -> a.getPrioridad().compareTo(b.getPrioridad());
    }
}
