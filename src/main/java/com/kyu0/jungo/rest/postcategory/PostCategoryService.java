package com.kyu0.jungo.rest.postcategory;

import java.util.List;
import java.util.Optional;

import javax.persistence.EntityNotFoundException;
import javax.validation.ValidationException;

import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class PostCategoryService {
    
    private final PostCategoryRepository postCategoryRepository;
    private static final String NOT_FOUND_MESSAGE = "해당 id를 가진 카테고리를 찾을 수 없습니다.";

    public List<PostCategory> findAll() {
        return postCategoryRepository.findAll();
    }

    public Optional<PostCategory> findById(Integer id) {
        return postCategoryRepository.findById(id);
    }

    public PostCategory save(PostCategory.SaveRequest requestDto) throws ValidationException {
        return postCategoryRepository.save(requestDto.toEntity());
    }

    public PostCategory modify(PostCategory.ModifyRequest requestDto) throws ValidationException, EntityNotFoundException {
        PostCategory category = postCategoryRepository.findById(requestDto.getId()).orElseThrow(() -> new EntityNotFoundException(NOT_FOUND_MESSAGE));

        return postCategoryRepository.save(requestDto.toEntity(category));
    }

    public boolean delete(Integer id) throws EntityNotFoundException {
        if (postCategoryRepository.existsById(id)) {
            postCategoryRepository.deleteById(id);

            return !postCategoryRepository.existsById(id);
        }
        else {
            throw new EntityNotFoundException(NOT_FOUND_MESSAGE);
        }
    }
}
