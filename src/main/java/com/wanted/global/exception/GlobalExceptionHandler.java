package com.wanted.global.exception;

import com.wanted.global.dto.ResponseDTO;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.stream.Collectors;

@RestControllerAdvice(annotations = {RestController.class}) // 모든 예외
public class GlobalExceptionHandler {
    @ExceptionHandler(MethodArgumentNotValidException.class) // 유효성 검사 예외
    public ResponseEntity<ResponseDTO<String>> handleValidationErrors(MethodArgumentNotValidException exception) {

        String errorMessage = exception
                .getBindingResult()
                .getAllErrors()
                .stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.joining("/"));

        String data = exception
                .getBindingResult()
                .getAllErrors()
                .stream()
                .map(DefaultMessageSourceResolvable::getCode)
                .collect(Collectors.joining("/"));

        ResponseDTO<String> response = new ResponseDTO<>("E-400", errorMessage, data);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }
}
