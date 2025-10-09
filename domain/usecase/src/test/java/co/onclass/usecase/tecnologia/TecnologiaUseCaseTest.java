package co.onclass.usecase.tecnologia;

import co.onclass.model.exceptions.TecnologiaYaExisteException;
import co.onclass.model.tecnologia.Tecnologia;
import co.onclass.model.tecnologia.gateways.TecnologiaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TecnologiaUseCaseTest {

    @InjectMocks
    TecnologiaUseCase tecnologiaUseCase;

    @Mock
    TecnologiaRepository tecnologiaRepository;

    private Tecnologia tecnologia;

    @BeforeEach
    void initMocks() {
        tecnologia = new Tecnologia();
        tecnologia.setId(1L);
        tecnologia.setNombre("JavaScript");
        tecnologia.setDescripcion("Lenguaje de la web");
    }

    @Test
    void guardarTecnologia() {
        when(tecnologiaRepository.existePorNombre(anyString())).thenReturn(Mono.just(false));
        when(tecnologiaRepository.guardarTecnologia(any(Tecnologia.class))).thenReturn(Mono.just(tecnologia));

        Mono<Tecnologia> respuesta = tecnologiaUseCase.guardarTecnologia(tecnologia);

        StepVerifier.create(respuesta)
                .expectNextMatches(val -> val.equals(tecnologia))
                .verifyComplete();

        verify(tecnologiaRepository, times(1)).existePorNombre(anyString());
        verify(tecnologiaRepository, times(1)).guardarTecnologia(any(Tecnologia.class));
    }

    @Test
    void guardarTecnologiaRetornaErrorCuandoTecnologiaYaExistePorNombre() {
        when(tecnologiaRepository.existePorNombre(anyString())).thenReturn(Mono.just(true));

        Mono<Tecnologia> respuesta = tecnologiaUseCase.guardarTecnologia(tecnologia);

        StepVerifier.create(respuesta)
                .expectError(TecnologiaYaExisteException.class)
                .verify();

        verify(tecnologiaRepository, times(1)).existePorNombre(anyString());
        verify(tecnologiaRepository, times(0)).guardarTecnologia(any(Tecnologia.class));
    }
}
