package com.kyu0.jungo.entity.media;

import javax.persistence.*;

import com.kyu0.jungo.entity.post.Post;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
@Entity(name = "ATTACH")
public class Attach {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String originalName;

    private String savedName;

    private String savedPath;

    @ManyToOne
    @JoinColumn(name = "POST_ID")
    private Post post;
}
