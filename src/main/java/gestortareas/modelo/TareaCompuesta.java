package gestortareas.modelo;

import gestortareas.estado.Terminada;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

//patron Composite.
//tarea que tiene subtareas y las subtareas tambien pueden ser simples o compuestas, por eso se pueden establecer en varios niveles

public class TareaCompuesta extends Tarea {
    private final List<Tarea> subtareas = new ArrayList<>();
    public TareaCompuesta(int id, String titulo, String responsable, Prioridad prioridad, LocalDate fechaLimite) {
        super(id, titulo, responsable, prioridad, fechaLimite);
    }

//agrega una subtarea. No se puede si la tarea esta terminada

    public void agregar(Tarea tarea) {
        if (!getEstado().permiteAgregarSubtareas()) {
            throw new IllegalStateException(
                    "No se pueden agregar subtareas a una tarea en estado " + getEstado().getNombre());
        }
        subtareas.add(tarea);
    }

    public void quitar(Tarea tarea) {
        subtareas.remove(tarea);
    }

    public List<Tarea> getSubtareas() {
        return subtareas;
    }

    //el avance de esta tarea es el promedio del avance de sus subtareas

    @Override
    public double getAvance() {
        if (subtareas.isEmpty()) {
            if (getEstado() instanceof Terminada) {
                return 100;
            }
            return 0;
        }
        double suma = 0;
        for (Tarea subtarea : subtareas) {
            suma = suma + subtarea.getAvance();
        }
        return suma / subtareas.size();
    }

    //solo termina si sus subtareas estan terminadas

    @Override
    public boolean puedeTerminarse() {
        for (Tarea subtarea : subtareas) {
            if (!(subtarea.getEstado() instanceof Terminada)) {
                return false;
            }
        }
        return true;
    }
}
