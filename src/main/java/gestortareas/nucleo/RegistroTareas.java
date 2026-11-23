package gestortareas.nucleo;

import gestortareas.modelo.Tarea;
import gestortareas.modelo.TareaCompuesta;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

// Guarda las tareas de primer nivel y reparte los id. Es el receptor del patrón Command.
public class RegistroTareas {

    private final List<Tarea> tareas = new ArrayList<>();
    private int ultimoId = 0;

    // entrega un id nuevo para crear una tarea
    public int siguienteId() {
        ultimoId++;
        return ultimoId;
    }

    public void agregar(Tarea tarea) {
        tareas.add(tarea);
    }

    public void quitar(Tarea tarea) {
        tareas.remove(tarea);
    }

    public List<Tarea> getTareas() {
        return Collections.unmodifiableList(tareas);
    }

    // busca una tarea por id en todo el árbol (también dentro de las compuestas)
    public Tarea buscar(int id) {
        return buscarEn(tareas, id);
    }

    private Tarea buscarEn(List<Tarea> lista, int id) {
        for (Tarea t : lista) {
            if (t.getId() == id) {
                return t;
            }
            if (t instanceof TareaCompuesta) {
                Tarea hallada = buscarEn(((TareaCompuesta) t).getSubtareas(), id);
                if (hallada != null) {
                    return hallada;
                }
            }
        }
        return null;
    }
}
