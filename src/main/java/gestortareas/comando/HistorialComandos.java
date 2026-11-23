package gestortareas.comando;

import java.util.ArrayDeque;
import java.util.Deque;

// Patrón Command: rol Invocador. Guarda dos pilas: lo que se puede deshacer y lo que se puede rehacer.
public class HistorialComandos {

    private final Deque<Comando> pilaDeshacer = new ArrayDeque<>();
    private final Deque<Comando> pilaRehacer = new ArrayDeque<>();

    // ejecuta un comando nuevo. Si falla, no queda en el historial.
    // Una acción nueva borra lo que se había deshecho (RF-9).
    public void ejecutar(Comando comando) {
        comando.ejecutar();
        pilaDeshacer.push(comando);
        pilaRehacer.clear();
    }

    // devuelve la descripción de la acción deshecha
    public String deshacer() {
        if (pilaDeshacer.isEmpty()) {
            throw new IllegalStateException("No hay acciones para deshacer");
        }
        Comando comando = pilaDeshacer.pop();
        comando.deshacer();
        pilaRehacer.push(comando);
        return comando.getDescripcion();
    }

    // devuelve la descripción de la acción rehecha
    public String rehacer() {
        if (pilaRehacer.isEmpty()) {
            throw new IllegalStateException("No hay acciones para rehacer");
        }
        Comando comando = pilaRehacer.pop();
        comando.ejecutar();
        pilaDeshacer.push(comando);
        return comando.getDescripcion();
    }

    public boolean puedeDeshacer() {
        return !pilaDeshacer.isEmpty();
    }

    public boolean puedeRehacer() {
        return !pilaRehacer.isEmpty();
    }
}
