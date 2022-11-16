package com.kyu0.jungo.system.auth;

import java.util.Collection;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import com.kyu0.jungo.member.MemberService;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@PropertySource("security.properties")
@Component
public class JwtProvider {
    
    private final MemberService memberService;
    private final String SECRET_KEY;
    private final long EXPIRE_TIME;

    // 생성자 메소드
    public JwtProvider(MemberService memberService, @Value("${jwt.token.secret-key}") String secretKey, @Value("${jwt.token.expire-time}") long expireTime) {
        this.memberService = memberService;
        this.SECRET_KEY = secretKey;
        this.EXPIRE_TIME = expireTime;
    }

    public String generateToken(Authentication authentication) {
        return generateToken(authentication.getName(), authentication.getAuthorities());
    }

    public String generateToken(String username, Collection<? extends GrantedAuthority> authorities) {
        return Jwts.builder()
            .setSubject(username)
            .claim("authority", authorities.stream().findFirst().get().toString())
            .setExpiration(getExpireDate())
            .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
        .compact();
    }

    public Authentication getAuthentication(String accessToken) {
        UserDetails userDetails = memberService.loadUserByUsername(getUsername(accessToken));

        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    public String resolveToken(HttpServletRequest request) {
        return request.getHeader("Authorization");
    }

    public boolean validateToken(String accessToken) {
        if (accessToken == null) {
            return false;
        }
        
        try {
            return Jwts.parser()
                .setSigningKey(SECRET_KEY)
                .parseClaimsJws(accessToken)
                .getBody()
                .getExpiration()
                .after(new Date());
        }
        catch (Exception e) {
            return false;
        }
    }

    private String getUsername(String accessToken) {
        return Jwts.parser()
            .setSigningKey(SECRET_KEY)
            .parseClaimsJws(accessToken)
            .getBody()
            .getSubject();
    }

    private Date getExpireDate() {
        Date now = new Date();
        return new Date(now.getTime() + EXPIRE_TIME);
    }
}
