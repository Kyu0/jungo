package com.kyu0.jungo.rest.post;

import javax.persistence.EntityNotFoundException;
import javax.validation.ValidationException;

import org.springframework.stereotype.Service;

import com.kyu0.jungo.rest.postcategory.PostCategory;
import com.kyu0.jungo.rest.postcategory.PostCategoryRepository;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class PostService {

    private final PostRepository postRepository;
    private final PostCategoryRepository postCategoryRepository;

    public Post save(Post.SaveRequest requestDto) throws ValidationException, EntityNotFoundException {
        PostCategory postCategory = postCategoryRepository.findById(requestDto.getCategoryId()).orElseThrow(() -> new EntityNotFoundException("카테고리 id가 잘못 입력되었습니다."));
        
        return postRepository.save(requestDto.toEntity(postCategory));
    }
}
