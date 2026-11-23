package gestortareas.comando;

import java.util.ArrayDeque;
import java.util.Deque;

// Patrón Command: rol Invocador. Guarda dos pilas: lo que se puede deshacer y lo que se puede rehacer.
public class HistorialComandos {

    private final Deque<Comando> paraDeshacer = new ArrayDeque<>();
    private final Deque<Comando> paraRehacer = new ArrayDeque<>();

    // ejecuta un comando nuevo. Si falla, no queda en el historial.
    // Una acción nueva borra lo que se había deshecho (RF-9).
    public void ejecutar(Comando comando) {
        comando.ejecutar();
        paraDeshacer.push(comando);
        paraRehacer.clear();
    }

    // devuelve la descripción de la acción deshecha
    public String deshacer() {
        if (paraDeshacer.isEmpty()) {
            throw new IllegalStateException("No hay acciones para deshacer");
        }
        Comando comando = paraDeshacer.pop();
        comando.deshacer();
        paraRehacer.push(comando);
        return comando.getDescripcion();
    }

    // devuelve la descripción de la acción rehecha
    public String rehacer() {
        if (paraRehacer.isEmpty()) {
            throw new IllegalStateException("No hay acciones para rehacer");
        }
        Comando comando = paraRehacer.pop();
        comando.ejecutar();
        paraDeshacer.push(comando);
        return comando.getDescripcion();
    }

    public boolean puedeDeshacer() {
        return !paraDeshacer.isEmpty();
    }

    public boolean puedeRehacer() {
        return !paraRehacer.isEmpty();
    }
}
