package com.kyu0.jungo.member;

import java.beans.Transient;

import javax.persistence.Entity;
import javax.persistence.Id;

import com.kyu0.jungo.member.authority.MemberAuthority;

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
    private MemberAuthority authority;

    @Builder
    public Member(String id, String password, MemberAuthority authority) {
        this.id = id;
        this.password = password;
        this.authority = authority;
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
        private MemberAuthority authority;

        @Transient
        public Member toEntity() {
            return Member.builder()
                        .id(this.id)
                        .password(this.password)
                        .authority(this.authority)
                    .build();
        }
    }
}