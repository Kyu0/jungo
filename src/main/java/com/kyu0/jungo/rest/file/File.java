package com.kyu0.jungo.rest.file;

import javax.persistence.*;
import javax.validation.constraints.Min;

import com.kyu0.jungo.rest.post.Post;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
@Entity(name = "ATTACHMENT")
public class File {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    private FileName fileName;

    @Min(value = 1, message = "첨부 파일의 크기가 잘못되었습니다.")
    @Column(name = "SIZE")
    private int size;

    @ManyToOne
    @JoinColumn(name = "POST_ID")
    private Post post;

    public void setPost(Post post) {
        this.post = post;
        
        if (!post.getAttaches().contains(this)) {
            post.getAttaches().add(this);
        }
    }
}
