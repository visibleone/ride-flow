package com.example.driverservice.controller;

import java.time.Instant;
import java.util.Map;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

@RestControllerAdvice
public class RestExceptionHandler {
  // TODO: Create unified Problem Detail in the APIs
  @ExceptionHandler(DuplicateKeyException.class)
  public ResponseEntity<?> handleDuplicateKeyException(
      DuplicateKeyException ex, WebRequest request) {
    Map<String, Object> errorResponse =
        Map.of(
            "timestamp", Instant.now(),
            "status", HttpStatus.CONFLICT.value(),
            "error", "Conflict",
            "message", "A driver with this userId already exists",
            "path", request.getDescription(false).replace("uri=", ""));

    return new ResponseEntity<>(errorResponse, HttpStatus.CONFLICT);
  }
}
