package com.kyu0.jungo.rest.comment;

import javax.persistence.EntityNotFoundException;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

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

    public Comment save(SaveRequest requestDto) throws EntityNotFoundException {
        Member member = memberRepository.findById(requestDto.getMemberId()).orElseThrow(() -> new EntityNotFoundException("해당 id를 가진 회원이 없습니다."));
        Post post = postRepository.findById(requestDto.getPostId()).orElseThrow(() -> new EntityNotFoundException("해당 id를 가진 게시글을 찾을 수 없습니다."));

        return commentRepository.save(requestDto.toEntity(member, post));
    }

    public Comment modify(ModifyRequest requestDto, String memberId) {
        Comment comment = commentRepository.findById(requestDto.getId()).orElseThrow(() -> new EntityNotFoundException("해당 id를 가진 댓글이 없습니다."));

        if (!comment.getMember().getId().equals(memberId)) {
            throw new AccessDeniedException("해당 댓글을 수정할 권한이 없습니다.");
        }
        

        return commentRepository.save(Comment.builder()
            .id(comment.getId())
            .content(requestDto.getContent())
            .member(comment.getMember())
            .post(comment.getPost())
            .build());
    }

    public boolean delete(Long id, String memberId) {
        Comment comment = commentRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("해당 id를 가진 댓글이 없습니다."));

        if (!comment.getMember().getId().equals(memberId)) {
            throw new AccessDeniedException("해당 댓글을 지울 권한이 없습니다.");
        }

        commentRepository.delete(comment);

        return commentRepository.existsById(id);
    }
}
