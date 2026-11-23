package gestortareas.estado;

//error que aparece cuando se intenta un cambio de estado no permitido

public class TransicionInvalidaException extends RuntimeException {

    public TransicionInvalidaException(String mensaje) {
        super(mensaje);
    }
}
