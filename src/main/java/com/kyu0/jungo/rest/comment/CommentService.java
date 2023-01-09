package com.kyu0.jungo.rest.comment;

import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;
import javax.validation.ValidationException;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kyu0.jungo.rest.comment.Comment.DeleteRequest;
import com.kyu0.jungo.rest.comment.Comment.ModifyRequest;
import com.kyu0.jungo.rest.comment.Comment.SaveRequest;
import com.kyu0.jungo.rest.member.Member;
import com.kyu0.jungo.rest.member.MemberRepository;
import com.kyu0.jungo.rest.post.Post;
import com.kyu0.jungo.rest.post.PostRepository;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class CommentService {
    
    private final MemberRepository memberRepository;
    private final CommentRepository commentRepository;
    private final PostRepository postRepository;

    @Transactional(rollbackFor = {EntityNotFoundException.class, ValidationException.class})
    public Comment save(@Valid SaveRequest requestDto) throws EntityNotFoundException, ValidationException {
        Member member = memberRepository.findById(requestDto.getMemberId()).orElseThrow(() -> new EntityNotFoundException("해당 id를 가진 회원이 없습니다."));
        Post post = postRepository.findById(requestDto.getPostId()).orElseThrow(() -> new EntityNotFoundException("해당 id를 가진 게시글을 찾을 수 없습니다."));

        return commentRepository.save(requestDto.toEntity(member, post));
    }

    @Transactional(rollbackFor = {EntityNotFoundException.class, AccessDeniedException.class, ValidationException.class})
    public Comment modify(@Valid ModifyRequest requestDto) throws EntityNotFoundException, ValidationException, AccessDeniedException {
        Comment comment = commentRepository.findById(requestDto.getId()).orElseThrow(() -> new EntityNotFoundException("해당 id를 가진 댓글이 없습니다."));

        if (!isOwner(comment, requestDto)) {
            throw new AccessDeniedException("해당 댓글을 수정할 권한이 없습니다.");
        }

        return commentRepository.save(Comment.builder()
            .id(comment.getId())
            .content(requestDto.getContent())
            .member(comment.getMember())
            .post(comment.getPost())
            .build());
    }

    @Transactional(rollbackFor = {EntityNotFoundException.class, AccessDeniedException.class, ValidationException.class})
    public boolean delete(DeleteRequest requestDto) throws EntityNotFoundException, ValidationException, AccessDeniedException {
        Comment comment = commentRepository.findById(requestDto.getId()).orElseThrow(() -> new EntityNotFoundException("해당 id를 가진 댓글이 없습니다."));

        if (!isOwner(comment, requestDto.getMemberId())) {
            throw new AccessDeniedException("해당 댓글을 지울 권한이 없습니다.");
        }

        commentRepository.delete(comment);

        return commentRepository.existsById(requestDto.getId());
    }

    private boolean isOwner(Comment comment, ModifyRequest requestDto) {
        return comment.getMember().getId().equals(requestDto.getMemberId());
    }

    private boolean isOwner(Comment comment, String memberId) {
        return comment.getMember().getId().equals(memberId);
    }
}
