package co.onclass.model.tecnologia.gateways;

import co.onclass.model.tecnologia.Tecnologia;
import reactor.core.publisher.Mono;

public interface TecnologiaRepository {

    Mono<Tecnologia> guardarTecnologia(Tecnologia tecnologia);

    Mono<Boolean> existePorNombre(String nombre);
}
