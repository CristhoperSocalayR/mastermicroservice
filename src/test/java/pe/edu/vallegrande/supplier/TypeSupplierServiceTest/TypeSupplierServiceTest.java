package pe.edu.vallegrande.supplier.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import pe.edu.vallegrande.supplier.model.TypeSupplier;
import pe.edu.vallegrande.supplier.repository.TypeSupplierRepository;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Flux;

import static org.mockito.Mockito.*;

class TypeSupplierServiceTest {

    @Mock
    private TypeSupplierRepository repository;

    private TypeSupplierService typeSupplierService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        typeSupplierService = new TypeSupplierService(repository);
    }

    @Test
    void shouldRestoreTypeSupplier() {
        // Arrange
        Long id = 1L;
        TypeSupplier typeSupplier = new TypeSupplier();
        typeSupplier.setId(id);
        typeSupplier.setStatus("I");

        when(repository.findById(id)).thenReturn(Mono.just(typeSupplier));
        when(repository.save(typeSupplier)).thenReturn(Mono.just(typeSupplier));

        // Act
        Mono<TypeSupplier> result = typeSupplierService.restore(id);

        // Assert
        result.doOnTerminate(() -> {
            // Verifica que el estado sea restaurado
            verify(repository, times(1)).findById(id);
            verify(repository, times(1)).save(typeSupplier);
            assert typeSupplier.getStatus().equals("A");
        }).block();
    }

    @Test
    void shouldDeletePhysically() {
        // Arrange
        Long id = 1L;
        when(repository.deleteById(id)).thenReturn(Mono.empty());

        // Act
        Mono<Void> result = typeSupplierService.deleteById(id);

        // Assert
        result.doOnTerminate(() -> {
            // Verifica que se haya llamado a deleteById
            verify(repository, times(1)).deleteById(id);
        }).block();
    }

    @Test
    void shouldSoftDelete() {
        // Arrange
        Long id = 1L;
        TypeSupplier typeSupplier = new TypeSupplier();
        typeSupplier.setId(id);
        typeSupplier.setStatus("A");

        when(repository.findById(id)).thenReturn(Mono.just(typeSupplier));
        when(repository.save(typeSupplier)).thenReturn(Mono.just(typeSupplier));

        // Act
        Mono<TypeSupplier> result = typeSupplierService.softDelete(id);

        // Assert
        result.doOnTerminate(() -> {
            // Verifica que el estado haya cambiado a "I"
            verify(repository, times(1)).findById(id);
            verify(repository, times(1)).save(typeSupplier);
            assert typeSupplier.getStatus().equals("I");
        }).block();
    }

    @Test
    void shouldGetAllTypeSuppliers() {
        // Arrange
        TypeSupplier typeSupplier1 = new TypeSupplier();
        typeSupplier1.setId(1L);
        TypeSupplier typeSupplier2 = new TypeSupplier();
        typeSupplier2.setId(2L);

        when(repository.findAllByOrderByIdAsc()).thenReturn(Flux.just(typeSupplier1, typeSupplier2));

        // Act
        Flux<TypeSupplier> result = typeSupplierService.getAll();

        // Assert
        result.doOnTerminate(() -> {
            // Verifica que se hayan llamado a `findAllByOrderByIdAsc`
            verify(repository, times(1)).findAllByOrderByIdAsc();
        }).blockLast();  // Espera a que el `Flux` se complete.
    }
}