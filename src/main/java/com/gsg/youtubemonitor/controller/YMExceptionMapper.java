package com.gsg.youtubemonitor.controller;

import com.gsg.youtubemonitor.common.YMException;
import com.gsg.youtubemonitor.common.YMExceptionReason;
import com.gsg.youtubemonitor.dto.Error;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class YMExceptionMapper {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleNotFou(MethodArgumentNotValidException ex) {
        String message = ex.getBindingResult().getFieldError().getDefaultMessage();
        YMException ymException = new YMException(YMExceptionReason.BAD_REQUEST, message);
        return handleException(ymException);
    }

    @ExceptionHandler(YMException.class)
    public ResponseEntity<?> handleException(YMException ex) {
        Error error = Error.builder()
                           .code(ex.getReason().name())
                           .message(ex.getMessage())
                           .build();

        return ResponseEntity.status(ex.getReason().getCode())
                             .body(error);
    }
}
