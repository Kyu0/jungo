package com.kyu0.jungo.rest.comment;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.kyu0.jungo.rest.BaseTimeEntity;
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
    @Column(name = "MEMBER_ID", nullable = false, updatable = false)
    private String memberId;

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
        private String memberId;
        private Long postId;

        public @Valid Comment toEntity(Post post) {
            return Comment.builder()
                .content(content)
                .memberId(memberId)
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
}