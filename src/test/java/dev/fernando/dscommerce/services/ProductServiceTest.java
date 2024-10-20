package dev.fernando.dscommerce.services;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import dev.fernando.dscommerce.dto.ProductDTO;
import dev.fernando.dscommerce.dto.ProductMinDTO;
import dev.fernando.dscommerce.entities.Product;
import dev.fernando.dscommerce.repositories.ProductRepository;
import dev.fernando.dscommerce.services.exceptions.DatabaseException;
import dev.fernando.dscommerce.services.exceptions.ResourceNotFoundException;
import dev.fernando.dscommerce.tests.ProductFactory;
import jakarta.persistence.EntityNotFoundException;

@ExtendWith(SpringExtension.class)
public class ProductServiceTest {

    @InjectMocks
    private ProductService service;

    @Mock
    private ProductRepository repository;

    private Long existingId;
    private Long nonExistingId;
    private Long dependentId;
    private Product entity;
    private ProductDTO dto;
    private PageImpl<Product> page;

    @BeforeEach
    void setUp() throws Exception {
        this.existingId = 1L;
        this.nonExistingId = 1000L;
        this.dependentId = 20L;
        this.entity = ProductFactory.createProduct();
        this.dto = new ProductDTO(entity);
        this.page = new PageImpl<>(List.of(this.entity));

        Mockito.when(repository.findById(existingId)).thenReturn(Optional.of(entity));
        Mockito.when(repository.findById(nonExistingId)).thenReturn(Optional.empty());
        Mockito.when(repository.searchByName(ArgumentMatchers.anyString(), (Pageable) ArgumentMatchers.any())).thenReturn(page);
        Mockito.when(repository.save(ArgumentMatchers.any())).thenReturn(entity);
        Mockito.when(repository.getReferenceById(existingId)).thenReturn(entity);
        Mockito.doThrow(EntityNotFoundException.class).when(repository).getReferenceById(nonExistingId);
        Mockito.when(repository.existsById(existingId)).thenReturn(true);
        Mockito.when(repository.existsById(dependentId)).thenReturn(true);
        Mockito.when(repository.existsById(nonExistingId)).thenReturn(false);
        Mockito.doNothing().when(repository).deleteById(existingId);
        Mockito.doThrow(DataIntegrityViolationException.class).when(repository).deleteById(dependentId);
    }

    @Test
    void findByIdShouldReturnDtoWhenProductIdExists() {
        var result = service.findById(existingId);
        Assertions.assertEquals(existingId, result.getId());
    }
    @Test
    void findByIdShouldThrowResourceNotFoundExceptionWhenProductIdDoesNotExist() {
        Assertions.assertThrows(ResourceNotFoundException.class, () -> service.findById(nonExistingId));
    }
    @Test
    void findAllShoudlReturnPagedProductMinDto() {
        var result = service.findAll("produto", PageRequest.of(1, 1));
        Assertions.assertNotNull(result);
        Assertions.assertEquals(1, result.getSize());
        Assertions.assertInstanceOf(ProductMinDTO.class, result.getContent().get(0));
    }
    @Test
    void storeShouldReturnProductDto(){
        var result = service.store(this.dto);
        Assertions.assertNotNull(result);
        Assertions.assertEquals(result.getId(), dto.getId());
        Assertions.assertInstanceOf(ProductDTO.class, result);
    }
    @Test
    void updateShouldReturnProductDtoWhenIdExists() {
        var result = service.update(existingId, dto);
        Assertions.assertNotNull(result);
        Assertions.assertEquals(existingId, result.getId());
        Assertions.assertInstanceOf(ProductDTO.class, result);
    }
    @Test
    void updateShouldThrowResourceNotFoundExceptionWhenIdDoesNotExist() {
        Assertions.assertThrows(ResourceNotFoundException.class, () -> service.update(nonExistingId, dto));
    }

    @Test
    void deleteShouldReturnNothingWhenIdExists() {
        Assertions.assertDoesNotThrow(()->service.delete(existingId));
    }

    @Test
    void deleteShouldThrowResourceNotFoundExceptionWhenIdDoesNotExist() {
        Assertions.assertThrows(ResourceNotFoundException.class, () -> service.delete(nonExistingId));
    }

    @Test
    void deleteShouldThrowDatabaseExceptionWhenDependentExist() {
        Assertions.assertThrows(DatabaseException.class, () -> service.delete(dependentId));
    }
}
