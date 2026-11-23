package gestortareas.comando;

import gestortareas.estado.EstadoTarea;
import gestortareas.modelo.Tarea;

// Patrón Command: comando concreto. Cambia el estado de una tarea y guarda el anterior para volver a él.
public class CambiarEstadoComando implements Comando {

    private final Tarea tarea;
    private final EstadoTarea destino;
    private EstadoTarea anterior;

    public CambiarEstadoComando(Tarea tarea, EstadoTarea destino) {
        this.tarea = tarea;
        this.destino = destino;
    }

    @Override
    public void ejecutar() {
        EstadoTarea antes = tarea.getEstado();
        tarea.cambiarEstado(destino); // si no está permitido lanza TransicionInvalidaException
        anterior = antes;             // solo se guarda si el cambio se hizo
    }

    @Override
    public void deshacer() {
        tarea.restaurarEstado(anterior);
    }

    @Override
    public String getDescripcion() {
        return "Cambiar estado de \"" + tarea.getTitulo() + "\" a " + destino.getNombre();
    }
}
