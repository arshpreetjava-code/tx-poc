package com.testing.ex.controller;

import com.testing.ex.domain.dto.response.ErrorDto;
import jakarta.validation.ConstraintViolationException;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;

/**
 * Centralized exception handler that converts exceptions thrown by controllers
 * into consistent {@link ErrorDto} HTTP responses. Keeps controllers clean and
 * returns appropriate HTTP status codes for common error scenarios.
 */
@RestController
@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

  /**
   * Handles validation errors for method parameters annotated with
   * {@link jakarta.validation.constraints}.
   *
   * @param ex the ConstraintViolationException containing validation errors
   * @return ResponseEntity with ErrorDto and HTTP 400 status
   */
  @ExceptionHandler(ConstraintViolationException.class)
  public ResponseEntity<ErrorDto> handleConstraintViolations(ConstraintViolationException ex) {
    log.info("Invalid Constraints: {}", ex.getMessage());

    ErrorDto error = ErrorDto.builder()
        .status(HttpStatus.BAD_REQUEST.value())
        .message(ex.getMessage())
        .build();

    return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
  }

  /**
   * Handles JSON parsing errors and invalid value types in request bodies.
   *
   * @param ex the HttpMessageNotReadableException
   * @return ResponseEntity with ErrorDto and HTTP 400 status
   */
  @ExceptionHandler(HttpMessageNotReadableException.class)
  public ResponseEntity<ErrorDto> handleJsonParseError(HttpMessageNotReadableException ex) {
    log.info("Invalid JSON or value type: {}", ex.getMessage());

    ErrorDto error = ErrorDto.builder()
        .status(HttpStatus.BAD_REQUEST.value())
        .message("Invalid JSON or value type")
        .build();

    return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
  }

  /**
   * Handles validation errors for method arguments annotated with
   * {@link jakarta.validation.Valid}.
   *
   * @param ex the MethodArgumentNotValidException containing validation errors
   * @return ResponseEntity with ErrorDto and HTTP 400 status
   */
  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ErrorDto> handleMethodArgumentNotValidException(
      MethodArgumentNotValidException ex) {
    log.error("Validation error", ex);

    String errorMessage = ex.getBindingResult()
        .getFieldErrors()
        .stream()
        .map(error -> error.getField() + ":" + error.getDefaultMessage())
        .collect(Collectors.joining(", "));

    ErrorDto error = ErrorDto.builder()
        .status(HttpStatus.BAD_REQUEST.value())
        .message("Validation failed " + errorMessage)
        .build();

    return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
  }

  /**
   * Handles all uncaught exceptions.
   *
   * @param ex the Exception
   * @return ResponseEntity with ErrorDto and HTTP 500 status
   */
  @ExceptionHandler(Exception.class)
  public ResponseEntity<ErrorDto> handleException(Exception ex) {
    log.error("Caught Exception", ex);

    ErrorDto error = ErrorDto.builder()
        .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
        .message("An unexpected Error occurred")
        .build();

    return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
  }

  /**
   * Handles requests with unacceptable media types.
   *
   * @param ex the HttpMediaTypeNotAcceptableException
   * @return ResponseEntity with ErrorDto and HTTP 406 status
   */
  @ExceptionHandler(HttpMediaTypeNotAcceptableException.class)
  public ResponseEntity<ErrorDto> handleHttpMediaTypeNotAcceptableException(
      HttpMediaTypeNotAcceptableException ex) {
    log.error("Media type not acceptable", ex);

    ErrorDto error = ErrorDto.builder()
        .status(HttpStatus.NOT_ACCEPTABLE.value())
        .message("Requested media type is not acceptable: {} " + ex.getMessage())
        .build();

    return new ResponseEntity<>(error, HttpStatus.NOT_ACCEPTABLE);
  }
}
