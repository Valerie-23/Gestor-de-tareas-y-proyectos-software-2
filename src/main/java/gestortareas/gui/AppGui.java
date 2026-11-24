package gestortareas.gui;

import gestortareas.nucleo.GestorTareas;

import javax.swing.SwingUtilities;

//punto de inicio de la version con interfaz grafica.

public class AppGui {

    public static void main(String[] args) {
        GestorTareas gestor = new GestorTareas();
        SwingUtilities.invokeLater(() -> {
            VentanaPrincipal ventana = new VentanaPrincipal(gestor);
            ventana.setVisible(true);
        });
    }
}
