package dev.fernando.dscommerce.services;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import dev.fernando.dscommerce.dto.ProductDTO;
import dev.fernando.dscommerce.entities.Product;
import dev.fernando.dscommerce.repositories.ProductRepository;
import dev.fernando.dscommerce.services.exceptions.DatabaseException;
import dev.fernando.dscommerce.services.exceptions.ResourceNotFoundException;
import jakarta.persistence.EntityNotFoundException;

@Service
public class ProductService {
    private final ProductRepository productRepository;

    public ProductService(final ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Transactional(readOnly = true)
    public ProductDTO findById(Long id) {
        Product p = this.productRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Produto de id = %d não encontrado!".formatted(id)));
        var dto = new ProductDTO(p);
        return dto;
    }

    @Transactional(readOnly = true)
    public Page<ProductDTO> findAll(String name, Pageable pageable) {
        Page<Product> products = this.productRepository.searchByName(name, pageable);
        return products.map(ProductDTO::new);
    }

    @Transactional
    public ProductDTO store(ProductDTO productDTO) {
        Product p = new Product(productDTO);
        p.setId(null);
        return new ProductDTO(this.productRepository.save(p));
    }
    
    @Transactional
    public ProductDTO update(Long id, ProductDTO dto) {
        try {
            Product entity = this.productRepository.getReferenceById(id);
            copyDtoToEntity(dto, entity);
            return new ProductDTO(this.productRepository.save(entity));
        } catch (EntityNotFoundException e) {
            throw new ResourceNotFoundException("Produto de id = %d não encontrado".formatted(id));
        }
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    public void delete(Long id) {
        if (!this.productRepository.existsById(id)) {
            throw new ResourceNotFoundException("Produto de id = %d não encontrado".formatted(id));
        }
        try {
            this.productRepository.deleteById(id);
        } catch (DataIntegrityViolationException e) {
            throw new DatabaseException("Produto de id = %d possui dependências, portanto, não é possível excluí-lo.".formatted(id));
        }
    }

    private void copyDtoToEntity(ProductDTO dto, Product entity) {
        entity.setName(dto.name());
        entity.setDescription(dto.description());
        entity.setPrice(dto.price());
        entity.setImgUrl(dto.imgUrl());
    }

}
