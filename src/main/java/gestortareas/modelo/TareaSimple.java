package gestortareas.modelo;

import gestortareas.estado.Terminada;

import java.time.LocalDate;

//patron Composite.
//tarea que no tiene subtareas.

public class TareaSimple extends Tarea {

    public TareaSimple(int id, String titulo, String responsable, Prioridad prioridad, LocalDate fechaLimite) {
        super(id, titulo, responsable, prioridad, fechaLimite);
    }

    //100 % si está terminada y 0 % en cualquier otro estado
    @Override
    public double getAvance() {
        if (getEstado() instanceof Terminada) {
            return 100;
        }
        return 0;
    }

    //una tarea simple siempre se puede terminar
    @Override
    public boolean puedeTerminarse() {
        return true;
    }
}
