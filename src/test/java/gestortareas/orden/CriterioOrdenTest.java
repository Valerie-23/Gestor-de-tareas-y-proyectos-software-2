package gestortareas.orden;

import static org.junit.jupiter.api.Assertions.*;

import gestortareas.modelo.Prioridad;
import gestortareas.modelo.Tarea;
import gestortareas.modelo.TareaSimple;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.Test;

// Pruebas del patrón Strategy
class CriterioOrdenTest {

    private final Tarea a = new TareaSimple(1, "A", "Valerie", Prioridad.BAJA, LocalDate.of(2026, 12, 20));
    private final Tarea b = new TareaSimple(2, "B", "carol", Prioridad.ALTA, LocalDate.of(2026, 12, 5));
    private final Tarea c = new TareaSimple(3, "C", "Juan", Prioridad.MEDIA, null);
    private final List<Tarea> tareas = List.of(a, b, c);

    @Test
    void ordenaPorPrioridadDeAltaABaja() {
        assertEquals(List.of(b, c, a), new OrdenPorPrioridad().ordenar(tareas));
    }

    @Test
    void ordenaPorFechaConLasSinFechaAlFinal() {
        assertEquals(List.of(b, a, c), new OrdenPorFecha().ordenar(tareas));
    }

    @Test
    void ordenaPorResponsableSinDistinguirMayusculas() {
        assertEquals(List.of(b, c, a), new OrdenPorResponsable().ordenar(tareas));
    }

    @Test
    void noModificaLaListaOriginal() {
        List<Tarea> original = new java.util.ArrayList<>(tareas);
        new OrdenPorPrioridad().ordenar(original);
        assertEquals(tareas, original);
    }

    @Test
    void sePuedeUsarCualquierCriterioSinSaberCual() {
        for (CriterioOrden criterio : List.of(new OrdenPorPrioridad(), new OrdenPorFecha(), new OrdenPorResponsable())) {
            assertEquals(3, criterio.ordenar(tareas).size());
            assertFalse(criterio.getNombre().isBlank());
        }
    }

    @Test
    void unCriterioNuevoSeAgregaSoloCreandoUnaClase() { // Abierto/cerrado
        CriterioOrden porTitulo = new CriterioOrden() {
            public String getNombre() { return "Por título"; }
            public java.util.Comparator<Tarea> comparador() {
                return java.util.Comparator.comparing(Tarea::getTitulo).reversed();
            }
        };
        assertEquals(List.of(c, b, a), porTitulo.ordenar(tareas));
    }
}
