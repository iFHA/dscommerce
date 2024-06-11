package dev.fernando.dscommerce.services;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import dev.fernando.dscommerce.dto.ProductDTO;
import dev.fernando.dscommerce.entities.Product;
import dev.fernando.dscommerce.repositories.ProductRepository;
import dev.fernando.dscommerce.services.exceptions.ResourceNotFoundException;

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
    public Page<ProductDTO> findAll(Pageable pageable) {
        Page<Product> products = this.productRepository.findAll(pageable);
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
        Product entity = this.productRepository.getReferenceById(id);
        copyDtoToEntity(dto, entity);
        return new ProductDTO(this.productRepository.save(entity));
    }

    @Transactional
    public void delete(Long id) {
        this.productRepository.deleteById(id);
    }

    private void copyDtoToEntity(ProductDTO dto, Product entity) {
        entity.setName(dto.name());
        entity.setDescription(dto.description());
        entity.setPrice(dto.price());
        entity.setImgUrl(dto.imgUrl());
    }

}
