package com.kyu0.jungo.rest.category;

import java.util.*;
import java.util.stream.Collectors;

import javax.persistence.*;
import javax.validation.constraints.*;
import javax.validation.Valid;


import com.kyu0.jungo.rest.post.Post;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Entity(name="CATEGORY")
@Builder
public class Category {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotBlank(message = "카테고리의 이름을 지정해주세요.")
    @Size(max = 10, message = "카테고리의 이름은 10자 이하여야 합니다.")
    @Column(length = 10, nullable = false)
    private String name;

    @OneToMany
    @Builder.Default
    private List<Post> posts = new ArrayList<>();
    
    public void addPost(Post post) {
        this.posts.add(post);

        if (post.getCategory() != this) {
            post.setCategory(this);
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
        private String name;
        
        public @Valid Category toEntity() {
            return Category.builder()
                .name(this.name)
            .build();
        }
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ModifyRequest {
        private Integer id;
        private String name;

        public @Valid Category toEntity(Category category) {
            return new Category(category.id, this.name, category.posts);
        }
    }

    @Getter
    @Setter
    public static class FindResponse {
        private Integer id;
        private String name;
        private List<Post.FindAllResponse> posts;

        public FindResponse(Category category) {
            this.id = category.getId();
            this.name = category.getName();
            this.posts = category.getPosts().stream().map(Post.FindAllResponse::new)
                .collect(Collectors.toUnmodifiableList());
        }
    }

    @Getter
    @Setter
    public static class FindAllResponse {
        private Integer id;
        private String name;

        public FindAllResponse(Category category) {
            this.id = category.getId();
            this.name = category.getName();
        }
    }
}
