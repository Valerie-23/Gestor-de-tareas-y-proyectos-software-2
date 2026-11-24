package gestortareas.comando;

import gestortareas.modelo.Tarea;
import gestortareas.modelo.TareaCompuesta;
import gestortareas.nucleo.RegistroTareas;

//patron Command.
//crea una tarea. Si no tiene padre se agrega como tarea principal,
//si tiene padre se agrega como subtarea de esa tarea compuesta.

public class CrearTareaComando implements Comando {

    private final RegistroTareas registro;
    private final Tarea tarea;
    private final TareaCompuesta padre;   // vacio si es una tarea principal

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

    //para deshacer se quita la tarea del lugar donde se agrego.
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
