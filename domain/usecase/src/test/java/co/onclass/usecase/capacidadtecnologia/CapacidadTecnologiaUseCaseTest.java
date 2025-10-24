package co.onclass.usecase.capacidadtecnologia;

import co.onclass.exceptions.BusinessException;
import co.onclass.model.capacidadtecnologia.CapacidadTecnologia;
import co.onclass.model.capacidadtecnologia.gateways.CapacidadTecnologiaRepository;
import co.onclass.model.tecnologia.Tecnologia;
import co.onclass.model.tecnologia.gateways.TecnologiaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CapacidadTecnologiaUseCaseTest {

    @InjectMocks
    CapacidadTecnologiaUseCase capacidadTecnologiaUseCase;

    @Mock
    CapacidadTecnologiaRepository capacidadTecnologiaRepository;

    @Mock
    TecnologiaRepository tecnologiaRepository;

    private final Long idCapacidad = 1L;

    private Tecnologia tecnologia;
    private CapacidadTecnologia capacidadTecnologia;

    private final List<Long> tecnologiasIds = List.of(1L, 2L, 3L);

    @BeforeEach
    void initMocks() {
        tecnologia = new Tecnologia();
        tecnologia.setId(1L);
        tecnologia.setNombre("JavaScript");
        tecnologia.setDescripcion("Lenguaje de la web");

        capacidadTecnologia = new CapacidadTecnologia();
        capacidadTecnologia.setId(1L);
        capacidadTecnologia.setIdTecnologia(1L);
        capacidadTecnologia.setIdCapacidad(1L);
    }

    @Test
    void asignarTecnologiasCapacidas() {
        when(tecnologiaRepository.contarTecnologiasExistentes(anyList())).thenReturn(Mono.just(3L));
        when(capacidadTecnologiaRepository.guardarTodas(anyList())).thenReturn(Flux.just(capacidadTecnologia));
        when(tecnologiaRepository.buscarTodasPorId(anyList())).thenReturn(Flux.just(tecnologia));

        Mono<List<Tecnologia>> respuesta = capacidadTecnologiaUseCase.asignarTecnologiasCapacidas(idCapacidad, tecnologiasIds);

        StepVerifier.create(respuesta)
                .assertNext(dto -> {
                    assertNotNull(dto);
                    assertEquals(1, dto.size());
                })
                .verifyComplete();

        verify(tecnologiaRepository, times(1)).contarTecnologiasExistentes(anyList());
        verify(capacidadTecnologiaRepository, times(1)).guardarTodas(anyList());
        verify(tecnologiaRepository, times(1)).buscarTodasPorId(anyList());
    }

    @Test
    void asignarTecnologiasCapacidasRetornaExceptionCuandoNoExisteTecnologia() {
        when(tecnologiaRepository.contarTecnologiasExistentes(anyList())).thenReturn(Mono.just(1L));
        when(tecnologiaRepository.buscarTodasPorId(anyList())).thenReturn(Flux.just(tecnologia));

        Mono<List<Tecnologia>> respuesta = capacidadTecnologiaUseCase.asignarTecnologiasCapacidas(idCapacidad, tecnologiasIds);

        StepVerifier.create(respuesta)
                .expectError(BusinessException.class)
                .verify();

        verify(tecnologiaRepository, times(1)).contarTecnologiasExistentes(anyList());
        verify(capacidadTecnologiaRepository, times(0)).guardarTodas(anyList());
        verify(tecnologiaRepository, times(1)).buscarTodasPorId(anyList());
    }
}
