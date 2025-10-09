package co.onclass.api;

import co.onclass.api.dto.ApiSuccessResponse;
import co.onclass.api.dto.tecnologia.TecnologiaRequestDto;
import co.onclass.api.utils.TecnologiaMapper;
import co.onclass.api.validation.ValidationService;
import co.onclass.usecase.tecnologia.TecnologiaUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import static org.springframework.web.reactive.function.server.ServerResponse.status;

@Component
@RequiredArgsConstructor
public class Handler {

    private final TecnologiaUseCase tecnologiaUseCase;
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
}
