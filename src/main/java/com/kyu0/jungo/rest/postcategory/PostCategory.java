package com.kyu0.jungo.rest.postcategory;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.Length;

import com.kyu0.jungo.rest.post.Post;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Entity(name="POST_CATEGORY")
@Builder
public class PostCategory {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotBlank(message = "카테고리의 이름을 지정해주세요.")
    @Size(max = 10, message = "카테고리의 이름은 10자 이하여야 합니다.")
    @Column(length = 10)
    private String name;

    @OneToMany
    @Builder.Default
    private List<Post> posts = new ArrayList<>();
    

    /**
     * DTO 선언부
     */
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SaveRequest {
        private String name;
        
        public @Valid PostCategory toEntity() {
            return PostCategory.builder()
                .name(this.name)
            .build();
        }
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ModifyRequest {
        private int id;
        private String name;
    }
}
