package com.kyu0.jungo.rest.post;

import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;
import javax.validation.ValidationException;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.kyu0.jungo.rest.category.Category;
import com.kyu0.jungo.rest.category.CategoryRepository;
import com.kyu0.jungo.rest.member.Member;
import com.kyu0.jungo.rest.member.MemberRepository;
import com.kyu0.jungo.rest.post.Post.DeleteRequest;
import com.kyu0.jungo.rest.post.Post.ModifyRequest;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class PostService {
    private final MemberRepository memberRepository;
    private final PostRepository postRepository;
    private final CategoryRepository categoryRepository;

    private static final String CATEGORY_NOT_FOUND = "해당 id를 가진 카테고리를 찾을 수 없습니다.";
    private static final String POST_NOT_FOUND = "해당 id를 가진 게시글을 찾을 수 없습니다.";
    private static final String MEMBER_NOT_FOUND = "해당 id를 가진 회원을 찾을 수 없습니다.";
    private static final String MODIFY_NOT_ALLOWED = "해당 게시글을 수정할 권한이 없습니다.";
    private static final String DELETE_NOT_ALLOWED = "해당 게시글을 삭제할 권한이 없습니다.";

    @Transactional
    public Long save(@Valid Post.SaveRequest requestDto) throws ValidationException, EntityNotFoundException {
        Category category = categoryRepository.findById(requestDto.getCategoryId()).orElseThrow(() -> new EntityNotFoundException(CATEGORY_NOT_FOUND));
        Member member = memberRepository.findById(requestDto.getMemberId()).orElseThrow(() -> new EntityNotFoundException(MEMBER_NOT_FOUND));

        return postRepository.save(requestDto.toEntity(member, category)).getId();
    }

    @Transactional
    public Post modify(@Valid ModifyRequest requestDto) throws EntityNotFoundException, AccessDeniedException {
        Post post = postRepository.findById(requestDto.getId()).orElseThrow(() -> new EntityNotFoundException("해당 id를 가진 게시글이 없습니다."));
        Category category = categoryRepository.findById(requestDto.getCategoryId()).orElseThrow(() -> new EntityNotFoundException(CATEGORY_NOT_FOUND));

        if (!isOwner(post, requestDto.getMemberId())) {
            throw new AccessDeniedException(MODIFY_NOT_ALLOWED);
        }

        return postRepository.save(requestDto.toEntity(post, category));
    }

    @Transactional
    public boolean delete(@Valid DeleteRequest requestDto) throws EntityNotFoundException, AccessDeniedException {
        Post post = postRepository.findById(requestDto.getId()).orElseThrow(() -> new EntityNotFoundException(POST_NOT_FOUND));

        if (!isOwner(post, requestDto.getMemberId())) {
            throw new AccessDeniedException(DELETE_NOT_ALLOWED);
        }

        postRepository.delete(post);

        return !postRepository.existsById(requestDto.getId());
    }

    @Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
    public Post findById(Long id) {
        return postRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(POST_NOT_FOUND));
    }

    @Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
    public Page<Post> findAll(Pageable pageable) {
        return postRepository.findAll(pageable);
    }

    private boolean isOwner(Post post, String memberId) {
        return post.getMember().getId().equals(memberId);
    }
}
