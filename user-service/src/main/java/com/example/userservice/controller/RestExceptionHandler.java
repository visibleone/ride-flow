package com.example.userservice.controller;

import feign.RetryableException;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class RestExceptionHandler {

  @ExceptionHandler(EntityNotFoundException.class)
  public ResponseEntity<Void> handleEntityNotFound(EntityNotFoundException ex) {
    log.debug("Entity not found: {}", ex.getMessage());

    return ResponseEntity.notFound().build();
  }

  @ExceptionHandler(DataIntegrityViolationException.class)
  public ResponseEntity<?> handleDataIntegrityViolation(DataIntegrityViolationException ex) {
    log.error("DataIntegrityViolationException: {}", ex.getMessage());
    // TODO: Works well in most cases as it returns less verbose error message. But still risking to
    // expose DB details. In real app, use custom exception and handle the check in the service
    // layer
    return ResponseEntity.status(HttpStatus.CONFLICT).body(ex.getMostSpecificCause().getMessage());
  }

  @ExceptionHandler(RetryableException.class)
  public ResponseEntity<?> handleRetryableException(RetryableException ex) {
    log.error("External service unavailable after retries: {}", ex.getMessage());

    return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
        .body("External service unavailable after retries. Please try again later");
  }
}
