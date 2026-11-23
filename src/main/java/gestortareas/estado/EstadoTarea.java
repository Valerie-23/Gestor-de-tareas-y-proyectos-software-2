package gestortareas.estado;

import gestortareas.modelo.Tarea;

//patron State.
//cada estado sabe a que otros estados se puede pasar desde el.

public abstract class EstadoTarea {

    //nombre que se muestra al usuario
    public abstract String getNombre();

    //Indica si desde este estado se puede pasar al estado destino.
    public abstract boolean permiteIrA(EstadoTarea destino);


    //reglas que se verifican antes de que una tarea entre en estado
    public void validarEntrada(Tarea tarea) {
    }

    //indica si a esta tarea en este estado se le pueden agregar subtarea
    public boolean permiteAgregarSubtareas() {
        return true;
    }

    @Override
    public String toString() {
        return getNombre();
    }
}
