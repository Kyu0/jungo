package com.kyu0.jungo.rest.comment;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.kyu0.jungo.rest.BaseTimeEntity;
import com.kyu0.jungo.rest.member.Member;
import com.kyu0.jungo.rest.post.Post;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Entity(name = "COMMENT")
public class Comment extends BaseTimeEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;

    @NotBlank
    @ManyToOne
    @JoinColumn(name = "MEMBER_ID", nullable = false, updatable = false)
    private Member member;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "POST_ID", nullable = false)
    private Post post;


    public void setPost(Post post) {
        this.post = post;

        if (!post.getComments().contains(this)) {
            post.getComments().add(this);
        }
    }

    /**
     * DTO 선언부
     */

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SaveRequest {
        private String content;
        @NotBlank
        private String memberId;
        @NotNull
        private Long postId;

        public @Valid Comment toEntity(Member member, Post post) {
            return Comment.builder()
                .content(content)
                .member(member)
                .post(post)
            .build();
        }
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ModifyRequest {
        private Long id;
        private String content;
        private String memberId;
    }

    @Getter
    @Setter
    public static class FindResponse {
        private Long id;
        private Long postId;
        private String content;
        private String memberId;

        public FindResponse(Comment comment) {
            this.id = comment.getId();
            this.content = comment.getContent();
            this.postId = comment.getPost().getId();
            this.memberId = comment.getMember().getId();
        }
    }
}