package co.onclass.model.capacidadtecnologia.gateways;

import co.onclass.model.capacidadtecnologia.CapacidadTecnologia;
import co.onclass.model.paging.PageableQuery;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

public interface CapacidadTecnologiaRepository {

    Flux<CapacidadTecnologia> guardarTodas(List<CapacidadTecnologia> relaciones);

    Mono<Long> contarCapacidadesDistintas();

    Flux<Long> getCapacidadIdsOrdenadosPorTecnologiaConteo(PageableQuery query);

    Flux<CapacidadTecnologia> buscarTodasPorIdCapacidad(List<Long> capacidadIds);
}
