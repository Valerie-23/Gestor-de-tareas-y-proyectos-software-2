package gestortareas.nucleo;

import gestortareas.estado.EnCurso;
import gestortareas.estado.EnRevision;
import gestortareas.estado.Pendiente;
import gestortareas.estado.Terminada;
import gestortareas.estado.TransicionInvalidaException;
import gestortareas.modelo.Prioridad;
import gestortareas.modelo.Tarea;
import gestortareas.modelo.TareaCompuesta;
import gestortareas.orden.OrdenPorPrioridad;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

//pruebas del patron Facade para este se usa el sistema completo solo a traves de GestorTareas

class GestorTareasTest {

    private final LocalDate fecha = LocalDate.of(2026, 12, 1);
    private final GestorTareas gestor = new GestorTareas();

    @Test
    void creaTareasConSubtareasEnVariosNiveles() {
        Tarea proyecto = gestor.crearTareaCompuesta("Entrega final", "Ana", Prioridad.ALTA, fecha, null);
        Tarea codigo = gestor.crearTareaCompuesta("Codigo", "Luis", Prioridad.ALTA, fecha, proyecto.getId());
        gestor.crearTareaSimple("Modelo", "Luis", Prioridad.MEDIA, fecha, codigo.getId());
        gestor.crearTareaSimple("Documento", "Ana", Prioridad.BAJA, fecha, proyecto.getId());

        assertEquals(1, gestor.getTareas().size());
        assertEquals(2, ((TareaCompuesta) proyecto).getSubtareas().size());
        assertEquals(1, ((TareaCompuesta) codigo).getSubtareas().size());
    }

    @Test
    void cadaTareaRecibeUnIdDistinto() {
        Tarea a = gestor.crearTareaSimple("A", "Ana", Prioridad.ALTA, fecha, null);
        Tarea b = gestor.crearTareaSimple("B", "Ana", Prioridad.ALTA, fecha, null);
        assertNotEquals(a.getId(), b.getId());
    }

    @Test
    void noSePuedenAgregarSubtareasAUnaTareaSimple() {
        Tarea simple = gestor.crearTareaSimple("Simple", "Ana", Prioridad.ALTA, fecha, null);
        assertThrows(IllegalArgumentException.class,
                () -> gestor.crearTareaSimple("Hija", "Ana", Prioridad.ALTA, fecha, simple.getId()));
    }

    @Test
    void noSeCreaUnaTareaSinTituloOSinFecha() {
        assertThrows(IllegalArgumentException.class,
                () -> gestor.crearTareaSimple(" ", "Ana", Prioridad.ALTA, fecha, null));
        assertThrows(IllegalArgumentException.class,
                () -> gestor.crearTareaSimple("Tarea", "Ana", Prioridad.ALTA, null, null));
        assertTrue(gestor.getTareas().isEmpty());
    }

    //crear, cambiar estado y deshacer dos veces.
    @Test
    void ejemploDelDocumentoCrearCambiarYDeshacerDosVeces() {
        Tarea tarea = gestor.crearTareaSimple("Diseñar UML", "Ana", Prioridad.ALTA, fecha, null);
        gestor.cambiarEstado(tarea.getId(), new EnCurso());

        gestor.deshacer();
        assertTrue(tarea.getEstado() instanceof Pendiente);

        String descripcion = gestor.deshacer();
        assertEquals("Crear tarea: Diseñar UML", descripcion);
        assertTrue(gestor.getTareas().isEmpty());

        gestor.rehacer();
        assertEquals(1, gestor.getTareas().size());
    }

    @Test
    void unaTransicionInvalidaNoSeGuardaEnElHistorial() {
        Tarea tarea = gestor.crearTareaSimple("Diseñar UML", "Ana", Prioridad.ALTA, fecha, null);
        assertThrows(TransicionInvalidaException.class,
                () -> gestor.cambiarEstado(tarea.getId(), new Terminada()));

        gestor.deshacer();   // deshace la creacion, no el cambio que fallo
        assertTrue(gestor.getTareas().isEmpty());
    }

    @Test
    void noSeTerminaUnaCompuestaConSubtareasPendientes() {
        Tarea proyecto = gestor.crearTareaCompuesta("Proyecto", "Ana", Prioridad.ALTA, fecha, null);
        gestor.crearTareaSimple("Documento", "Ana", Prioridad.ALTA, fecha, proyecto.getId());
        gestor.cambiarEstado(proyecto.getId(), new EnCurso());
        gestor.cambiarEstado(proyecto.getId(), new EnRevision());

        assertThrows(TransicionInvalidaException.class,
                () -> gestor.cambiarEstado(proyecto.getId(), new Terminada()));
    }

    @Test
    void cambiarEstadoDeUnaTareaQueNoExisteLanzaError() {
        assertThrows(IllegalArgumentException.class, () -> gestor.cambiarEstado(99, new EnCurso()));
    }

    @Test
    void listarOrdenadoDevuelveLasTareasEnElOrdenDelCriterio() {
        Tarea baja = gestor.crearTareaSimple("Baja", "Ana", Prioridad.BAJA, fecha, null);
        Tarea alta = gestor.crearTareaSimple("Alta", "Ana", Prioridad.ALTA, fecha, null);

        List<Tarea> ordenadas = gestor.listarOrdenado(new OrdenPorPrioridad());

        assertSame(alta, ordenadas.get(0));
        assertSame(baja, ordenadas.get(1));
        assertSame(baja, gestor.getTareas().get(0));
    }

    @Test
    void ofreceLosCriteriosYEstadosDisponibles() {
        assertEquals(3, gestor.getCriteriosDisponibles().size());
        assertEquals(4, gestor.getEstadosDisponibles().size());
    }
}
