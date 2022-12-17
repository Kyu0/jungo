package com.kyu0.jungo.rest.member;

import java.util.*;

import javax.persistence.*;

import org.springframework.security.core.GrantedAuthority;

import com.kyu0.jungo.rest.member.role.MemberRole;
import com.kyu0.jungo.rest.post.Post;

import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Entity(name = "MEMBER")
public class Member {
	// id 컬럼을 MEMBER 테이블의 기본키로 설정
    @Id
    private String id;
    private String password;
    private MemberRole role;

    @Builder.Default
    private List<Post> posts = new ArrayList<>();

    public void addPost(Post post) {
        this.posts.add(post);

        if (post.getMember() != this) {
            post.setMember(this);
        }
    }

    /**
     * DTO 선언부
     */
    @Getter
    @Setter
    @NoArgsConstructor
    public static class SaveRequest {
        private String id;
        private String password;
        private MemberRole role;

        public Member toEntity() {
            return Member.builder()
                        .id(this.id)
                        .password(this.password)
                        .role(this.role)
                    .build();
        }
    }

    @Getter
    @Setter
    @NoArgsConstructor
    public static class LoginRequest {
        private String id;
        private String password;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    public static class LoginResponse {
        private String id;
        private String password;
        private Collection<? extends GrantedAuthority> role;

        @Builder
        public LoginResponse (String id, String password, Collection<? extends GrantedAuthority> role) {
            this.id = id;
            this.password = password;
            this.role = role;
        }
    }
}