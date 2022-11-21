package com.kyu0.jungo.member;

import org.springframework.web.bind.annotation.*;

import com.kyu0.jungo.system.auth.JwtProvider;
import com.kyu0.jungo.util.ApiUtils;
import com.kyu0.jungo.util.ApiUtils.*;

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
    public ApiResult<String> login(@RequestBody Member.LoginRequest requestDto) {
        Member.LoginResponse responseDto = memberService.authenticateByIdAndPassword(requestDto);
        
        return ApiUtils.success(jwtProvider.generateToken(responseDto.getId(), responseDto.getRole()));
    }
}