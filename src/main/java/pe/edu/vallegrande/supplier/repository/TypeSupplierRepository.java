package pe.edu.vallegrande.supplier.repository;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import pe.edu.vallegrande.supplier.model.TypeSupplier;
import reactor.core.publisher.Flux;

@Repository
public interface TypeSupplierRepository extends ReactiveCrudRepository<TypeSupplier, Long> {
    Flux<TypeSupplier>findAllByOrderByIdAsc();
}

