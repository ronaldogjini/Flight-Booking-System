package com.ronaldo.tripsuite.advice;

import com.ronaldo.tripsuite.dto.ErrorMessageDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.validation.ConstraintViolationException;
import javax.validation.constraints.Null;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

@ControllerAdvice
public class ExceptionHandlerGlobal {

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity handleIllegalArgs(IllegalArgumentException e) {
        return ResponseEntity.badRequest().body(new ErrorMessageDto(e.getMessage()));
    }

    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity handleNull(NullPointerException e) {
        return ResponseEntity.badRequest().body(new ErrorMessageDto(e.getMessage()));
    }


    @ExceptionHandler(HttpClientErrorException.class)
    public ResponseEntity handleForbidden(HttpClientErrorException e) {

        switch (e.getStatusCode()) {
            case FORBIDDEN:
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body(new ErrorMessageDto("You are not allowed to access this resource!"));
        }

        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(new ErrorMessageDto("You are not allowed!"));
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity handleConstraintViolation(ConstraintViolationException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorMessageDto(e.getMessage()));
    }

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity handleNoElementFound(NoSuchElementException e) {
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    public ResponseEntity handleUniqueFields(SQLIntegrityConstraintViolationException e) {
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(new ErrorMessageDto(e.getMessage()));
    }


    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity handleMethodArgs(MethodArgumentNotValidException e) {

        Map<String, String> errors = new HashMap<>();

        e.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        return new ResponseEntity(errors, HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler(UnsupportedOperationException.class)
    public ResponseEntity handleMethodArgs(UnsupportedOperationException e) {
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(new ErrorMessageDto(e.getMessage()));
    }


}
