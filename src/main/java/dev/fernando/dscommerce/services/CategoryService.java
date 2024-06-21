package dev.fernando.dscommerce.services;

import java.util.List;

import org.springframework.stereotype.Service;

import dev.fernando.dscommerce.dto.CategoryDTO;
import dev.fernando.dscommerce.repositories.CategoryRepository;

@Service
public class CategoryService {
    private final CategoryRepository categoryRepository;
    public CategoryService(final CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public List<CategoryDTO> findAll() {
        return this.categoryRepository.findAll()
        .stream()
        .map(CategoryDTO::new)
        .toList();
    }
}
