package gestortareas.estado;

//patron State
//desde Pendiente solo se puede pasar a En curso.

public class Pendiente extends EstadoTarea {

    @Override
    public String getNombre() {
        return "Pendiente";
    }

    @Override
    public boolean permiteIrA(EstadoTarea destino) {
        return destino instanceof EnCurso;
    }
}
