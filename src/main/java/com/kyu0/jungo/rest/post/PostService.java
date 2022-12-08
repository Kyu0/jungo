package com.kyu0.jungo.rest.post;

import javax.persistence.EntityNotFoundException;
import javax.validation.ValidationException;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import com.kyu0.jungo.rest.post.Post.ModifyRequest;
import com.kyu0.jungo.rest.postcategory.PostCategory;
import com.kyu0.jungo.rest.postcategory.PostCategoryRepository;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class PostService {

    private final PostRepository postRepository;
    private final PostCategoryRepository postCategoryRepository;

    private static final String CATEGORY_NOT_FOUND = "해당 id를 가진 카테고리를 찾을 수 없습니다.";
    private static final String POST_NOT_FOUND = "해당 id를 게시글을 찾을 수 없습니다.";
    private static final String MODIFY_NOT_ALLOWED = "해당 게시글을 수정할 권한이 없습니다.";
    private static final String DELETE_NOT_ALLOWED = "해당 게시글을 삭제할 권한이 없습니다.";

    public Post save(Post.SaveRequest requestDto) throws ValidationException, EntityNotFoundException {
        PostCategory category = postCategoryRepository.findById(requestDto.getCategoryId()).orElseThrow(() -> new EntityNotFoundException(CATEGORY_NOT_FOUND));
        
        return postRepository.save(requestDto.toEntity(category));
    }

    public Post modify(ModifyRequest requestDto, String memberId) throws EntityNotFoundException, AccessDeniedException {
        Post post = postRepository.findById(requestDto.getId()).orElseThrow(() -> new EntityNotFoundException("해당 id를 가진 게시글이 없습니다."));
        PostCategory category = postCategoryRepository.findById(requestDto.getCategoryId()).orElseThrow(() -> new EntityNotFoundException(CATEGORY_NOT_FOUND));

        if (post.getMemberId() != memberId) {
            throw new AccessDeniedException(MODIFY_NOT_ALLOWED);
        }

        return postRepository.save(requestDto.toEntity(post, category));
    }

    public boolean delete(Long id, String memberId) throws EntityNotFoundException, AccessDeniedException {
        Post post = postRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(POST_NOT_FOUND));

        if (!post.getMemberId().equals(memberId)) {
            throw new AccessDeniedException(DELETE_NOT_ALLOWED);
        }

        postRepository.delete(post);

        return !postRepository.existsById(id);
    }

    public Post findById(Long id) {
        return postRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(POST_NOT_FOUND));
    }

    public Page<Post> findAll(Pageable pageable) {
        return postRepository.findAll(pageable);
    }
}
