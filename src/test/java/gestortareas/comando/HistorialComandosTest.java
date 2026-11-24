package gestortareas.comando;

import gestortareas.estado.EnCurso;
import gestortareas.estado.Pendiente;
import gestortareas.estado.Terminada;
import gestortareas.estado.TransicionInvalidaException;
import gestortareas.modelo.Prioridad;
import gestortareas.modelo.TareaCompuesta;
import gestortareas.modelo.TareaSimple;
import gestortareas.nucleo.RegistroTareas;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

//pruebas del patron Command

class HistorialComandosTest {

    private final LocalDate fecha = LocalDate.of(2026, 12, 1);
    private final RegistroTareas registro = new RegistroTareas();
    private final HistorialComandos historial = new HistorialComandos();

    private TareaSimple nuevaTarea(String titulo) {
        return new TareaSimple(registro.nuevoId(), titulo, "Carol", Prioridad.MEDIA, fecha);
    }

    @Test
    void crearTareaLaAgregaYDeshacerLaQuita() {
        TareaSimple tarea = nuevaTarea("Diseñar UML");
        historial.ejecutar(new CrearTareaComando(registro, tarea, null));
        assertEquals(1, registro.getTareas().size());

        historial.deshacer();
        assertTrue(registro.getTareas().isEmpty());
    }

    @Test
    void rehacerVuelveAAgregarLaTarea() {
        TareaSimple tarea = nuevaTarea("Diseñar UML");
        historial.ejecutar(new CrearTareaComando(registro, tarea, null));
        historial.deshacer();
        historial.rehacer();
        assertSame(tarea, registro.getTareas().get(0));
    }

    @Test
    void crearSubtareaSeAgregaAlPadreYSeDeshace() {
        TareaCompuesta padre = new TareaCompuesta(registro.nuevoId(), "Código", "Juan", Prioridad.ALTA, fecha);
        TareaSimple hija = nuevaTarea("Modelo");
        historial.ejecutar(new CrearTareaComando(registro, hija, padre));
        assertEquals(1, padre.getSubtareas().size());

        historial.deshacer();
        assertTrue(padre.getSubtareas().isEmpty());
    }

    //crear, cambiar estado, deshacer dos veces y rehacer.
    @Test
    void ejemploDelDocumentoCrearCambiarDeshacerDeshacerRehacer() {
        TareaSimple tarea = nuevaTarea("Diseñar UML");
        historial.ejecutar(new CrearTareaComando(registro, tarea, null));
        historial.ejecutar(new CambiarEstadoComando(tarea, new EnCurso()));
        assertTrue(tarea.getEstado() instanceof EnCurso);

        historial.deshacer();   // vuelve a Pendiente
        assertTrue(tarea.getEstado() instanceof Pendiente);
        assertEquals(1, registro.getTareas().size());

        historial.deshacer();   // la tarea desaparece
        assertTrue(registro.getTareas().isEmpty());

        historial.rehacer();    // la tarea vuelve a aparecer
        assertEquals(1, registro.getTareas().size());
    }

    //una accion nueva borra lo que se podia rehacer.
    @Test
    void unaAccionNuevaBorraLoQueSePodiaRehacer() {
        TareaSimple tarea = nuevaTarea("A");
        historial.ejecutar(new CrearTareaComando(registro, tarea, null));
        historial.deshacer();
        assertTrue(historial.puedeRehacer());

        historial.ejecutar(new CrearTareaComando(registro, nuevaTarea("B"), null));

        assertFalse(historial.puedeRehacer());
        assertThrows(IllegalStateException.class, () -> historial.rehacer());
    }

    @Test
    void cambioInvalidoLanzaExcepcionYNoQuedaEnElHistorial() {
        TareaSimple tarea = nuevaTarea("A");
        assertThrows(TransicionInvalidaException.class,
                () -> historial.ejecutar(new CambiarEstadoComando(tarea, new Terminada())));
        assertFalse(historial.puedeDeshacer());
        assertTrue(tarea.getEstado() instanceof Pendiente);
    }

    @Test
    void deshacerSinAccionesLanzaExcepcion() {
        assertFalse(historial.puedeDeshacer());
        assertThrows(IllegalStateException.class, () -> historial.deshacer());
    }

    @Test
    void deshacerDevuelveLaDescripcionDeLaAccion() {
        TareaSimple tarea = nuevaTarea("Diseñar UML");
        historial.ejecutar(new CambiarEstadoComando(tarea, new EnCurso()));

        String descripcion = historial.deshacer();

        assertTrue(descripcion.contains("Diseñar UML"));
        assertTrue(descripcion.contains("En curso"));
    }
}
