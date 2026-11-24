package gestortareas.comando;

import gestortareas.estado.EstadoTarea;
import gestortareas.modelo.Tarea;

//patron Command.
//cambia el estado de una tarea y guarda el estado anterior para poder deshacer.

public class CambiarEstadoComando implements Comando {

    private final Tarea tarea;
    private final EstadoTarea destino;
    private EstadoTarea anterior;

    public CambiarEstadoComando(Tarea tarea, EstadoTarea destino) {
        this.tarea = tarea;
        this.destino = destino;
    }

    //hace el cambio. Si no esta permitido, la tarea lanza "TransicionInvalidaException".
    //el estado anterior solo se guarda si el cambio se hizo.
    @Override
    public void ejecutar() {
        EstadoTarea antes = tarea.getEstado();
        tarea.cambiarEstado(destino);
        anterior = antes;
    }

    //vuelve al estado anterior sin validar la transicion.
    @Override
    public void deshacer() {
        tarea.restaurarEstado(anterior);
    }

    @Override
    public String getDescripcion() {
        return "Cambiar estado de \"" + tarea.getTitulo() + "\" a " + destino.getNombre();
    }
}
