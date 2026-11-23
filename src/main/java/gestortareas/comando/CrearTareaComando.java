package gestortareas.comando;

import gestortareas.modelo.Tarea;
import gestortareas.modelo.TareaCompuesta;
import gestortareas.nucleo.RegistroTareas;

// Patrón Command: comando concreto. Crea una tarea (de primer nivel o como subtarea) y sabe quitarla.
public class CrearTareaComando implements Comando {

    private final RegistroTareas registro;
    private final Tarea tarea;
    private final TareaCompuesta padre; // null si es una tarea de primer nivel

    public CrearTareaComando(RegistroTareas registro, Tarea tarea, TareaCompuesta padre) {
        this.registro = registro;
        this.tarea = tarea;
        this.padre = padre;
    }

    @Override
    public void ejecutar() {
        if (padre == null) {
            registro.agregar(tarea);
        } else {
            padre.agregar(tarea);
        }
    }

    @Override
    public void deshacer() {
        if (padre == null) {
            registro.quitar(tarea);
        } else {
            padre.quitar(tarea);
        }
    }

    @Override
    public String getDescripcion() {
        return "Crear tarea: " + tarea.getTitulo();
    }
}
