package gestortareas.consola;

import gestortareas.estado.EstadoTarea;
import gestortareas.modelo.Prioridad;
import gestortareas.modelo.Tarea;
import gestortareas.modelo.TareaCompuesta;
import gestortareas.nucleo.GestorTareas;
import gestortareas.orden.CriterioOrden;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Scanner;

//interfaz por consola con un menu numerado.
//solo usa GestorTareas.

public class MenuConsola {

    private final GestorTareas gestor;
    private final Scanner scanner = new Scanner(System.in);
    private CriterioOrden criterioActual = null;   // null = orden en que se crearon

    public MenuConsola(GestorTareas gestor) {
        this.gestor = gestor;
    }

    public void iniciar() {
        System.out.println("=== GESTOR DE TAREAS Y PROYECTOS ===");
        int opcion = -1;
        while (opcion != 0) {
            mostrarMenu();
            opcion = leerEntero("Elija una opcion: ");
            //los errores se muestran como mensaje y el menu sigue funcionando.
            try {
                switch (opcion) {
                    case 1:
                        mostrarTareas();
                        break;
                    case 2:
                        crearTarea(false);
                        break;
                    case 3:
                        crearTarea(true);
                        break;
                    case 4:
                        cambiarEstado();
                        break;
                    case 5:
                        System.out.println("Se deshizo: " + gestor.deshacer());
                        break;
                    case 6:
                        System.out.println("Se rehizo: " + gestor.rehacer());
                        break;
                    case 7:
                        elegirOrden();
                        break;
                    case 0:
                        System.out.println("Hasta luego.");
                        break;
                    default:
                        System.out.println("Opcion no valida.");
                }
            } catch (RuntimeException e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }

    private void mostrarMenu() {
        System.out.println();
        System.out.println("1. Ver tareas");
        System.out.println("2. Crear tarea simple");
        System.out.println("3. Crear tarea compuesta");
        System.out.println("4. Cambiar estado de una tarea");
        System.out.println("5. Deshacer");
        System.out.println("6. Rehacer");
        System.out.println("7. Ordenar tareas");
        System.out.println("0. Salir");
    }

    //muestra el arbol de tareas. Las compuestas se marcan con + y las simples con -.
    private void mostrarTareas() {
        List<Tarea> tareas = ordenar(gestor.getTareas());
        if (tareas.isEmpty()) {
            System.out.println("No hay tareas.");
            return;
        }
        System.out.println("[id] titulo | estado | prioridad | responsable | fecha limite | avance");
        for (Tarea tarea : tareas) {
            mostrarTarea(tarea, 0);
        }
    }

    private void mostrarTarea(Tarea tarea, int nivel) {
        String sangria = "   ".repeat(nivel);
        if (tarea instanceof TareaCompuesta) {
            System.out.println(sangria + "+ " + tarea);
            TareaCompuesta compuesta = (TareaCompuesta) tarea;
            for (Tarea subtarea : ordenar(compuesta.getSubtareas())) {
                mostrarTarea(subtarea, nivel + 1);
            }
        } else {
            System.out.println(sangria + "- " + tarea);
        }
    }

    //ordena con el criterio elegido. Si no hay criterio, deja el orden en que se crearon.
    private List<Tarea> ordenar(List<Tarea> tareas) {
        if (criterioActual == null) {
            return tareas;
        }
        return criterioActual.ordenar(tareas);
    }

    private void crearTarea(boolean compuesta) {
        String titulo = leerTexto("Titulo: ");
        String responsable = leerTexto("Responsable: ");
        Prioridad prioridad = leerPrioridad();
        LocalDate fecha = leerFecha();
        Integer idPadre = leerIdPadre();

        Tarea tarea;
        if (compuesta) {
            tarea = gestor.crearTareaCompuesta(titulo, responsable, prioridad, fecha, idPadre);
        } else {
            tarea = gestor.crearTareaSimple(titulo, responsable, prioridad, fecha, idPadre);
        }
        System.out.println("Tarea creada con id " + tarea.getId() + ".");
    }

    private void cambiarEstado() {
        int id = leerEntero("Id de la tarea: ");
        List<EstadoTarea> estados = gestor.getEstadosDisponibles();
        for (int i = 0; i < estados.size(); i++) {
            System.out.println((i + 1) + ". " + estados.get(i).getNombre());
        }
        int numero = leerEntero("Nuevo estado: ");
        if (numero < 1 || numero > estados.size()) {
            System.out.println("Estado no valido.");
            return;
        }
        gestor.cambiarEstado(id, estados.get(numero - 1));
        System.out.println("Estado cambiado.");
    }

    private void elegirOrden() {
        List<CriterioOrden> criterios = gestor.getCriteriosDisponibles();
        System.out.println("0. Orden en que se crearon");
        for (int i = 0; i < criterios.size(); i++) {
            System.out.println((i + 1) + ". " + criterios.get(i).getNombre());
        }
        int numero = leerEntero("Criterio: ");
        if (numero == 0) {
            criterioActual = null;
        } else if (numero >= 1 && numero <= criterios.size()) {
            criterioActual = criterios.get(numero - 1);
        } else {
            System.out.println("Criterio no valido.");
            return;
        }
        mostrarTareas();
    }

    //metodos para leer datos del usuario

    private String leerTexto(String mensaje) {
        System.out.print(mensaje);
        return scanner.nextLine().trim();
    }

    private int leerEntero(String mensaje) {
        while (true) {
            String texto = leerTexto(mensaje);
            try {
                return Integer.parseInt(texto);
            } catch (NumberFormatException e) {
                System.out.println("Debe escribir un numero.");
            }
        }
    }

    private Prioridad leerPrioridad() {
        while (true) {
            int numero = leerEntero("Prioridad (1 = alta, 2 = media, 3 = baja): ");
            if (numero == 1) {
                return Prioridad.ALTA;
            }
            if (numero == 2) {
                return Prioridad.MEDIA;
            }
            if (numero == 3) {
                return Prioridad.BAJA;
            }
            System.out.println("Prioridad no valida.");
        }
    }

    private LocalDate leerFecha() {
        while (true) {
            String texto = leerTexto("Fecha limite (AAAA-MM-DD): ");
            try {
                return LocalDate.parse(texto);
            } catch (DateTimeParseException e) {
                System.out.println("Fecha no valida. Ejemplo: 2026-12-01");
            }
        }
    }

    //si el usuario deja el campo vacio, la tarea queda como tarea principal.
    private Integer leerIdPadre() {
        while (true) {
            String texto = leerTexto("Id de la tarea compuesta padre (Enter si no tiene): ");
            if (texto.isEmpty()) {
                return null;
            }
            try {
                return Integer.parseInt(texto);
            } catch (NumberFormatException e) {
                System.out.println("Debe escribir un numero o dejarlo vacio.");
            }
        }
    }
}
