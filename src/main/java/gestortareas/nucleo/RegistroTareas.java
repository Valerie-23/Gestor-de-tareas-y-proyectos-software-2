package gestortareas.nucleo;

import gestortareas.modelo.Tarea;
import gestortareas.modelo.TareaCompuesta;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

//guarda las tareas principales
//y entrega un numero de id distinto para cada tarea nueva
//es el receptor del patron Command

public class RegistroTareas {

    private final List<Tarea> raices = new ArrayList<>();
    private int siguienteId = 1;

    //devuelve un id nuevo cada vez que se llama
    public int nuevoId() {
        int id = siguienteId;
        siguienteId++;
        return id;
    }

    public void agregar(Tarea tarea) {
        raices.add(tarea);
    }

    public void quitar(Tarea tarea) {
        raices.remove(tarea);
    }

    //devuelve la lista sin permitir modificarla desde afuera.
    //para agregar o quitar tareas se usan los comandos

    public List<Tarea> getTareas() {
        return Collections.unmodifiableList(raices);
    }

    //busca una tarea por su id en todos los niveles. Devuelve null si no existe.
    public Tarea buscarPorId(int id) {
        return buscarEn(raices, id);
    }

    private Tarea buscarEn(List<Tarea> lista, int id) {
        for (Tarea tarea : lista) {
            if (tarea.getId() == id) {
                return tarea;
            }
            if (tarea instanceof TareaCompuesta) {
                TareaCompuesta compuesta = (TareaCompuesta) tarea;
                Tarea encontrada = buscarEn(compuesta.getSubtareas(), id);
                if (encontrada != null) {
                    return encontrada;
                }
            }
        }
        return null;
    }
}
