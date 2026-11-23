package gestortareas.orden;

import gestortareas.modelo.Tarea;
import java.time.LocalDate;
import java.util.Comparator;

// Patrón Strategy: estrategia concreta. De la fecha límite más cercana a la más lejana; sin fecha al final.
public class OrdenPorFecha implements CriterioOrden {

    @Override
    public String getNombre() {
        return "Por fecha límite";
    }

    @Override
    public Comparator<Tarea> comparador() {
        return Comparator.comparing(Tarea::getFechaLimite,
                Comparator.nullsLast(Comparator.<LocalDate>naturalOrder()));
    }
}
