package co.onclass.model.capacidadtecnologia.gateways;

import co.onclass.model.capacidadtecnologia.CapacidadTecnologia;
import reactor.core.publisher.Flux;

import java.util.List;

public interface CapacidadTecnologiaRepository {
    Flux<CapacidadTecnologia> guardarTodas(List<CapacidadTecnologia> relaciones);
}
