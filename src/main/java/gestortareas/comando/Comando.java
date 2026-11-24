package gestortareas.comando;

//patron Command.
//una accion del usuario que sabe hacerse y deshacerse.

public interface Comando {

    //hace la accion.
    void ejecutar();

    //revierte la accion.
    void deshacer();

    //texto que se le muestra al usuario, por ejemplo "Crear tarea: Diseñar UML".
    String getDescripcion();
}
