package co.onclass.r2dbc.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Table("capacidad_tecnologias")
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class CapacidadTecnologiaEntity {

    @Id
    private Long id;

    @Column("id_tecnologia")
    private Long idTecnologia;

    @Column("id_capacidad")
    private Long idCapacidad;
}
