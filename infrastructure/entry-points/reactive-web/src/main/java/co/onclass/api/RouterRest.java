package co.onclass.api;

import co.onclass.api.dto.ApiErrorResponse;
import co.onclass.api.dto.ApiSuccessResponse;
import co.onclass.api.dto.tecnologia.CapacidadTecnologiasRequestDto;
import co.onclass.api.dto.tecnologia.TecnologiaRequestDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springdoc.core.annotations.RouterOperation;
import org.springdoc.core.annotations.RouterOperations;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static co.onclass.api.constants.ApiConstants.*;
import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class RouterRest {

    @Bean
    @RouterOperations({
            @RouterOperation(
                    path = GUARDAR_TECNOLOGIA,
                    method = RequestMethod.POST,
                    beanClass = Handler.class,
                    beanMethod = "listenPOSTGuardarTecnologia",
                    operation = @Operation(
                            operationId = "guardarTecnologia",
                            summary = "Guarda una nueva tecnologia",
                            requestBody = @RequestBody(
                                    required = true,
                                    description = "Datos de la nueva tecnología a crear",
                                    content = @Content(schema = @Schema(implementation = TecnologiaRequestDto.class))
                            ),
                            responses = {
                                    @ApiResponse(
                                            responseCode = "200",
                                            description = "Tecnología guardada exitosamente",
                                            content = @Content(schema = @Schema(implementation = ApiSuccessResponse.class))
                                    ),
                                    @ApiResponse(
                                            responseCode = "400",
                                            description = "Datos de entrada inválidos",
                                            content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))
                                    )
                            }
                    )
            ),
            @RouterOperation(
                    path = ASIGNAR_TECNOLOGIA,
                    method = RequestMethod.POST,
                    beanClass = Handler.class,
                    beanMethod = "listenPOSTAsignarTecnologia",
                    operation = @Operation(
                            operationId = "asignarTecnologiaCapacidad",
                            summary = "Asigna tecnologias a una capacidad",
                            requestBody = @RequestBody(
                                    required = true,
                                    description = "Datos con la capacidad y las tecnologías a asignar",
                                    content = @Content(schema = @Schema(implementation = CapacidadTecnologiasRequestDto.class))
                            ),
                            responses = {
                                    @ApiResponse(
                                            responseCode = "200",
                                            description = "Tecnologías asignadas a la capacidad correctamente",
                                            content = @Content(schema = @Schema(implementation = ApiSuccessResponse.class))
                                    ),
                                    @ApiResponse(
                                            responseCode = "400",
                                            description = "Datos inválidos para la asignación",
                                            content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))
                                    )
                            }

                    )
            )
    })
    public RouterFunction<ServerResponse> routerFunction(Handler handler) {
        return route(POST(GUARDAR_TECNOLOGIA), handler::listenPOSTGuardarTecnologia)
                .andRoute(POST(ASIGNAR_TECNOLOGIA), handler::listenPOSTAsignarTecnologia)
                .andRoute(GET(OBTENER_CAPACIDADES_ORDENADAS), handler::listenGETCapacidadesOrdenadas)
                .andRoute(GET(OBTENER_TECNOLOGIAS_POR_CAPACIDADES), handler::listenGETTecnologiasPorCapacidades);
    }
}
