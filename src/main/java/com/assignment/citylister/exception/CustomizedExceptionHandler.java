package com.assignment.citylister.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.validation.ConstraintViolationException;
import java.util.stream.Collectors;

@Repository
@ControllerAdvice
@Slf4j
public class CustomizedExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public final ResponseEntity<ErrorDetails> handleValidationError(MethodArgumentNotValidException ex) {
        log.error("Data Validation failed");
        StringBuilder sb = new StringBuilder();
        String errorMsg = ex.getBindingResult().getFieldErrors().stream().map(e -> e.getField() + " : " + e.getDefaultMessage()).collect(Collectors.joining(","));
        return new ResponseEntity<>(new ErrorDetails("Input Validation failed", errorMsg), HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler(CityNotFoundException.class)
    public final ResponseEntity<ErrorDetails> handleCityNotFoundException(CityNotFoundException ex) {
        return new ResponseEntity<>(new ErrorDetails("Data Not found", ex.getMessage()), HttpStatus.NOT_FOUND);

    }

    @ExceptionHandler(ConstraintViolationException.class)
    public final ResponseEntity<ErrorDetails> handleConstraintViolation(ConstraintViolationException ex) {
        return new ResponseEntity<>(new ErrorDetails("Invalid parameter passed", ex.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public final ResponseEntity<ErrorDetails> handleUnKnownException(Exception ex) {
        return new ResponseEntity<>(new ErrorDetails("Server Error", ex.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
