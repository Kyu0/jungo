package com.kyu0.jungo.system.auth;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import com.kyu0.jungo.util.ApiUtils;
import com.kyu0.jungo.util.FormatConverter;

@Component
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response,
            AccessDeniedException accessDeniedException) throws IOException, ServletException {
        
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        response.setHeader("Content-Type", "applicaiton/json");
        response.getWriter().write(FormatConverter.toJson(
            ApiUtils.error("Forbidden", HttpStatus.FORBIDDEN)
        ));
        response.getWriter().flush();
        response.getWriter().close();
    }
    
}
