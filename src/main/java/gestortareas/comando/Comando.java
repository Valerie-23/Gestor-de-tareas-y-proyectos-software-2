package gestortareas.comando;

// Patrón Command: rol Comando. Una acción del usuario que se puede hacer y deshacer.
public interface Comando {

    void ejecutar();

    void deshacer();

    // texto que se muestra al usuario, por ejemplo "Crear tarea: Diseñar UML"
    String getDescripcion();
}
