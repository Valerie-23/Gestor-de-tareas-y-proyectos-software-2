package gestortareas.orden;

import gestortareas.modelo.Tarea;

import java.util.Comparator;

//patron Strategy.
//ordena alfabeticamente por responsable, sin distinguir mayusculas.

public class OrdenPorResponsable implements CriterioOrden {

    @Override
    public String getNombre() {
        return "Por responsable";
    }

    @Override
    public Comparator<Tarea> comparador() {
        return (a, b) -> a.getResponsable().compareToIgnoreCase(b.getResponsable());
    }
}
