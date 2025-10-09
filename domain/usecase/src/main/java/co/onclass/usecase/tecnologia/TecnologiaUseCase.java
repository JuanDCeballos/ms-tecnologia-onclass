package co.onclass.usecase.tecnologia;

import co.onclass.model.exceptions.TecnologiaYaExisteException;
import co.onclass.model.tecnologia.Tecnologia;
import co.onclass.model.tecnologia.gateways.TecnologiaRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class TecnologiaUseCase {

    private final TecnologiaRepository tecnologiaRepository;

    public Mono<Tecnologia> guardarTecnologia(Tecnologia tecnologia) {
        return tecnologiaRepository.existePorNombre(tecnologia.getNombre())
                .flatMap(existe -> {
                    if (Boolean.TRUE.equals(existe)) {
                        return Mono.error(new TecnologiaYaExisteException(tecnologia.getNombre()));
                    }

                    return tecnologiaRepository.guardarTecnologia(tecnologia);
                });
    }
}
