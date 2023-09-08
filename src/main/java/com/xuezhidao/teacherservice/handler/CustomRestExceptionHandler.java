package com.xuezhidao.teacherservice.handler;

import com.xuezhidao.teacherservice.exception.CoureseNotExistException;
import com.xuezhidao.teacherservice.exception.CoureseStatusException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
@Component
public class CustomRestExceptionHandler extends ResponseEntityExceptionHandler {

    public static final String ERROR_RESPONSE = "Error response: {}";

    @ExceptionHandler(CoureseNotExistException.class)
    public ResponseEntity<String> handleConstraintViolationException(
            CoureseNotExistException ex, HttpServletRequest httpRequest) {
        HttpStatus httpStatus = HttpStatus.NOT_FOUND;
        return new ResponseEntity<>(ex.getMessage(), httpStatus);
    }

    @ExceptionHandler(CoureseStatusException.class)
    public ResponseEntity<String> handleConstraintViolationException(
            CoureseStatusException ex, HttpServletRequest httpRequest) {
        HttpStatus httpStatus = HttpStatus.BAD_REQUEST;
        return new ResponseEntity<>(ex.getMessage(), httpStatus);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleConstraintViolationException(
            Exception ex, HttpServletRequest httpRequest) {
        HttpStatus httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
        return new ResponseEntity<>("服务错误，请重试", httpStatus);
    }
}
