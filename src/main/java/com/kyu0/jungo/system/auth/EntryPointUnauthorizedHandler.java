package com.kyu0.jungo.system.auth;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import com.kyu0.jungo.util.ApiUtils;
import com.kyu0.jungo.util.FormatConverter;

@Component
public class EntryPointUnauthorizedHandler implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
            AuthenticationException authException) throws IOException, ServletException {
        
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setHeader("Content-Type", "application/json");
        response.getWriter().write(FormatConverter.toJson(
            ApiUtils.error("Unauthorized", HttpStatus.UNAUTHORIZED)
        ));
        response.getWriter().flush();
        response.getWriter().close();
    }
    
}
