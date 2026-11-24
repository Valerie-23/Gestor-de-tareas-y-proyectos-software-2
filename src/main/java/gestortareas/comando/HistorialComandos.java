package gestortareas.comando;

import java.util.ArrayDeque;
import java.util.Deque;

//patron Command.
//guarda los comandos en dos pilas: una para deshacer y otra para rehacer.

public class HistorialComandos {

    private final Deque<Comando> pilaDeshacer = new ArrayDeque<>();
    private final Deque<Comando> pilaRehacer = new ArrayDeque<>();

    //ejecuta un comando nuevo y lo guarda para poder deshacerlo.
    //si el comando falla, lanza la excepcion y no se guarda.
    //al hacer una accion nueva, la pila de rehacer se vacia.
    public void ejecutar(Comando comando) {
        comando.ejecutar();
        pilaDeshacer.push(comando);
        pilaRehacer.clear();
    }

    //deshace la ultima accion, la pasa a la pila de rehacer y devuelve su descripcion.
    public String deshacer() {
        if (pilaDeshacer.isEmpty()) {
            throw new IllegalStateException("No hay acciones para deshacer");
        }
        Comando comando = pilaDeshacer.pop();
        comando.deshacer();
        pilaRehacer.push(comando);
        return comando.getDescripcion();
    }

    //vuelve a hacer la ultima accion que se deshizo y devuelve su descripcion.
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
