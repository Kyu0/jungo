package com.kyu0.jungo.rest.post;

import javax.persistence.EntityNotFoundException;
import javax.validation.ValidationException;

import org.springframework.data.domain.Pageable;
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

    @GetMapping("/api/post")
    public ApiResult<?> findAll(Pageable pageable) {
        return ApiUtils.success(postService.findAll(pageable));
    }

    @GetMapping("/api/post/{id}")
    public ApiResult<?> findById(@PathVariable Long id) {
        try {
            return ApiUtils.success(postService.findById(id));
        }
        catch (EntityNotFoundException e) {
            return ApiUtils.error(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
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

    @LoginCheck
    @PutMapping("/api/post")
    public ApiResult<?> modify(@RequestBody Post.ModifyRequest requestDto, Authentication authentication) {
        try {
            String memberId = (String)authentication.getPrincipal();
            return ApiUtils.success(postService.modify(requestDto, memberId));
        }
        catch (EntityNotFoundException | ValidationException e) {
            return ApiUtils.error(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @LoginCheck
    @DeleteMapping("/api/post/{id}")
    public ApiResult<?> delete(@PathVariable Long id, Authentication authentication) {
        try {
            String memberId = (String)authentication.getPrincipal();
            return ApiUtils.success(postService.delete(id, memberId));
        }
        catch (EntityNotFoundException e) {
            return ApiUtils.error(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}
