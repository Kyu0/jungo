package com.kyu0.jungo.system.controlleradvice;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.kyu0.jungo.util.ApiUtils;
import com.kyu0.jungo.util.ApiUtils.ApiResult;

@RestControllerAdvice
public class AuthenticationAdvice {
    
    @ExceptionHandler(AccessDeniedException.class)
    public ApiResult<?> authenticationExceptionHandler(AccessDeniedException e) {
        return ApiUtils.error(e.getMessage(), HttpStatus.UNAUTHORIZED);
    }
}
