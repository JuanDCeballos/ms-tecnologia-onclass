package co.onclass.api.dto.tecnologia;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CapacidadTecnologiasRequestDto {

    private Long idCapacidad;
    private List<Long> tecnologias;
}
