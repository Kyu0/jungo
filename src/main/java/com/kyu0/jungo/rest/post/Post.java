package com.kyu0.jungo.rest.post;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

import com.kyu0.jungo.rest.BaseTimeEntity;
import com.kyu0.jungo.rest.attach.Attach;
import com.kyu0.jungo.rest.comment.Comment;
import com.kyu0.jungo.rest.postcategory.PostCategory;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Entity(name="POST")
@Builder
public class Post extends BaseTimeEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "제목을 입력해주세요.")
    @Length(max = 64)
    @Column(nullable = false, length = 64)
    private String title;

    @NotBlank(message = "게시물의 내용을 입력해주세요.")
    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @Column(name = "VIEW_COUNT")
    private int viewCount;

    @NotNull(message = "카테고리의 id를 입력해주세요.")
    @ManyToOne
    @JoinColumn(name = "CATEGORY_ID", nullable = false)
    private PostCategory category;

    @NotNull(message = "작성자의 id를 입력해주세요.")
    @JoinColumn(name = "MEMBER_ID", nullable = false, updatable = false)
    private String memberId;

    @OneToMany
    @Builder.Default
    private List<Comment> comments = new ArrayList<>();

    @OneToMany
    @Builder.Default
    private List<Attach> attaches = new ArrayList<>();

    /**
     * DTO 선언부
     */
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SaveRequest {
        
        private String memberId;
        private String title;
        private String content;
        private Integer categoryId;

        public @Valid Post toEntity(PostCategory postCategory) {
            return Post.builder()
                .title(title)
                .content(content)
                .viewCount(0)
                .memberId(memberId)
                .category(postCategory)
            .build();
        }
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ModifyRequest {

        private Long id;
        private String title;
        private String content;
        private Integer categoryId;

        public @Valid Post toEntity(Post post, PostCategory category) {
            return Post.builder()
                .id(id)
                .title(title)
                .content(content)
                .viewCount(post.getViewCount())
                .category(category)
                .memberId(post.getMemberId())
                .comments(post.getComments())
                .attaches(post.getAttaches())
            .build();
        }
    }
}