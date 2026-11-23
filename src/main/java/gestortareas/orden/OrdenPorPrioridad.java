package gestortareas.orden;

import gestortareas.modelo.Tarea;
import java.util.Comparator;

// Patrón Strategy: estrategia concreta. De la prioridad más alta a la más baja.
public class OrdenPorPrioridad implements CriterioOrden {

    @Override
    public String getNombre() {
        return "Por prioridad";
    }

    @Override
    public Comparator<Tarea> comparador() {
        return Comparator.comparing(Tarea::getPrioridad); // ALTA, MEDIA, BAJA
    }
}
