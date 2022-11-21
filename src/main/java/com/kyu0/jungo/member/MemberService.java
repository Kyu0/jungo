package com.kyu0.jungo.member;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.security.core.authority.AuthorityUtils.createAuthorityList;

@Service
public class MemberService implements UserDetailsService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    public MemberService(MemberRepository memberRepository, PasswordEncoder passwordEncoder) {
        this.memberRepository = memberRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public void save(Member.SaveRequest requestDto) {
        requestDto.setPassword(passwordEncoder.encode(requestDto.getPassword()));

        memberRepository.save(requestDto.toEntity());
    }

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException, BadCredentialsException {
        Member member = memberRepository.findById(username)
            .orElseThrow(() -> new UsernameNotFoundException("username"));
    
        return toUserDetails(member);
    }

    public Member.LoginResponse authenticateByIdAndPassword(Member.LoginRequest requestDto) {
        Member member = memberRepository.findById(requestDto.getId())
            .orElseThrow(() -> new UsernameNotFoundException("등록되지 않은 아이디입니다."));

        if (!passwordEncoder.matches(requestDto.getPassword(), member.getPassword())) {
            throw new BadCredentialsException("비밀번호가 잘못되었습니다.");
        }

        return Member.LoginResponse.builder()
            .id(member.getId())
            .password(member.getPassword())
            .role(createAuthorityList(member.getRole().getName()))
        .build();
    }

    private UserDetails toUserDetails(Member member) {
        return User.builder()
            .username(member.getId())
            .password(member.getPassword())
            .authorities(new SimpleGrantedAuthority(member.getRole().getName()))
        .build();
    }
}
