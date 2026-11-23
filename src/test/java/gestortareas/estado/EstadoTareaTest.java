package gestortareas.estado;

import gestortareas.modelo.Prioridad;
import gestortareas.modelo.TareaCompuesta;
import gestortareas.modelo.TareaSimple;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

//pruebas del patrón State

class EstadoTareaTest {
    private final LocalDate fecha = LocalDate.of(2026, 12, 1);
    private TareaSimple nuevaTarea() {
        return new TareaSimple(1, "Diseñar UML", "Ana", Prioridad.MEDIA, fecha);
    }

    @Test
    void unaTareaNuevaEmpiezaEnPendiente() {
        assertTrue(nuevaTarea().getEstado() instanceof Pendiente);
    }

    @Test
    void recorridoCompletoPermitido() {
        TareaSimple tarea = nuevaTarea();
        tarea.cambiarEstado(new EnCurso());
        tarea.cambiarEstado(new EnRevision());
        tarea.cambiarEstado(new Terminada());
        assertTrue(tarea.getEstado() instanceof Terminada);
    }

    @Test
    void sePuedeVolverDeEnCursoAPendiente() {
        TareaSimple tarea = nuevaTarea();
        tarea.cambiarEstado(new EnCurso());
        tarea.cambiarEstado(new Pendiente());
        assertTrue(tarea.getEstado() instanceof Pendiente);
    }

    @Test
    void sePuedeDevolverDeEnRevisionAEnCurso() {
        TareaSimple tarea = nuevaTarea();
        tarea.cambiarEstado(new EnCurso());
        tarea.cambiarEstado(new EnRevision());
        tarea.cambiarEstado(new EnCurso());
        assertTrue(tarea.getEstado() instanceof EnCurso);
    }

    @Test
    void noSePuedePasarDePendienteATerminada() {
        TareaSimple tarea = nuevaTarea();
        assertThrows(TransicionInvalidaException.class, () -> tarea.cambiarEstado(new Terminada()));
        assertTrue(tarea.getEstado() instanceof Pendiente);
    }

    @Test
    void unaTareaTerminadaNoCambiaDeEstado() {
        TareaSimple tarea = nuevaTarea();
        tarea.cambiarEstado(new EnCurso());
        tarea.cambiarEstado(new EnRevision());
        tarea.cambiarEstado(new Terminada());
        assertThrows(TransicionInvalidaException.class, () -> tarea.cambiarEstado(new EnCurso()));
    }

    @Test
    void noSePuedeTerminarUnaCompuestaConSubtareasPendientes() {
        TareaCompuesta proyecto = new TareaCompuesta(1, "Proyecto", "Ana", Prioridad.ALTA, fecha);
        proyecto.agregar(new TareaSimple(2, "Documento", "Luis", Prioridad.BAJA, fecha));
        proyecto.cambiarEstado(new EnCurso());
        proyecto.cambiarEstado(new EnRevision());

        assertThrows(TransicionInvalidaException.class, () -> proyecto.cambiarEstado(new Terminada()));
        assertTrue(proyecto.getEstado() instanceof EnRevision);
    }

    @Test
    void restaurarEstadoNoValidaLaTransicion() {
        TareaSimple tarea = nuevaTarea();
        tarea.cambiarEstado(new EnCurso());
        tarea.cambiarEstado(new EnRevision());
        tarea.cambiarEstado(new Terminada());

        tarea.restaurarEstado(new EnRevision());   // usado al deshacer

        assertTrue(tarea.getEstado() instanceof EnRevision);
    }
}
