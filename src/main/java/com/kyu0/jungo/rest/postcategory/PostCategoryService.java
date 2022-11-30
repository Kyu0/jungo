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

    public PostCategory modify(PostCategory.ModifyRequest requestDto) {
        PostCategory category = postCategoryRepository.findById(requestDto.getId()).orElseThrow(() -> new EntityNotFoundException(NOT_FOUND_MESSAGE));

        return postCategoryRepository.save(PostCategory.builder()
            .id(category.getId())
            .name(requestDto.getName())
            .posts(category.getPosts())
        .build());
    }

    public boolean delete(Integer id) {
        PostCategory category = postCategoryRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(NOT_FOUND_MESSAGE));
        
        postCategoryRepository.delete(category);

        return postCategoryRepository.existsById(id);
    }
}
