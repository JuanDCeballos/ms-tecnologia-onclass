package co.onclass.usecase.capacidadtecnologia;

import co.onclass.exceptions.BusinessException;
import co.onclass.model.capacidadtecnologia.CapacidadTecnologia;
import co.onclass.model.capacidadtecnologia.gateways.CapacidadTecnologiaRepository;
import co.onclass.model.paging.PageableQuery;
import co.onclass.model.paging.PaginatedCapacidadIdsResponse;
import co.onclass.model.paging.SortDirection;
import co.onclass.model.tecnologia.Tecnologia;
import co.onclass.model.tecnologia.gateways.TecnologiaRepository;
import org.junit.jupiter.api.Assertions;
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
import java.util.Map;

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
    private PageableQuery pageableQuery;

    private final List<Long> tecnologiasIds = List.of(1L, 2L, 3L);
    private final List<Long> capacidadesIds = List.of(1L, 2L, 3L);

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

        pageableQuery = new PageableQuery();
        pageableQuery.setPage(0);
        pageableQuery.setSize(3);
        pageableQuery.setSortBy("technologyCount");
        pageableQuery.setDirection(SortDirection.ASC);
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

    @Test
    void getCapacidadesConteo() {
        when(capacidadTecnologiaRepository.contarCapacidadesDistintas()).thenReturn(Mono.just(1L));
        when(capacidadTecnologiaRepository
                .getCapacidadIdsOrdenadosPorTecnologiaConteo(any(PageableQuery.class))).thenReturn(Flux.just(1L));

        Mono<PaginatedCapacidadIdsResponse> response = capacidadTecnologiaUseCase.getCapacidadesPorConteo(pageableQuery);

        StepVerifier.create(response)
                .assertNext(dto -> {
                    assertNotNull(dto);
                    assertEquals(1L, dto.getTotalElementos());
                    assertEquals(1, dto.getIds().size());
                })
                .verifyComplete();

        verify(capacidadTecnologiaRepository, times(1)).contarCapacidadesDistintas();
        verify(capacidadTecnologiaRepository, times(1))
                .getCapacidadIdsOrdenadosPorTecnologiaConteo(any(PageableQuery.class));
    }

    @Test
    void getTecnologiasPorCapacidades() {
        when(capacidadTecnologiaRepository
                .buscarTodasPorIdCapacidad(anyList())).thenReturn(Flux.just(capacidadTecnologia));
        when(tecnologiaRepository.buscarTodasPorId(anySet())).thenReturn(Flux.just(tecnologia));

        Mono<Map<Long, List<Tecnologia>>> respuesta =
                capacidadTecnologiaUseCase.getTecnologiasPorCapacidades(capacidadesIds);

        StepVerifier.create(respuesta)
                .assertNext(dto -> {
                    assertNotNull(dto);
                    assertEquals(1L, dto.get(1L).getFirst().getId());
                    assertEquals("JavaScript", dto.get(1L).getFirst().getNombre());
                })
                .verifyComplete();

        verify(capacidadTecnologiaRepository, times(1)).buscarTodasPorIdCapacidad(anyList());
        verify(tecnologiaRepository, times(1)).buscarTodasPorId(anySet());
    }

    @Test
    void getTecnologiasPorCapacidadesRetornaVacio() {
        when(capacidadTecnologiaRepository.buscarTodasPorIdCapacidad(anyList())).thenReturn(Flux.empty());

        Mono<Map<Long, List<Tecnologia>>> respuesta =
                capacidadTecnologiaUseCase.getTecnologiasPorCapacidades(capacidadesIds);

        StepVerifier.create(respuesta)
                .assertNext(Assertions::assertNotNull)
                .verifyComplete();

        verify(capacidadTecnologiaRepository, times(1)).buscarTodasPorIdCapacidad(anyList());
        verify(tecnologiaRepository, times(0)).buscarTodasPorId(anySet());
    }
}
