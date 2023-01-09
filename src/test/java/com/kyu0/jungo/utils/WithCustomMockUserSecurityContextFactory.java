package com.kyu0.jungo.utils;

import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

public class WithCustomMockUserSecurityContextFactory implements WithSecurityContextFactory<WithCustomMockUser> {

    @Override
    public SecurityContext createSecurityContext(WithCustomMockUser annotation) {
        SecurityContext context = SecurityContextHolder.createEmptyContext();

        context.setAuthentication(toAuthentication(annotation));

        return context;
    }
    
    private Authentication toAuthentication(WithCustomMockUser info) {
        return new UsernamePasswordAuthenticationToken(info.username(), info.password(),
            Stream.of(info.roles()).map(SimpleGrantedAuthority::new).collect(Collectors.toList())
        );
    }
}
