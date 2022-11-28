package com.kyu0.jungo.entity.postcategory;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.*;

import com.kyu0.jungo.entity.post.Post;

import lombok.*;

@NoArgsConstructor
@Getter
@Entity(name="POST_CATEGORY")
public class PostCategory {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String name;

    @OneToMany
    private List<Post> posts = new ArrayList<>();
}
