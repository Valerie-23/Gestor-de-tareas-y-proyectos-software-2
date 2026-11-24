package gestortareas.gui;

import gestortareas.estado.EstadoTarea;
import gestortareas.modelo.Prioridad;
import gestortareas.modelo.Tarea;
import gestortareas.modelo.TareaCompuesta;
import gestortareas.nucleo.GestorTareas;
import gestortareas.orden.CriterioOrden;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;

//interfaz grafica en Swing: una sola ventana con el arbol de tareas y botones.
//solo usa GestorTareas, igual que la consola.

public class VentanaPrincipal extends JFrame {

    private final GestorTareas gestor;
    private final JTree arbol = new JTree();
    private final JComboBox<String> comboOrden = new JComboBox<>();
    private final JButton botonDeshacer = new JButton("Deshacer");
    private final JButton botonRehacer = new JButton("Rehacer");
    private final JLabel mensaje = new JLabel(" ");

    public VentanaPrincipal(GestorTareas gestor) {
        this.gestor = gestor;
        setTitle("Gestor de Tareas y Proyectos");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 550);
        setLocationRelativeTo(null);

        //botones de arriba
        JButton botonSimple = new JButton("Nueva tarea simple");
        JButton botonCompuesta = new JButton("Nueva tarea compuesta");
        JButton botonEstado = new JButton("Cambiar estado");
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelBotones.add(botonSimple);
        panelBotones.add(botonCompuesta);
        panelBotones.add(botonEstado);
        panelBotones.add(botonDeshacer);
        panelBotones.add(botonRehacer);
        panelBotones.add(new JLabel("   Ordenar:"));
        panelBotones.add(comboOrden);

        //el combo se arma con los criterios del gestor, asi un criterio nuevo aparece solo.
        comboOrden.addItem("Orden en que se crearon");
        for (CriterioOrden criterio : gestor.getCriteriosDisponibles()) {
            comboOrden.addItem(criterio.getNombre());
        }

        arbol.setRootVisible(false);
        arbol.setShowsRootHandles(true);

        add(panelBotones, BorderLayout.NORTH);
        add(new JScrollPane(arbol), BorderLayout.CENTER);
        add(mensaje, BorderLayout.SOUTH);

        //acciones de los botones
        botonSimple.addActionListener(e -> crearTarea(false));
        botonCompuesta.addActionListener(e -> crearTarea(true));
        botonEstado.addActionListener(e -> cambiarEstado());
        botonDeshacer.addActionListener(e -> deshacer());
        botonRehacer.addActionListener(e -> rehacer());
        comboOrden.addActionListener(e -> refrescarArbol());

