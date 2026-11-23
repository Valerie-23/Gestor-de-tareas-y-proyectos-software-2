package gestortareas.estado;

//patron State.
//desde En revision se puede volver a En curso o pasar a Terminada.

public class EnRevision extends EstadoTarea {

    @Override
    public String getNombre() {
        return "En revisión";
    }

    @Override
    public boolean permiteIrA(EstadoTarea destino) {
        return destino instanceof EnCurso || destino instanceof Terminada;
    }
}
