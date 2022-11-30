package com.kyu0.jungo.rest.member;

import java.beans.Transient;
import java.util.Collection;

import javax.persistence.Entity;
import javax.persistence.Id;

import org.springframework.security.core.GrantedAuthority;

import com.kyu0.jungo.rest.member.role.MemberRole;

import lombok.*;

/**
 * 1. JPA 를 이용하기 위해 기본 생성자를 Lombok 으로 선언 (@NoArgsConstructor)
 * 2. 데이터베이스 테이블과 매핑되는 클래스임을 선언 (@Entity)
 */
@NoArgsConstructor
@Getter
@Entity(name = "MEMBER")
public class Member {
	// id 컬럼을 MEMBER 테이블의 기본키로 설정
    @Id
    private String id;
    private String password;
    private MemberRole role;

    @Builder
    public Member(String id, String password, MemberRole role) {
        this.id = id;
        this.password = password;
        this.role = role;
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

        @Transient
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