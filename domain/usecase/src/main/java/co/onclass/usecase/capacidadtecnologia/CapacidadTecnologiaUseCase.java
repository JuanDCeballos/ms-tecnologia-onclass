package co.onclass.usecase.capacidadtecnologia;

import co.onclass.enums.ExceptionMessages;
import co.onclass.exceptions.BusinessException;
import co.onclass.model.capacidadtecnologia.CapacidadTecnologia;
import co.onclass.model.capacidadtecnologia.gateways.CapacidadTecnologiaRepository;
import co.onclass.model.tecnologia.Tecnologia;
import co.onclass.model.tecnologia.gateways.TecnologiaRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

import java.util.List;

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
}
