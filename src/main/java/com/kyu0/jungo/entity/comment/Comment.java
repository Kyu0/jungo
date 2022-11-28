package com.kyu0.jungo.entity.comment;

import javax.persistence.*;

import com.kyu0.jungo.entity.BaseTimeEntity;
import com.kyu0.jungo.entity.member.Member;
import com.kyu0.jungo.entity.post.Post;

import lombok.*;

@NoArgsConstructor
@Getter
@Entity(name = "COMMENT")
public class Comment extends BaseTimeEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String content;

    @ManyToOne
    @JoinColumn(name = "MEMBER_ID")
    private Member member;

    @ManyToOne
    @JoinColumn(name = "POST_ID")
    private Post post;
}