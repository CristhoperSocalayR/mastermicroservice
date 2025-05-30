package pe.edu.vallegrande.supplier.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pe.edu.vallegrande.supplier.model.Supplier;
import pe.edu.vallegrande.supplier.repository.SupplierRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.LocalDate;

import static org.mockito.Mockito.*;

class SupplierServiceTest {

    private SupplierRepository repository;
    private SupplierService service;

    private Supplier sampleSupplier;

    @BeforeEach
    void setUp() {
        repository = mock(SupplierRepository.class);
        service = new SupplierService(repository);

        sampleSupplier = new Supplier();
        sampleSupplier.setId(1L);
        sampleSupplier.setRuc("12345678901");
        sampleSupplier.setCompany("Empresa SAC");
        sampleSupplier.setName("Juan");
        sampleSupplier.setLastname("Pérez");
        sampleSupplier.setEmail("juan@example.com");
        sampleSupplier.setCellphone("987654321");
        sampleSupplier.setNotes("Proveedor confiable");
        sampleSupplier.setRegisterDate(LocalDate.now());
        sampleSupplier.setStatus("A");
        sampleSupplier.setTypeSupplierId(2L);
    }

    @Test
    void testFindAll() {
        when(repository.findAllByOrderByIdAsc()).thenReturn(Flux.just(sampleSupplier));

        StepVerifier.create(service.findAll())
                .expectNext(sampleSupplier)
                .verifyComplete();
    }

    @Test
    void testDeleteById() {
        when(repository.deleteById(1L)).thenReturn(Mono.empty());

        StepVerifier.create(service.deleteById(1L))
                .verifyComplete();

        verify(repository, times(1)).deleteById(1L);
    }

    @Test
    void testLogicalDelete() {
        when(repository.findById(1L)).thenReturn(Mono.just(sampleSupplier));
        when(repository.save(any(Supplier.class)))
                .thenAnswer(invocation -> {
                    Supplier s = invocation.getArgument(0);
                    s.setStatus("I");
                    return Mono.just(s);
                });

        StepVerifier.create(service.logicalDelete(1L))
                .expectNextMatches(s -> s.getStatus().equals("I"))
                .verifyComplete();
    }

    @Test
    void testRestore() {
        sampleSupplier.setStatus("I");
        when(repository.findById(1L)).thenReturn(Mono.just(sampleSupplier));
        when(repository.save(any(Supplier.class)))
                .thenAnswer(invocation -> {
                    Supplier s = invocation.getArgument(0);
                    s.setStatus("A");
                    return Mono.just(s);
                });

        StepVerifier.create(service.restore(1L))
                .expectNextMatches(s -> s.getStatus().equals("A"))
                .verifyComplete();
    }
}