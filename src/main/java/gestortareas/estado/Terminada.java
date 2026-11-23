package gestortareas.estado;

import gestortareas.modelo.Tarea;

//patron State.
//una tarea terminada ya no cambia de estado ni recibe subtareas nuevas.

public class Terminada extends EstadoTarea {

    @Override
    public String getNombre() {
        return "Terminada";
    }

    @Override
    public boolean permiteIrA(EstadoTarea destino) {
        return false;
    }

    //valida que una tarea compuesta no se puede terminar si tiene subtareas pendientes
    @Override
    public void validarEntrada(Tarea tarea) {
        if (!tarea.puedeTerminarse()) {
            throw new TransicionInvalidaException(
                    "No se puede terminar la tarea: tiene subtareas sin terminar");
        }
    }

    //A una tarea ya terminada no se le pueden agregar subtareas
    @Override
    public boolean permiteAgregarSubtareas() {
        return false;
    }
}
