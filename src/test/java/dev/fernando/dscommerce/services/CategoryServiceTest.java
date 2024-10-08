package dev.fernando.dscommerce.services;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import dev.fernando.dscommerce.dto.CategoryDTO;
import dev.fernando.dscommerce.entities.Category;
import dev.fernando.dscommerce.repositories.CategoryRepository;
import dev.fernando.dscommerce.tests.CategoryFactory;

@ExtendWith(SpringExtension.class)
public class CategoryServiceTest {
    @InjectMocks
    private CategoryService service;
    @Mock
    private CategoryRepository repository;

    private Category category;
    private List<Category> categories = new ArrayList<>();

    @BeforeEach
    void setUp() throws Exception {
        category = CategoryFactory.createCategory();
        categories.add(category);
        Mockito.when(repository.findAll()).thenReturn(categories);
    }
    
    @Test
    void findAllShouldReturnCategoryDTOList() {
        List<CategoryDTO> list = service.findAll();
        Assertions.assertEquals(categories.size(), list.size());
        Assertions.assertEquals(category.getId(), list.get(0).id());
        Assertions.assertEquals(category.getName(), list.get(0).name());
    }
}
