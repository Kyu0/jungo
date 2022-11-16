package com.kyu0.jungo.member;

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
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Member member = memberRepository.findById(username)
            .orElseThrow(() -> new UsernameNotFoundException("username"));
    
        return toUserDetails(member);
    }

    private UserDetails toUserDetails(Member member) {
        return User.builder()
            .username(member.getId())
            .password(member.getPassword())
            .authorities(new SimpleGrantedAuthority(member.getAuthority().getName()))
        .build();
    }
}
