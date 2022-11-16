package com.kyu0.jungo.system.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.kyu0.jungo.system.auth.JwtFilter;
import com.kyu0.jungo.system.auth.JwtProvider;

@Configuration
public class SecurityConfiguration {

    private final JwtProvider jwtProvider;

    public SecurityConfiguration(JwtProvider jwtProvider) {
        this.jwtProvider = jwtProvider;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
            .addFilterBefore(new JwtFilter(jwtProvider), UsernamePasswordAuthenticationFilter.class)
            .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        httpSecurity
            .authorizeRequests() // 요청에 대한 권한 설정
            .antMatchers("/").authenticated()
            .anyRequest().permitAll();

        httpSecurity
            .formLogin()
                .loginPage("/login")
                .and()
            .httpBasic()
                .disable()
            .csrf()
                .disable();

        return httpSecurity.build();
    }
}