        refrescarArbol();
    }

    //vuelve a dibujar el arbol con los datos actuales.
    private void refrescarArbol() {
        DefaultMutableTreeNode raiz = new DefaultMutableTreeNode("Tareas");
        for (Tarea tarea : ordenar(gestor.getTareas())) {
            raiz.add(crearNodo(tarea));
        }
        arbol.setModel(new DefaultTreeModel(raiz));
        for (int i = 0; i < arbol.getRowCount(); i++) {
            arbol.expandRow(i);
        }
        botonDeshacer.setEnabled(gestor.puedeDeshacer());
        botonRehacer.setEnabled(gestor.puedeRehacer());
    }

    //cada nodo guarda la tarea, el arbol muestra su toString().
    private DefaultMutableTreeNode crearNodo(Tarea tarea) {
        DefaultMutableTreeNode nodo = new DefaultMutableTreeNode(tarea);
        if (tarea instanceof TareaCompuesta) {
            TareaCompuesta compuesta = (TareaCompuesta) tarea;
            for (Tarea subtarea : ordenar(compuesta.getSubtareas())) {
                nodo.add(crearNodo(subtarea));
            }
        }
        return nodo;
    }

    //ordena con el criterio elegido en el combo. La opcion 0 deja el orden en que se crearon.
    private List<Tarea> ordenar(List<Tarea> tareas) {
        int indice = comboOrden.getSelectedIndex();
        if (indice <= 0) {
            return tareas;
        }
        CriterioOrden criterio = gestor.getCriteriosDisponibles().get(indice - 1);
        return criterio.ordenar(tareas);
    }

    //devuelve la tarea seleccionada en el arbol, o null si no hay ninguna.
    private Tarea tareaSeleccionada() {
        Object seleccion = arbol.getLastSelectedPathComponent();
        if (seleccion == null) {
            return null;
        }
        Object dato = ((DefaultMutableTreeNode) seleccion).getUserObject();
        if (dato instanceof Tarea) {
            return (Tarea) dato;
        }
        return null;
    }

    private void crearTarea(boolean compuesta) {
        JTextField campoTitulo = new JTextField();
        JTextField campoResponsable = new JTextField();
        JComboBox<Prioridad> campoPrioridad = new JComboBox<>(Prioridad.values());
        JTextField campoFecha = new JTextField(LocalDate.now().plusDays(7).toString());

        //si hay una tarea compuesta seleccionada, se puede crear como subtarea.
        Tarea seleccionada = tareaSeleccionada();
        JCheckBox comoSubtarea = new JCheckBox("Agregar como subtarea de la tarea seleccionada");
        if (seleccionada instanceof TareaCompuesta) {
            comoSubtarea.setText("Agregar como subtarea de: " + seleccionada.getTitulo());
            comoSubtarea.setSelected(true);
        } else {
            comoSubtarea.setEnabled(false);
        }

        JPanel formulario = new JPanel(new GridLayout(0, 1));
        formulario.add(new JLabel("Titulo:"));
        formulario.add(campoTitulo);
        formulario.add(new JLabel("Responsable:"));
        formulario.add(campoResponsable);
        formulario.add(new JLabel("Prioridad:"));
        formulario.add(campoPrioridad);
        formulario.add(new JLabel("Fecha limite (AAAA-MM-DD):"));
        formulario.add(campoFecha);
        formulario.add(comoSubtarea);

        String titulo = compuesta ? "Nueva tarea compuesta" : "Nueva tarea simple";
        int respuesta = JOptionPane.showConfirmDialog(this, formulario, titulo,
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (respuesta != JOptionPane.OK_OPTION) {
            return;
        }

        try {
            LocalDate fecha = LocalDate.parse(campoFecha.getText().trim());
            Prioridad prioridad = (Prioridad) campoPrioridad.getSelectedItem();
            Integer idPadre = null;
            if (comoSubtarea.isSelected()) {
                idPadre = seleccionada.getId();
            }
            Tarea tarea;
            if (compuesta) {
                tarea = gestor.crearTareaCompuesta(campoTitulo.getText(), campoResponsable.getText(), prioridad, fecha, idPadre);
            } else {
                tarea = gestor.crearTareaSimple(campoTitulo.getText(), campoResponsable.getText(), prioridad, fecha, idPadre);
            }
            mensaje.setText("Tarea creada: " + tarea.getTitulo());
        } catch (DateTimeParseException e) {
            mostrarError("Fecha no valida. Ejemplo: 2026-12-01");
        } catch (RuntimeException e) {
            mostrarError(e.getMessage());
        }
        refrescarArbol();
    }

    private void cambiarEstado() {
        Tarea tarea = tareaSeleccionada();
        if (tarea == null) {
            mostrarError("Seleccione una tarea en el arbol.");
            return;
        }
        //el EstadoTarea se muestra con su nombre porque su toString() devuelve getNombre().
        Object[] opciones = gestor.getEstadosDisponibles().toArray();
        Object elegido = JOptionPane.showInputDialog(this,
                "Estado actual: " + tarea.getEstado().getNombre() + "\nNuevo estado:",
                "Cambiar estado", JOptionPane.PLAIN_MESSAGE, null, opciones, opciones[0]);
        if (elegido == null) {
            return;
        }
        try {
            gestor.cambiarEstado(tarea.getId(), (EstadoTarea) elegido);
            mensaje.setText("Estado de \"" + tarea.getTitulo() + "\" cambiado a " + tarea.getEstado().getNombre());
        } catch (RuntimeException e) {
            mostrarError(e.getMessage());
        }
        refrescarArbol();
    }

    private void deshacer() {
        try {
            mensaje.setText("Se deshizo: " + gestor.deshacer());
        } catch (RuntimeException e) {
            mostrarError(e.getMessage());
        }
        refrescarArbol();
    }

    private void rehacer() {
        try {
            mensaje.setText("Se rehizo: " + gestor.rehacer());
        } catch (RuntimeException e) {
            mostrarError(e.getMessage());
        }
        refrescarArbol();
    }

    private void mostrarError(String texto) {
        JOptionPane.showMessageDialog(this, texto, "Error", JOptionPane.ERROR_MESSAGE);
    }
}
