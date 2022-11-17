package com.kyu0.jungo.util;

import org.springframework.http.HttpStatus;

import lombok.Getter;

public class ApiUtils {
    
    public static <T> ApiResult<T> success(T response) {
        return new ApiResult<>(true, response, null);
    }

    public static ApiResult<?> error(Throwable throwable, HttpStatus status) {
        return new ApiResult<>(false, null, new ApiError(throwable, status));
    }

    public static ApiResult<?> error(String message, HttpStatus status) {
        return new ApiResult<>(false, null, new ApiError(message, status));
    }

    @Getter
    public static class ApiError {
        private final String message;
        private final int status;

        ApiError(Throwable throwable, HttpStatus status) {
            this(throwable.getMessage(), status);
        }

        ApiError(String message, HttpStatus status) {
            this.message = message;
            this.status = status.value();
        }
    }
    
    @Getter
    public static class ApiResult<T> {
        private final boolean isSuccess;
        private final T response;
        private final ApiError error;

        private ApiResult(boolean isSuccess, T response, ApiError error) {
            this.isSuccess = isSuccess;
            this.response = response;
            this.error = error;
        }
    }
}
