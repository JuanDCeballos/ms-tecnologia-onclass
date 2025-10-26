package co.onclass.r2dbc;

import co.onclass.r2dbc.entity.CapacidadTecnologiaEntity;
import org.springframework.data.repository.query.ReactiveQueryByExampleExecutor;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;

import java.util.List;

public interface CapacidadTecnologiaReactiveRepository extends ReactiveCrudRepository<CapacidadTecnologiaEntity, Long>,
        ReactiveQueryByExampleExecutor<CapacidadTecnologiaEntity> {

    Flux<CapacidadTecnologiaEntity> findAllByIdCapacidadIn(List<Long> ids);
}
