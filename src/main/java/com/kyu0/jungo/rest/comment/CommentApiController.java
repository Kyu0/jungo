package com.kyu0.jungo.rest.comment;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.kyu0.jungo.aop.LoginCheck;
import com.kyu0.jungo.util.ApiUtils;
import com.kyu0.jungo.util.ApiUtils.ApiResult;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@RestController
public class CommentApiController {
    
    private final CommentService commentService;

    @LoginCheck
    @PostMapping("/api/comments")
    public ApiResult<?> save(@RequestBody Comment.SaveRequest requestDto, Authentication authentication) {
        try {
            String memberId = (String)authentication.getPrincipal();
            requestDto.setMemberId(memberId);

            return ApiUtils.success(commentService.save(requestDto));
        }
        catch (Exception e) {
            return ApiUtils.error(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @LoginCheck
    @PutMapping("/api/comments")
    public ApiResult<?> modify(@RequestBody Comment.ModifyRequest requestDto, Authentication authentication) {
        try {
            String memberId = (String)authentication.getPrincipal();
            requestDto.setMemberId(memberId);

            return ApiUtils.success(commentService.modify(requestDto, memberId));
        }
        catch (Exception e) {
            return ApiUtils.error(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @LoginCheck
    @DeleteMapping("/api/comments/{id}")
    public ApiResult<?> delete(@PathVariable Long id, Authentication authentication) {
        try {
            String memberId = (String)authentication.getPrincipal();
            
            return ApiUtils.success(commentService.delete(id, memberId));
        }
        catch (Exception e) {
            return ApiUtils.error(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}
