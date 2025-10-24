package co.onclass.model.capacidadtecnologia;

import lombok.Builder;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class CapacidadTecnologia {

    private Long id;
    private Long idTecnologia;
    private Long idCapacidad;
}
