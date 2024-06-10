package dev.fernando.dscommerce.services;

import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import dev.fernando.dscommerce.dto.ProductDTO;
import dev.fernando.dscommerce.entities.Product;
import dev.fernando.dscommerce.repositories.ProductRepository;

@Service
public class ProductService {
    private final ProductRepository productRepository;

    public ProductService (final ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Transactional(readOnly = true)
    public ProductDTO findById(Long id) {
        Optional<Product> product = this.productRepository.findById(id);
        Product p = product.get();
        var dto = new ProductDTO(p);
        return dto;
    }
}
