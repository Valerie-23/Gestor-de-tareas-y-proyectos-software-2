package gestortareas.orden;

import gestortareas.modelo.Tarea;

import java.util.Comparator;

//patron Strategy.
//ordena de la fecha limite mas cercana a la mas lejana.
//las tareas sin fecha quedan al final.

public class OrdenPorFecha implements CriterioOrden {

    @Override
    public String getNombre() {
        return "Por fecha límite";
    }

    @Override
    public Comparator<Tarea> comparador() {
        return (a, b) -> {
            if (a.getFechaLimite() == null && b.getFechaLimite() == null) {
                return 0;
            }
            if (a.getFechaLimite() == null) {
                return 1;
            }
            if (b.getFechaLimite() == null) {
                return -1;
            }
            return a.getFechaLimite().compareTo(b.getFechaLimite());
        };
    }
}
