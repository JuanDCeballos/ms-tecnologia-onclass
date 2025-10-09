package co.onclass.api.utils;

import co.onclass.api.dto.tecnologia.TecnologiaRequestDto;
import co.onclass.api.dto.tecnologia.TecnologiaResponseDto;
import co.onclass.model.tecnologia.Tecnologia;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TecnologiaMapper {

    Tecnologia toTecnologia(TecnologiaRequestDto tecnologiaRequestDto);

    TecnologiaResponseDto toTecnologiaResponseDto(Tecnologia tecnologia);
}
