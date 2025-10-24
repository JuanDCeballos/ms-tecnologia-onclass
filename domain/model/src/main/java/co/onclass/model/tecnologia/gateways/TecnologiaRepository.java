package co.onclass.model.tecnologia.gateways;

import co.onclass.model.tecnologia.Tecnologia;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Collection;

public interface TecnologiaRepository {

    Mono<Tecnologia> guardarTecnologia(Tecnologia tecnologia);

    Mono<Boolean> existePorNombre(String nombre);

    Mono<Long> contarTecnologiasExistentes(Collection<Long> ids);

    Flux<Tecnologia> buscarTodasPorId(Collection<Long> ids);
}
