package gestortareas.modelo;

import gestortareas.estado.EnCurso;
import gestortareas.estado.EnRevision;
import gestortareas.estado.Terminada;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

//pruebas del patrón Composite

class TareaCompuestaTest {
    private final LocalDate fecha = LocalDate.of(2026, 12, 1);
    private TareaSimple simple(int id) {
        return new TareaSimple(id, "Tarea " + id, "Ana", Prioridad.MEDIA, fecha);
    }

    private void terminar(Tarea tarea) {
        tarea.cambiarEstado(new EnCurso());
        tarea.cambiarEstado(new EnRevision());
        tarea.cambiarEstado(new Terminada());
    }

    @Test
    void tareaSimpleTieneAvanceCeroSiNoEstaTerminada() {
        assertEquals(0, simple(1).getAvance());
    }

    @Test
    void tareaSimpleTerminadaTieneAvanceCien() {
        TareaSimple tarea = simple(1);
        terminar(tarea);
        assertEquals(100, tarea.getAvance());
    }

    @Test
    void avanceDeCompuestaEsElPromedioDeSusSubtareas() {
        TareaCompuesta proyecto = new TareaCompuesta(1, "Proyecto", "Ana", Prioridad.ALTA, fecha);
        TareaSimple a = simple(2);
        TareaSimple b = simple(3);
        proyecto.agregar(a);
        proyecto.agregar(b);

        terminar(a);

        assertEquals(50, proyecto.getAvance());
    }

    @Test
    void avanceFuncionaConVariosNiveles() {
        TareaCompuesta proyecto = new TareaCompuesta(1, "Proyecto", "Ana", Prioridad.ALTA, fecha);
        TareaCompuesta codigo = new TareaCompuesta(2, "Código", "Ana", Prioridad.ALTA, fecha);
        TareaSimple documento = simple(3);
        TareaSimple modelo = simple(4);
        TareaSimple gui = simple(5);
        codigo.agregar(modelo);
        codigo.agregar(gui);
        proyecto.agregar(codigo);
        proyecto.agregar(documento);

        terminar(documento);
        terminar(modelo);

        // codigo = (100 + 0) / 2 = 50 , proyecto = (50 + 100) / 2 = 75
        assertEquals(50, codigo.getAvance());
        assertEquals(75, proyecto.getAvance());
    }

    @Test
    void compuestaSoloPuedeTerminarseSiTodasSusSubtareasEstanTerminadas() {
        TareaCompuesta proyecto = new TareaCompuesta(1, "Proyecto", "Ana", Prioridad.ALTA, fecha);
        TareaSimple a = simple(2);
        proyecto.agregar(a);

        assertFalse(proyecto.puedeTerminarse());
        terminar(a);
        assertTrue(proyecto.puedeTerminarse());
    }

    @Test
    void noSePuedenAgregarSubtareasAUnaTareaTerminada() {
        TareaCompuesta proyecto = new TareaCompuesta(1, "Proyecto", "Ana", Prioridad.ALTA, fecha);
        terminar(proyecto);

        assertThrows(IllegalStateException.class, () -> proyecto.agregar(simple(2)));
    }
}
