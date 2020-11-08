package com.gsg.youtubemonitor.controller;

import com.gsg.youtubemonitor.common.YMException;
import com.gsg.youtubemonitor.common.YMExceptionReason;
import com.gsg.youtubemonitor.dto.Error;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class YMExceptionMapper {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleNotFou(MethodArgumentNotValidException e) {
        FieldError fieldError = e.getBindingResult().getFieldError();
        String message = "Illegal arguments were passed";
        if (fieldError != null) {
            message = fieldError.getDefaultMessage();
        }
        YMException ymException = new YMException(YMExceptionReason.BAD_REQUEST, message);
        return handleException(ymException);
    }

    @ExceptionHandler(YMException.class)
    public ResponseEntity<?> handleException(YMException e) {
        Error error = Error.builder()
                           .code(e.getReason().name())
                           .message(e.getMessage())
                           .build();

        return ResponseEntity.status(e.getReason().getCode())
                             .body(error);
    }
}
