package gestortareas.consola;

import gestortareas.nucleo.GestorTareas;

//punto de inicio de la version por consola.

public class AppConsola {
    public static void main(String[] args) {
        GestorTareas gestor = new GestorTareas();
        MenuConsola menu = new MenuConsola(gestor);
        menu.iniciar();
    }
}
