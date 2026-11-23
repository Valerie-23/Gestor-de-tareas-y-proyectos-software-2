package gestortareas.modelo;

import gestortareas.estado.EstadoTarea;
import gestortareas.estado.Pendiente;
import gestortareas.estado.TransicionInvalidaException;

import java.time.LocalDate;

//patron Composite.
//patron State.

//clase base de todas las tareas. Las tareas simples y las compuestas
//heredan de esta clase, por eso el resto del sistema las trata por igual.
public abstract class Tarea {

    private final int id;
    private final String titulo;
    private final String responsable;
    private final Prioridad prioridad;
    private final LocalDate fechaLimite;
    private EstadoTarea estado;

    public Tarea(int id, String titulo, String responsable, Prioridad prioridad, LocalDate fechaLimite) {
        this.id = id;
        this.titulo = titulo;
        this.responsable = responsable;
        this.prioridad = prioridad;
        this.fechaLimite = fechaLimite;
        this.estado = new Pendiente();   // toda tarea empieza en Pendiente
    }

    //porcentaje de avance de la tarea, de 0 a 100.
    public abstract double getAvance();

    //indica si la tarea se puede pasar a Terminada
    public abstract boolean puedeTerminarse();

    //cambia el estado de la tarea.
    //1)primero le pregunta al estado actual si el cambio está permitido y luego deja que el nuevo estado revise sus propias reglas.

    public void cambiarEstado(EstadoTarea destino) {
        if (!estado.permiteIrA(destino)) {
            throw new TransicionInvalidaException(
                    "No se puede pasar de " + estado.getNombre() + " a " + destino.getNombre());
        }
        destino.validarEntrada(this);
        estado = destino;
    }

    //vuelve a poner un estado anterior (Ctrl z).
    //solo se usa al deshacer un cambio de estado.

    public void restaurarEstado(EstadoTarea anterior) {
        estado = anterior;
    }

    public int getId() {
        return id;
    }

    public String getTitulo() {
        return titulo;
    }

    public String getResponsable() {
        return responsable;
    }

    public Prioridad getPrioridad() {
        return prioridad;
    }

    public LocalDate getFechaLimite() {
        return fechaLimite;
    }

    public EstadoTarea getEstado() {
        return estado;
    }

    @Override
    public String toString() {
        return "[" + id + "] " + titulo
                + " | " + estado.getNombre()
                + " | " + prioridad
                + " | " + responsable
                + " | " + fechaLimite
                + " | " + Math.round(getAvance()) + "%";
    }
}
