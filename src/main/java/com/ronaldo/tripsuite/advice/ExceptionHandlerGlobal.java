package com.ronaldo.tripsuite.advice;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.HttpClientErrorException;

@ControllerAdvice
public class ExceptionHandlerGlobal {

//    @ExceptionHandler(IllegalArgumentException.class)
//    public ResponseEntity handleIllegalArgs() {
//        return ResponseEntity.badRequest().body("Bad request");
//    }


    @ExceptionHandler(HttpClientErrorException.class)
    public ResponseEntity handleForbidden() {
        return ResponseEntity.status(403).build();
    }


}
