package pe.edu.vallegrande.supplier.repository;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import pe.edu.vallegrande.supplier.model.Ubigeo;
import reactor.core.publisher.Flux;

public interface UbigeoRepository extends ReactiveCrudRepository<Ubigeo, Long> {
    Flux<Ubigeo> findAllByOrderByIdAsc();
}
