package co.onclass.api.dto.tecnologia;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TecnologiaRequestDto {

    @NotBlank(message = "El nombre de la tecnología es obligatorio.")
    @Size(max = 50, message = "El nombre de la tecnología no puede ser superior a 50 caracteres.")
    private String nombre;

    @NotBlank(message = "La descripción de la tecnología es obligatoria.")
    @Size(max = 90, message = "La descripción de la tecnología no puede ser superior a 90 caracteres.")
    private String descripcion;
}
