package co.onclass.usecase.capacidadtecnologia;

import co.onclass.enums.ExceptionMessages;
import co.onclass.exceptions.BusinessException;
import co.onclass.model.capacidadtecnologia.CapacidadTecnologia;
import co.onclass.model.capacidadtecnologia.gateways.CapacidadTecnologiaRepository;
import co.onclass.model.paging.PageableQuery;
import co.onclass.model.paging.PaginatedCapacidadIdsResponse;
import co.onclass.model.tecnologia.Tecnologia;
import co.onclass.model.tecnologia.gateways.TecnologiaRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

import java.util.*;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class CapacidadTecnologiaUseCase {

    private final CapacidadTecnologiaRepository capacidadTecnologiaRepository;
    private final TecnologiaRepository tecnologiaRepository;

    public Mono<List<Tecnologia>> asignarTecnologiasCapacidas(Long idCapacidad, List<Long> tecnologias) {
        Mono<Void> validacionTecnologias = tecnologiaRepository.contarTecnologiasExistentes(tecnologias)
                .flatMap(count -> {
                    if (count != tecnologias.size()) {
                        return Mono.error(new BusinessException(ExceptionMessages.TECNOLOGIA_NO_ENCONTRADA));
                    }
                    return Mono.empty();
                });

        Mono<Void> guardar = Mono.defer(() -> {
            List<CapacidadTecnologia> relaciones = tecnologias.stream()
                    .map(capaTecno -> new CapacidadTecnologia(null, capaTecno, idCapacidad))
                    .toList();

            return capacidadTecnologiaRepository.guardarTodas(relaciones)
                    .then();
        });

        return validacionTecnologias
                .then(guardar)
                .then(tecnologiaRepository.buscarTodasPorId(tecnologias).collectList());
    }

    public Mono<PaginatedCapacidadIdsResponse> getCapacidadesPorConteo(PageableQuery query) {
        Mono<Long> totalMono = capacidadTecnologiaRepository.contarCapacidadesDistintas()
                .defaultIfEmpty(0L);

        Mono<List<Long>> idsMono = capacidadTecnologiaRepository.getCapacidadIdsOrdenadosPorTecnologiaConteo(query)
                .collectList();

        return Mono.zip(totalMono, idsMono)
                .map(tuple -> new PaginatedCapacidadIdsResponse(tuple.getT1(), tuple.getT2()));
    }

    public Mono<Map<Long, List<Tecnologia>>> getTecnologiasPorCapacidades(List<Long> capacidadesIds) {
        return capacidadTecnologiaRepository.buscarTodasPorIdCapacidad(capacidadesIds)
                .collectList()
                .flatMap(relations -> {
                    Set<Long> tecnologiasIds = relations.stream()
                            .map(CapacidadTecnologia::getIdTecnologia)
                            .collect(Collectors.toSet());

                    if (tecnologiasIds.isEmpty()) {
                        return Mono.just(Collections.emptyMap());
                    }

                    return tecnologiaRepository.buscarTodasPorId(tecnologiasIds)
                            .collectMap(Tecnologia::getId)
                            .map(tecnologiaMap ->
                                    agruparTecnologiasPorCapacidad(relations, tecnologiaMap)
                            );
                });
    }

    private Map<Long, List<Tecnologia>> agruparTecnologiasPorCapacidad(
            List<CapacidadTecnologia> relations, Map<Long, Tecnologia> tecnologiaMap) {
        Map<Long, List<CapacidadTecnologia>> relationsByCapacidad = relations.stream()
                .collect(Collectors.groupingBy(CapacidadTecnologia::getIdCapacidad));

        return relationsByCapacidad.entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        entry -> entry.getValue().stream()
                                .map(relation -> tecnologiaMap.get(relation.getIdTecnologia()))
                                .filter(Objects::nonNull)
                                .toList()
                ));
    }
}
