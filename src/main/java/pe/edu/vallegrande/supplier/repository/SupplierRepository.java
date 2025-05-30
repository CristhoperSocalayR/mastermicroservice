package pe.edu.vallegrande.supplier.repository;


import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import pe.edu.vallegrande.supplier.model.Supplier;
import reactor.core.publisher.Flux;

@Repository
public interface SupplierRepository extends ReactiveCrudRepository<Supplier, Long> {
    Flux<Supplier> findAllByOrderByIdAsc();
}
