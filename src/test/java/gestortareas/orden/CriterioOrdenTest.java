package gestortareas.orden;

import gestortareas.modelo.Prioridad;
import gestortareas.modelo.Tarea;
import gestortareas.modelo.TareaSimple;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

//pruebas del patron Strategy

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
        List<Tarea> original = new ArrayList<>(tareas);
        new OrdenPorPrioridad().ordenar(original);
        assertEquals(tareas, original);
    }

    //se puede usar cualquier criterio sin saber cual es.
    @Test
    void sePuedeUsarCualquierCriterioSinSaberCual() {
        List<CriterioOrden> criterios = List.of(new OrdenPorPrioridad(), new OrdenPorFecha(), new OrdenPorResponsable());
        for (CriterioOrden criterio : criterios) {
            assertEquals(3, criterio.ordenar(tareas).size());
            assertFalse(criterio.getNombre().isBlank());
        }
    }

    //un criterio nuevo se agrega solo creando una clase.
    @Test
    void unCriterioNuevoSeAgregaSoloCreandoUnaClase() {
        CriterioOrden porTituloInverso = new CriterioOrden() {
            @Override
            public String getNombre() {
                return "Por titulo (Z-A)";
            }

            @Override
            public Comparator<Tarea> comparador() {
                return (x, y) -> y.getTitulo().compareTo(x.getTitulo());
            }
        };
        assertEquals(List.of(c, b, a), porTituloInverso.ordenar(tareas));
    }
}
