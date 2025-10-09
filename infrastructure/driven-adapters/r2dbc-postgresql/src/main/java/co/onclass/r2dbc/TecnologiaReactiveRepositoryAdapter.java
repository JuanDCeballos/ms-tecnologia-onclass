package co.onclass.r2dbc;

import co.onclass.model.tecnologia.Tecnologia;
import co.onclass.model.tecnologia.gateways.TecnologiaRepository;
import co.onclass.r2dbc.entity.TecnologiaEntity;
import co.onclass.r2dbc.helper.ReactiveAdapterOperations;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public class TecnologiaReactiveRepositoryAdapter extends ReactiveAdapterOperations<
        Tecnologia,
        TecnologiaEntity,
        Long,
        TecnologiaReactiveRepository
        > implements TecnologiaRepository {
    public TecnologiaReactiveRepositoryAdapter(TecnologiaReactiveRepository repository, ObjectMapper mapper) {
        super(repository, mapper, d -> mapper.map(d, Tecnologia.class));
    }

    @Override
    public Mono<Tecnologia> guardarTecnologia(Tecnologia tecnologia) {
        return save(tecnologia);
    }

    @Override
    public Mono<Boolean> existePorNombre(String nombre) {
        return repository.existsByNombre(nombre);
    }
}
