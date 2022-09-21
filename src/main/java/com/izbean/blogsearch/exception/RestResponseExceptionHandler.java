package com.izbean.blogsearch.exception;

import com.izbean.blogsearch.dto.response.error.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class RestResponseExceptionHandler {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(BindException.class)
    public ErrorResponse bindExceptionHandler(BindException e) {
        ErrorResponse response = ErrorResponse.builder()
                .code("400")
                .message("잘못 된 요청 입니다.")
                .build();

        for (FieldError fieldError : e.getFieldErrors()) {
            response.addValidation(fieldError.getField(), fieldError.getDefaultMessage());
        }

        return response;
    }

    @ExceptionHandler(InternalException.class)
    public ResponseEntity<ErrorResponse> internalExceptionHandler(InternalException e) {
        int status = e.getStatusCode();

        ErrorResponse response = ErrorResponse.builder()
                .code(String.valueOf(status))
                .message(e.getMessage())
                .validation(e.getValidation())
                .build();

        return ResponseEntity.status(status).body(response);
    }

}
