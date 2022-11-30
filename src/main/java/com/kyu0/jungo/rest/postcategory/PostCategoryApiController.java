package com.kyu0.jungo.rest.postcategory;

import javax.validation.ValidationException;
import javax.validation.constraints.NotNull;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import com.kyu0.jungo.util.ApiUtils;
import com.kyu0.jungo.util.ApiUtils.ApiResult;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@AllArgsConstructor
@RestController
public class PostCategoryApiController {
    
    private final PostCategoryService postCategoryService;

    @GetMapping("/api/post-category")
    public ApiResult<?> findAll() {
        return ApiUtils.success(postCategoryService.findAll());
    }

    @GetMapping("/api/post-category/{id}")
    public ApiResult<?> findById(@PathVariable Integer id){
        return ApiUtils.success(postCategoryService.findById(id));
    }

    @PostMapping("/api/post-category")
    public ApiResult<?> save(@RequestBody PostCategory.SaveRequest requestDto) {
        try {
            return ApiUtils.success(postCategoryService.save(requestDto));
        }
        catch (ValidationException e) {
            log.error(e.getMessage());
            return ApiUtils.error(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/api/post-category")
    public ApiResult<?> modify(@RequestBody PostCategory.ModifyRequest requestDto) {
        return ApiUtils.success(postCategoryService.modify(requestDto));
    }

    @DeleteMapping("/api/post-category/{id}")
    public ApiResult<?> delete(@PathVariable Integer id) {
        return ApiUtils.success(postCategoryService.delete(id));
    }
}
