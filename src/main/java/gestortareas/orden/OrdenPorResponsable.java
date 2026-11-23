package gestortareas.orden;

import gestortareas.modelo.Tarea;
import java.util.Comparator;

// Patrón Strategy: estrategia concreta. Alfabético por responsable, sin distinguir mayúsculas.
public class OrdenPorResponsable implements CriterioOrden {

    @Override
    public String getNombre() {
        return "Por responsable";
    }

    @Override
    public Comparator<Tarea> comparador() {
        return Comparator.comparing(Tarea::getResponsable,
                Comparator.nullsLast(String.CASE_INSENSITIVE_ORDER));
    }
}
