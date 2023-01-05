package com.kyu0.jungo.rest.category;

import java.util.List;

import javax.persistence.EntityNotFoundException;
import javax.validation.ValidationException;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class CategoryService {
    
    private final CategoryRepository categoryRepository;
    private static final String NOT_FOUND_MESSAGE = "해당 id를 가진 카테고리를 찾을 수 없습니다.";

    @Transactional
    public List<Category> findAll() {
        return categoryRepository.findAll();
    }

    public Category findById(Integer id) {
        return categoryRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(NOT_FOUND_MESSAGE));
    }

    public Category save(Category.SaveRequest requestDto) throws ValidationException {
        return categoryRepository.save(requestDto.toEntity());
    }

    public Category modify(Category.ModifyRequest requestDto) throws ValidationException, EntityNotFoundException {
        Category category = categoryRepository.findById(requestDto.getId()).orElseThrow(() -> new EntityNotFoundException(NOT_FOUND_MESSAGE));

        return categoryRepository.save(requestDto.toEntity(category));
    }

    public boolean delete(Integer id) throws EntityNotFoundException {
        if (categoryRepository.existsById(id)) {
            categoryRepository.deleteById(id);

            return !categoryRepository.existsById(id);
        }
        else {
            throw new EntityNotFoundException(NOT_FOUND_MESSAGE);
        }
    }
}
