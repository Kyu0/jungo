package com.kyu0.jungo.member;

import java.util.ArrayList;
import java.util.List;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

        List<GrantedAuthority> authority = new ArrayList<>();
        authority.add(new SimpleGrantedAuthority(member.getAuthority().getName()));
        
        return Member.LoginResponse.builder()
            .id(member.getId())
            .password(member.getPassword())
            .authority(authority)
        .build();
    }

    private UserDetails toUserDetails(Member member) {
        return User.builder()
            .username(member.getId())
            .password(member.getPassword())
            .authorities(new SimpleGrantedAuthority(member.getAuthority().getName()))
        .build();
    }
}
