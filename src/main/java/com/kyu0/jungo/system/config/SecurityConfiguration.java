package com.kyu0.jungo.system.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.kyu0.jungo.entity.member.role.MemberRole;
import com.kyu0.jungo.system.auth.*;

@Configuration
public class SecurityConfiguration {

    private final JwtProvider jwtProvider;
    private final CustomAccessDeniedHandler accessDeniedhandler;
    private final EntryPointUnauthorizedHandler unauthorizedHandler;

    public SecurityConfiguration(JwtProvider jwtProvider, CustomAccessDeniedHandler accessDeniedHandler, EntryPointUnauthorizedHandler unauthorizedHandler) {
        this.jwtProvider = jwtProvider;
        this.accessDeniedhandler = accessDeniedHandler;
        this.unauthorizedHandler = unauthorizedHandler;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
            .exceptionHandling()
                .accessDeniedHandler(accessDeniedhandler)
                .authenticationEntryPoint(unauthorizedHandler)
                .and()
            .addFilterBefore(new JwtFilter(jwtProvider), UsernamePasswordAuthenticationFilter.class)
            .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        httpSecurity
            .authorizeRequests() // 요청에 대한 권한 설정
            .antMatchers("/").authenticated()
            .antMatchers("/test/user").hasRole(MemberRole.ROLE_USER.getNameWithoutPrefix())
            .antMatchers("/test/admin").hasRole(MemberRole.ROLE_ADMIN.getNameWithoutPrefix())
            .anyRequest().permitAll();

        httpSecurity
            .formLogin()
                .disable()
            .httpBasic()
                .disable()
            .csrf()
                .disable();

        return httpSecurity.build();
    }
}