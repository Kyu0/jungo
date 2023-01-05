package com.kyu0.jungo.rest.category;

import java.util.stream.Collectors;

import javax.persistence.EntityNotFoundException;
import javax.validation.ValidationException;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import com.kyu0.jungo.util.ApiUtils;
import com.kyu0.jungo.util.ApiUtils.ApiResult;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@AllArgsConstructor
@RestController
public class CategoryApiController {
    
    private final CategoryService categoryService;

    @GetMapping("/api/categories")
    public ApiResult<?> findAll() {
        return ApiUtils.success(
            categoryService.findAll().stream().map(Category.FindAllResponse::new)
            .collect(Collectors.toUnmodifiableList())
        );
    }

    @GetMapping("/api/categories/{id}")
    public ApiResult<?> findById(@PathVariable Integer id){
        return ApiUtils.success(new Category.FindResponse(categoryService.findById(id)));
    }

    @PostMapping("/api/categories")
    public ApiResult<?> save(@RequestBody Category.SaveRequest requestDto) {
        try {
            return ApiUtils.success(categoryService.save(requestDto).getId());
        }
        catch (ValidationException e) {
            log.error(e.getMessage());
            return ApiUtils.error(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/api/categories")
    public ApiResult<?> modify(@RequestBody Category.ModifyRequest requestDto) {
        try {
            return ApiUtils.success(new Category.FindResponse(categoryService.modify(requestDto)));
        }
        catch (ValidationException | EntityNotFoundException e) {
            log.error(e.getMessage());
            return ApiUtils.error(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/api/categories/{id}")
    public ApiResult<?> delete(@PathVariable Integer id) {
        try {
            return ApiUtils.success(categoryService.delete(id));
        }
        catch (EntityNotFoundException e) {
            log.error(e.getMessage());
            return ApiUtils.error(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}
