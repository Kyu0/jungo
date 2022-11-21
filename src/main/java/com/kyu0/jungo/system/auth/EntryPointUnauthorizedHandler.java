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
        
        redirect(response);
    }
    
    /**
     * 로그인 페이지로 이동하는 메소드
     * @param response
     * @throws IOException
     */
    private void redirect(HttpServletResponse response) throws IOException {
        response.sendRedirect("/login");
    }

    /**
     * error 를 반환하는 메소드
     * @param response
     * @throws IOException
     */
    private void sendError(HttpServletResponse response) throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setHeader("Content-Type", "application/json");
        response.getWriter().write(FormatConverter.toJson(
            ApiUtils.error("Unauthorized", HttpStatus.UNAUTHORIZED)
        ));
        response.getWriter().flush();
        response.getWriter().close();
    }
}
