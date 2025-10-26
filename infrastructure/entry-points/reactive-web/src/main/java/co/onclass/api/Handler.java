package co.onclass.api;

import co.onclass.api.dto.ApiSuccessResponse;
import co.onclass.api.dto.tecnologia.CapacidadTecnologiasRequestDto;
import co.onclass.api.dto.tecnologia.TecnologiaRequestDto;
import co.onclass.api.utils.TecnologiaMapper;
import co.onclass.api.validation.ValidationService;
import co.onclass.model.paging.PageableQuery;
import co.onclass.model.paging.SortDirection;
import co.onclass.usecase.capacidadtecnologia.CapacidadTecnologiaUseCase;
import co.onclass.usecase.tecnologia.TecnologiaUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.springframework.web.reactive.function.server.ServerResponse.status;

@Component
@RequiredArgsConstructor
public class Handler {

    private final TecnologiaUseCase tecnologiaUseCase;
    private final CapacidadTecnologiaUseCase capacidadTecnologiaUseCase;
    private final ValidationService validationService;
    private final TecnologiaMapper tecnologiaMapper;

    public Mono<ServerResponse> listenPOSTGuardarTecnologia(ServerRequest serverRequest) {
        return serverRequest.bodyToMono(TecnologiaRequestDto.class)
                .flatMap(validationService::validateObject)
                .map(tecnologiaMapper::toTecnologia)
                .flatMap(tecnologiaUseCase::guardarTecnologia)
                .map(tecnologiaMapper::toTecnologiaResponseDto)
                .flatMap(tecnologiaGuardada ->
                        status(201)
                                .contentType(MediaType.APPLICATION_JSON)
                                .bodyValue(new ApiSuccessResponse<>(tecnologiaGuardada))
                );
    }

    public Mono<ServerResponse> listenPOSTAsignarTecnologia(ServerRequest serverRequest) {
        return serverRequest.bodyToMono(CapacidadTecnologiasRequestDto.class)
                .flatMap(dto ->
                        capacidadTecnologiaUseCase.asignarTecnologiasCapacidas(dto.getIdCapacidad(), dto.getTecnologias())
                )
                .flatMap(tecnologiasAsignadas ->
                        ServerResponse.ok()
                                .contentType(MediaType.APPLICATION_JSON)
                                .bodyValue(tecnologiasAsignadas)
                );
    }

    public Mono<ServerResponse> listenGETCapacidadesOrdenadas(ServerRequest serverRequest) {
        int page = serverRequest.queryParam("page").map(Integer::parseInt).orElse(0);
        int size = serverRequest.queryParam("size").map(Integer::parseInt).orElse(10);
        String order = serverRequest.queryParam("order").orElse("ASC");

        SortDirection direction = "DESC".equalsIgnoreCase(order) ? SortDirection.DESC : SortDirection.ASC;

        PageableQuery query = new PageableQuery(page, size, "count", direction);

        return capacidadTecnologiaUseCase.getCapacidadesPorConteo(query)
                .flatMap(res ->
                        ServerResponse.ok()
                                .contentType(MediaType.APPLICATION_JSON)
                                .bodyValue(res)
                );
    }

    public Mono<ServerResponse> listenGETTecnologiasPorCapacidades(ServerRequest serverRequest) {
        List<Long> capacidadesIds = serverRequest.queryParam("ids")
                .map(idsString -> Arrays.stream(idsString.split(","))
                        .map(Long::parseLong)
                        .toList())
                .orElse(Collections.emptyList());

        return capacidadTecnologiaUseCase.getTecnologiasPorCapacidades(capacidadesIds)
                .flatMap(res ->
                        ServerResponse.ok()
                                .contentType(MediaType.APPLICATION_JSON)
                                .bodyValue(res)
                );
    }
}
