package com.kyu0.jungo.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import lombok.extern.log4j.Log4j2;

@Aspect
@Component
@Log4j2
public class LoginCheckAspect {
    
    @Before("@annotation(com.kyu0.jungo.aop.LoginCheck)")
    public void loginCheck(JoinPoint joinPoint) throws AccessDeniedException {
        log.info("Login Check in \"{}\"", joinPoint.getTarget());
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        log.info("Authentication : {}", authentication);

        if (isAnonymous(authentication)) {
            throw new AccessDeniedException("로그인 인증 토큰과 함께 요청을 보내주세요.");
        }
    }

    private boolean isAnonymous(Authentication authentication) {
        return authentication.getAuthorities().stream()
            .map((authority) -> authority.getAuthority())
            .anyMatch((authority) -> authority.equals("ROLE_ANONYMOUS"));
    }
}
