package com.kyu0.jungo.member;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.kyu0.jungo.system.auth.JwtProvider;

@RestController
public class MemberApiController {
    private final MemberService memberService;
    private final JwtProvider jwtProvider;

    public MemberApiController(MemberService memberService, JwtProvider jwtProvider) {
        this.memberService = memberService;
        this.jwtProvider = jwtProvider;
    }

    @PostMapping("/api/member")
    public void save(@RequestBody Member.SaveRequest requestDto) {
        memberService.save(requestDto);
    }

    @PostMapping("/api/login")
    public String login(@RequestBody Member.LoginRequest requestDto) {
        UserDetails userDetails = memberService.loadUserByUsername(requestDto.getId());
        return jwtProvider.generateToken(userDetails.getUsername(), userDetails.getAuthorities());
    }
}