package co.onclass.r2dbc;

import co.onclass.r2dbc.entity.TecnologiaEntity;
import org.springframework.data.repository.query.ReactiveQueryByExampleExecutor;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

public interface TecnologiaReactiveRepository extends ReactiveCrudRepository<TecnologiaEntity, Long>,
        ReactiveQueryByExampleExecutor<TecnologiaEntity> {

    Mono<Boolean> existsByNombre(String nombre);
}
