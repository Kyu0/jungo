package com.kyu0.jungo.rest.post;

import javax.persistence.EntityNotFoundException;
import javax.validation.ValidationException;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.kyu0.jungo.util.ApiUtils;
import com.kyu0.jungo.util.ApiUtils.ApiResult;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@RestController
public class PostApiController {
    
    private final PostService postService;

    @PostMapping("/api/post")
    public ApiResult<?> save(@RequestBody Post.SaveRequest requestDto) {
        try {
            return ApiUtils.success(postService.save(requestDto));
        }
        catch (ValidationException | EntityNotFoundException e) {
            return ApiUtils.error(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}
