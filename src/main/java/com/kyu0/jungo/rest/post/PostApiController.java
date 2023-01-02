package com.kyu0.jungo.rest.post;

import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.EntityNotFoundException;
import javax.validation.ValidationException;

import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import com.kyu0.jungo.aop.LoginCheck;
import com.kyu0.jungo.rest.post.Post.FindAllResponse;
import com.kyu0.jungo.util.ApiUtils;
import com.kyu0.jungo.util.ApiUtils.ApiResult;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@AllArgsConstructor
@RestController
public class PostApiController {
    
    private final PostService postService;

    @GetMapping("/api/posts")
    public ApiResult<List<FindAllResponse>> findAll(Pageable pageable) {
        return ApiUtils.success(
            postService.findAll(pageable).stream().map(Post.FindAllResponse::new)
            .collect(Collectors.toUnmodifiableList())
        );
    }

    @GetMapping("/api/posts/{id}")
    public ApiResult<?> findById(@PathVariable Long id) {
        try {
            return ApiUtils.success(
                new Post.FindResponse(postService.findById(id))
            );
        }
        catch (EntityNotFoundException e) {
            return ApiUtils.error(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @LoginCheck
    @PostMapping("/api/posts")
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
    @PutMapping("/api/posts")
    public ApiResult<?> modify(@RequestBody Post.ModifyRequest requestDto, Authentication authentication) {
        try {
            String memberId = (String)authentication.getPrincipal();
            return ApiUtils.success(
                new Post.FindResponse(postService.modify(requestDto, memberId))
            );
        }
        catch (EntityNotFoundException | ValidationException e) {
            return ApiUtils.error(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @LoginCheck
    @DeleteMapping("/api/posts/{id}")
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
