package com.kyu0.jungo.rest.post;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

import com.kyu0.jungo.rest.BaseTimeEntity;
import com.kyu0.jungo.rest.attach.Attach;
import com.kyu0.jungo.rest.category.Category;
import com.kyu0.jungo.rest.comment.Comment;
import com.kyu0.jungo.rest.member.Member;

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

    @Column(name = "IS_SOLD")
    private boolean isSold;

    @NotNull(message = "카테고리의 id를 입력해주세요.")
    @ManyToOne
    @JoinColumn(name = "CATEGORY_ID", nullable = false)
    private Category category;

    @NotNull(message = "작성자의 id를 입력해주세요.")
    @ManyToOne
    @JoinColumn(name = "MEMBER_ID", nullable = false, updatable = false)
    private Member member;

    @OneToMany
    @Builder.Default
    private List<Comment> comments = new ArrayList<>();

    @OneToMany
    @Builder.Default
    private List<Attach> attaches = new ArrayList<>();

    public void setMember(Member member) {
        this.member = member;

        if (!member.getPosts().contains(this)) {
            member.addPost(this);
        }
    }

    public void setCategory(Category category) {
        this.category = category;

        if (!category.getPosts().contains(this)) {
            category.getPosts().add(this);
        }
    }

    public void addComment(Comment comment) {
        this.comments.add(comment);

        if (comment.getPost() != this) {
            comment.setPost(this);
        }
    }

    public void addAttach(Attach attach) {
        this.attaches.add(attach);

        if (attach.getPost() != this) {
            attach.setPost(this);
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
        
        @NotBlank
        private String memberId;
        @NotBlank
        private String title;
        private String content;
        @NotNull
        private Integer categoryId;

        public @Valid Post toEntity(Member member, Category category) {
            return Post.builder()
                .title(title)
                .content(content)
                .viewCount(0)
                .isSold(false)
                .member(member)
                .category(category)
            .build();
        }
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ModifyRequest {

        private Long id;
        @NotBlank
        private String title;
        private String content;
        @NotBlank
        private String memberId;
        private boolean isSold;
        @NotNull
        private Integer categoryId;

        public @Valid Post toEntity(Post post, Category category) {
            return Post.builder()
                .id(id)
                .title(title)
                .content(content)
                .viewCount(post.getViewCount())
                .isSold(isSold)
                .category(category)
                .member(post.getMember())
                .comments(post.getComments())
                .attaches(post.getAttaches())
            .build();
        }
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DeleteRequest {
        @NotNull
        private Long id;
        @NotNull
        private String memberId;
    }

    @Getter
    @Setter
    public static class FindResponse {
        private Long id;
        private String title;
        private String content;
        private Integer categoryId;
        private int viewCount;
        private boolean isSold;
        private String memberId;
        private List<Comment.FindResponse> comments;
        private LocalDateTime createdAt;
        private LocalDateTime lastModifiedAt;

        public FindResponse (@Valid Post post) {
            this.id = post.getId();
            this.title = post.getTitle();
            this.content = post.getContent();
            this.categoryId = post.getCategory().getId();
            this.viewCount = post.getViewCount();
            this.isSold = post.isSold();
            this.memberId = post.getMember().getId();
            this.comments = post.getComments().stream().map(Comment.FindResponse::new)
                .collect(Collectors.toUnmodifiableList());
            this.createdAt = post.getCreatedAt();
            this.lastModifiedAt = post.getLastModifiedAt();
        }
    }

    @Getter
    @Setter
    public static class FindAllResponse {
        private Long id;
        private String title;
        private Integer categoryId;
        private int viewCount;
        private boolean isSold;
        private String memberId;
        private LocalDateTime createdAt;
        
        public FindAllResponse (@Valid Post post) {
            this.id = post.getId();
            this.title = post.getTitle();
            this.isSold = post.isSold();
            this.categoryId = post.getCategory().getId();
            this.memberId = post.getMember().getId();
            this.createdAt = post.getCreatedAt();
        }
    }
}