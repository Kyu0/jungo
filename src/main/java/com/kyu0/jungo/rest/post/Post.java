package com.kyu0.jungo.rest.post;

import java.util.List;

import javax.persistence.*;

import com.kyu0.jungo.rest.BaseTimeEntity;
import com.kyu0.jungo.rest.attach.Attach;
import com.kyu0.jungo.rest.comment.Comment;
import com.kyu0.jungo.rest.member.Member;
import com.kyu0.jungo.rest.postcategory.PostCategory;

import lombok.*;

@NoArgsConstructor
@Getter
@Entity(name="POST")
public class Post extends BaseTimeEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String content;

    @Column(name = "VIEW_COUNT")
    private int viewCount;

    @ManyToOne
    @JoinColumn(name = "CATEGORY_ID")
    private PostCategory category;

    @ManyToOne
    @JoinColumn(name = "MEMBER_ID")
    private Member member;

    @OneToMany
    private List<Comment> comments;

    @OneToMany
    private List<Attach> attaches;
}