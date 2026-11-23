package gestortareas.comando;

import static org.junit.jupiter.api.Assertions.*;

import gestortareas.estado.EnCurso;
import gestortareas.estado.Pendiente;
import gestortareas.estado.Terminada;
import gestortareas.estado.TransicionInvalidaException;
import gestortareas.modelo.Prioridad;
import gestortareas.modelo.TareaCompuesta;
import gestortareas.modelo.TareaSimple;
import gestortareas.nucleo.RegistroTareas;
import java.time.LocalDate;
import org.junit.jupiter.api.Test;

// Pruebas del patrón Command
class HistorialComandosTest {

    private final LocalDate fecha = LocalDate.of(2026, 12, 1);
    private final RegistroTareas registro = new RegistroTareas();
    private final HistorialComandos historial = new HistorialComandos();

    private TareaSimple nuevaTarea(String titulo) {
        return new TareaSimple(registro.nuevoId(), titulo, "Carol", Prioridad.MEDIA, fecha);
    }

    @Test
    void crearTareaLaAgregaYDeshacerLaQuita() {
        TareaSimple t = nuevaTarea("Diseñar UML");
        historial.ejecutar(new CrearTareaComando(registro, t, null));
        assertEquals(1, registro.getTareas().size());
        historial.deshacer();
        assertTrue(registro.getTareas().isEmpty());
    }

    @Test
    void rehacerVuelveAAgregarLaTarea() {
        TareaSimple t = nuevaTarea("Diseñar UML");
        historial.ejecutar(new CrearTareaComando(registro, t, null));
        historial.deshacer();
        historial.rehacer();
        assertSame(t, registro.getTareas().get(0));
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

    @Test
    void ejemploDelDocumentoCrearCambiarDeshacerDeshacerRehacer() {
        TareaSimple t = nuevaTarea("Diseñar UML");
        historial.ejecutar(new CrearTareaComando(registro, t, null));
        historial.ejecutar(new CambiarEstadoComando(t, new EnCurso()));
        assertTrue(t.getEstado() instanceof EnCurso);

        historial.deshacer(); // vuelve a Pendiente
        assertTrue(t.getEstado() instanceof Pendiente);
        assertEquals(1, registro.getTareas().size());

        historial.deshacer(); // desaparece
        assertTrue(registro.getTareas().isEmpty());

        historial.rehacer();  // vuelve a aparecer
        assertEquals(1, registro.getTareas().size());
    }

    @Test
    void unaAccionNuevaBorraLoQueSePodiaRehacer() { // RF-9
        TareaSimple t = nuevaTarea("A");
        historial.ejecutar(new CrearTareaComando(registro, t, null));
        historial.deshacer();
        assertTrue(historial.puedeRehacer());
        historial.ejecutar(new CrearTareaComando(registro, nuevaTarea("B"), null));
        assertFalse(historial.puedeRehacer());
        assertThrows(IllegalStateException.class, historial::rehacer);
    }

    @Test
    void cambioInvalidoLanzaExcepcionYNoQuedaEnElHistorial() {
        TareaSimple t = nuevaTarea("A");
        assertThrows(TransicionInvalidaException.class,
                () -> historial.ejecutar(new CambiarEstadoComando(t, new Terminada())));
        assertFalse(historial.puedeDeshacer());
        assertTrue(t.getEstado() instanceof Pendiente);
    }

    @Test
    void deshacerSinAccionesLanzaExcepcion() {
        assertFalse(historial.puedeDeshacer());
        assertThrows(IllegalStateException.class, historial::deshacer);
    }

    @Test
    void descripcionesLegiblesParaElUsuario() {
        TareaSimple t = nuevaTarea("Diseñar UML");
        Comando c = new CambiarEstadoComando(t, new EnCurso());
        assertTrue(c.getDescripcion().contains("Diseñar UML"));
        assertTrue(c.getDescripcion().contains("En curso"));
    }
}
