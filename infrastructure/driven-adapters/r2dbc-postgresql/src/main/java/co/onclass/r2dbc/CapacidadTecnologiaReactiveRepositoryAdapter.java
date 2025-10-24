package co.onclass.r2dbc;

import co.onclass.model.capacidadtecnologia.CapacidadTecnologia;
import co.onclass.model.capacidadtecnologia.gateways.CapacidadTecnologiaRepository;
import co.onclass.r2dbc.entity.CapacidadTecnologiaEntity;
import co.onclass.r2dbc.helper.ReactiveAdapterOperations;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

import java.util.List;

@Repository
public class CapacidadTecnologiaReactiveRepositoryAdapter extends ReactiveAdapterOperations<
        CapacidadTecnologia,
        CapacidadTecnologiaEntity,
        Long,
        CapacidadTecnologiaReactiveRepository
        > implements CapacidadTecnologiaRepository {
    public CapacidadTecnologiaReactiveRepositoryAdapter(CapacidadTecnologiaReactiveRepository repository, ObjectMapper mapper) {
        super(repository, mapper, d -> mapper.map(d, CapacidadTecnologia.class));
    }

    @Override
    public Flux<CapacidadTecnologia> guardarTodas(List<CapacidadTecnologia> relaciones) {
        return saveAllEntities(Flux.fromIterable(relaciones));
    }
}
