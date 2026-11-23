package gestortareas.estado;

//patron State.
//desde En curso se puede volver a Pendiente o pasar a En revision.

public class EnCurso extends EstadoTarea {

    @Override
    public String getNombre() {
        return "En curso";
    }

    @Override
    public boolean permiteIrA(EstadoTarea destino) {
        return destino instanceof Pendiente || destino instanceof EnRevision;
    }
}
