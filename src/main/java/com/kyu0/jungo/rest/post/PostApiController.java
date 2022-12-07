package com.kyu0.jungo.rest.post;

import javax.persistence.EntityNotFoundException;
import javax.validation.ValidationException;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import com.kyu0.jungo.aop.LoginCheck;
import com.kyu0.jungo.util.ApiUtils;
import com.kyu0.jungo.util.ApiUtils.ApiResult;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@AllArgsConstructor
@RestController
public class PostApiController {
    
    private final PostService postService;

    @GetMapping("/api/post/{id}")
    public ApiResult<?> findById(@PathVariable Long id) {
        return null;
    }

    @LoginCheck
    @PostMapping("/api/post")
    public ApiResult<?> save(@RequestBody Post.SaveRequest requestDto, Authentication authentication) {
        try {
            requestDto.setMemberId((String)authentication.getPrincipal());
            return ApiUtils.success(postService.save(requestDto));
        }
        catch (ValidationException | EntityNotFoundException e) {
            return ApiUtils.error(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/api/post/{id}")
    public ApiResult<?> modify(@RequestBody Post.ModifyRequest requestDto) {
        try {
            return null;
        }
        catch (Exception e) {
            return null;
        }
    }

    @DeleteMapping("/api/post/{id}")
    public ApiResult<?> delete(@PathVariable Long id) {
        return null;
    }
}
