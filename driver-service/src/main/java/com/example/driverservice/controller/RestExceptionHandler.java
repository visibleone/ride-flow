package com.example.driverservice.controller;

import java.time.Instant;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

@RestControllerAdvice
@Slf4j
public class RestExceptionHandler {
  // TODO: Create unified Problem Detail in the APIs
  @ExceptionHandler(DuplicateKeyException.class)
  public ResponseEntity<?> handleDuplicateKeyException(
      DuplicateKeyException ex, WebRequest request) {
    log.error("A driver with this userId already exists: {}", ex.getMessage());

    Map<String, Object> errorResponse =
        Map.of(
            "timestamp", Instant.now(),
            "status", HttpStatus.CONFLICT.value(),
            "error", "Conflict",
            "message", "A driver with this userId already exists",
            "path", request.getDescription(false).replace("uri=", ""));

    return new ResponseEntity<>(errorResponse, HttpStatus.CONFLICT);
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<?> handleGeneralException(Exception ex) {
    log.error("Unexpected error occurred: {}", ex.getMessage(), ex);

    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
        .body("An unexpected error occurred. Please contact support if the problem persists.");
  }
}
