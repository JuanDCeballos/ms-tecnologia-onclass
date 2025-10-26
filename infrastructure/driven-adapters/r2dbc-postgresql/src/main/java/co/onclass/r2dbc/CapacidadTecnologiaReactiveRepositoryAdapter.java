package co.onclass.r2dbc;

import co.onclass.model.capacidadtecnologia.CapacidadTecnologia;
import co.onclass.model.capacidadtecnologia.gateways.CapacidadTecnologiaRepository;
import co.onclass.model.paging.PageableQuery;
import co.onclass.model.paging.SortDirection;
import co.onclass.r2dbc.entity.CapacidadTecnologiaEntity;
import co.onclass.r2dbc.helper.ReactiveAdapterOperations;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Repository
public class CapacidadTecnologiaReactiveRepositoryAdapter extends ReactiveAdapterOperations<
        CapacidadTecnologia,
        CapacidadTecnologiaEntity,
        Long,
        CapacidadTecnologiaReactiveRepository
        > implements CapacidadTecnologiaRepository {

    private final DatabaseClient databaseClient;

    public CapacidadTecnologiaReactiveRepositoryAdapter(CapacidadTecnologiaReactiveRepository repository,
                                                        ObjectMapper mapper, DatabaseClient databaseClient) {
        super(repository, mapper, d -> mapper.map(d, CapacidadTecnologia.class));
        this.databaseClient = databaseClient;
    }

    @Override
    public Flux<CapacidadTecnologia> guardarTodas(List<CapacidadTecnologia> relaciones) {
        return saveAllEntities(Flux.fromIterable(relaciones));
    }

    @Override
    public Mono<Long> contarCapacidadesDistintas() {
        String sql = "SELECT COUNT(DISTINCT id_capacidad) FROM capacidad_tecnologias";

        return databaseClient.sql(sql)
                .map(row -> row.get(0, Long.class))
                .one();
    }

    @Override
    public Flux<Long> getCapacidadIdsOrdenadosPorTecnologiaConteo(PageableQuery query) {
        String orderSql = query.getDirection() == SortDirection.DESC ? "DESC" : "ASC";
        long offset = (long) query.getPage() * query.getSize();
        int limit = query.getSize();

        String sql = String.format(
                "SELECT id_capacidad FROM capacidad_tecnologias " +
                        "GROUP BY id_capacidad " +
                        "ORDER BY COUNT(id_tecnologia) %s " +
                        "OFFSET %d LIMIT %d",
                orderSql, offset, limit
        );

        return databaseClient.sql(sql)
                .map(row -> row.get("id_capacidad", Long.class))
                .all();
    }

    @Override
    public Flux<CapacidadTecnologia> buscarTodasPorIdCapacidad(List<Long> capacidadIds) {
        return repository.findAllByIdCapacidadIn(capacidadIds)
                .map(this::toEntity);
    }
}
