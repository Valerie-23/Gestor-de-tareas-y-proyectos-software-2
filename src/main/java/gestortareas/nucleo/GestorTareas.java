package gestortareas.nucleo;

import gestortareas.comando.CambiarEstadoComando;
import gestortareas.comando.CrearTareaComando;
import gestortareas.comando.HistorialComandos;
import gestortareas.estado.EnCurso;
import gestortareas.estado.EnRevision;
import gestortareas.estado.EstadoTarea;
import gestortareas.estado.Pendiente;
import gestortareas.estado.Terminada;
import gestortareas.modelo.Prioridad;
import gestortareas.modelo.Tarea;
import gestortareas.modelo.TareaCompuesta;
import gestortareas.modelo.TareaSimple;
import gestortareas.orden.CriterioOrden;
import gestortareas.orden.OrdenPorFecha;
import gestortareas.orden.OrdenPorPrioridad;
import gestortareas.orden.OrdenPorResponsable;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

//patron Facade.
//es la unica entrada al nucleo. La consola y la interfaz grafica usan esta clase.
//no tiene reglas propias, solo coordina las tareas, los estados, los comandos y los criterios.

public class GestorTareas {

    private final RegistroTareas registro = new RegistroTareas();
    private final HistorialComandos historial = new HistorialComandos();
    private final List<CriterioOrden> criterios = new ArrayList<>();
    private final List<EstadoTarea> estados = new ArrayList<>();

    public GestorTareas() {
        //para agregar un criterio nuevo solo hay que crear la clase y digitarla aqui.
        criterios.add(new OrdenPorPrioridad());
        criterios.add(new OrdenPorFecha());
        criterios.add(new OrdenPorResponsable());

        //estados que el usuario puede elegir al cambiar el estado de una tarea.
        estados.add(new Pendiente());
        estados.add(new EnCurso());
        estados.add(new EnRevision());
        estados.add(new Terminada());
    }

    //crea una tarea simple. Si idPadre es null queda como tarea principal,
    //si no, se agrega como subtarea de la tarea compuesta con ese mismo id.

    public Tarea crearTareaSimple(String titulo, String responsable, Prioridad prioridad,
                                  LocalDate fechaLimite, Integer idPadre) {
        validarDatos(titulo, responsable, prioridad, fechaLimite);
        TareaCompuesta padre = buscarPadre(idPadre);
        Tarea tarea = new TareaSimple(registro.nuevoId(), titulo.trim(), responsable.trim(), prioridad, fechaLimite);
        historial.ejecutar(new CrearTareaComando(registro, tarea, padre));
        return tarea;
    }

    //crea una tarea compuesta.
    public Tarea crearTareaCompuesta(String titulo, String responsable, Prioridad prioridad,
                                     LocalDate fechaLimite, Integer idPadre) {
        validarDatos(titulo, responsable, prioridad, fechaLimite);
        TareaCompuesta padre = buscarPadre(idPadre);
        Tarea tarea = new TareaCompuesta(registro.nuevoId(), titulo.trim(), responsable.trim(), prioridad, fechaLimite);
        historial.ejecutar(new CrearTareaComando(registro, tarea, padre));
        return tarea;
    }

    //cambia el estado de una tarea usando un comando para poder deshacerlo.
    public void cambiarEstado(int idTarea, EstadoTarea destino) {
        Tarea tarea = buscarTarea(idTarea);
        historial.ejecutar(new CambiarEstadoComando(tarea, destino));
    }

    //deshace la ultima accion y devuelve su descripcion.
    public String deshacer() {
        return historial.deshacer();
    }

    //rehace la ultima accion deshecha y devuelve su descripcion.
    public String rehacer() {
        return historial.rehacer();
    }

    public boolean puedeDeshacer() {
        return historial.puedeDeshacer();
    }

    public boolean puedeRehacer() {
        return historial.puedeRehacer();
    }

    //devuelve las tareas principales ordenadas con el criterio elegido.
    //las subtareas se ordenan con el mismo criterio al mostrarlas.

    public List<Tarea> listarOrdenado(CriterioOrden criterio) {
        return criterio.ordenar(registro.getTareas());
    }

    //tareas principales en el orden en que se crearon.
    public List<Tarea> getTareas() {
        return registro.getTareas();
    }

    public List<EstadoTarea> getEstadosDisponibles() {
        return estados;
    }

    public List<CriterioOrden> getCriteriosDisponibles() {
        return criterios;
    }

    //metodos de apoyo

    private Tarea buscarTarea(int id) {
        Tarea tarea = registro.buscarPorId(id);
        if (tarea == null) {
            throw new IllegalArgumentException("No existe una tarea con id " + id);
        }
        return tarea;
    }

    private TareaCompuesta buscarPadre(Integer idPadre) {
        if (idPadre == null) {
            return null;
        }
        Tarea tarea = buscarTarea(idPadre);
        if (!(tarea instanceof TareaCompuesta)) {
            throw new IllegalArgumentException("La tarea " + idPadre + " no es compuesta, no puede tener subtareas");
        }
        return (TareaCompuesta) tarea;
    }

    private void validarDatos(String titulo, String responsable, Prioridad prioridad, LocalDate fechaLimite) {
        if (titulo == null || titulo.isBlank()) {
            throw new IllegalArgumentException("El titulo no puede estar vacio");
        }
        if (responsable == null || responsable.isBlank()) {
            throw new IllegalArgumentException("El responsable no puede estar vacio");
        }
        if (prioridad == null) {
            throw new IllegalArgumentException("Debe elegir una prioridad");
        }
        if (fechaLimite == null) {
            throw new IllegalArgumentException("Debe indicar una fecha limite");
        }
    }
}
