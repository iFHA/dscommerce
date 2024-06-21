package dev.fernando.dscommerce.controllers.handlers;

import java.time.Instant;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import dev.fernando.dscommerce.dto.CustomError;
import dev.fernando.dscommerce.dto.ValidationError;
import dev.fernando.dscommerce.services.exceptions.DatabaseException;
import dev.fernando.dscommerce.services.exceptions.ForbiddenException;
import dev.fernando.dscommerce.services.exceptions.ResourceNotFoundException;
import jakarta.servlet.http.HttpServletRequest;

@ControllerAdvice
public class ControllerExceptionHandler {
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<CustomError> handleResourceNotFoundExeption(ResourceNotFoundException ex, HttpServletRequest request){
        var status = HttpStatus.NOT_FOUND.value();
        return ResponseEntity.status(status)
        .body(new CustomError(Instant.now(), status, ex.getMessage(), request.getRequestURI()));
    }

    @ExceptionHandler(DatabaseException.class)
    public ResponseEntity<CustomError> handleDatabaseException(DatabaseException e, HttpServletRequest request) {
        var status = HttpStatus.BAD_REQUEST.value();
        return ResponseEntity.badRequest()
        .body(new CustomError(Instant.now(), status, e.getMessage(), request.getRequestURI()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<CustomError> handleMethodArgumentNotValidException(MethodArgumentNotValidException e, HttpServletRequest request) {
        var status = HttpStatus.UNPROCESSABLE_ENTITY.value();
        var error = new ValidationError(Instant.now(), status, "Dados inválidos!", request.getRequestURI());
        e.getBindingResult()
        .getFieldErrors()
        .stream()
        .forEach(field -> error.addError(field.getField(), field.getDefaultMessage()));
        return ResponseEntity.status(status).body(error);
    }

    @ExceptionHandler(ForbiddenException.class)
    public ResponseEntity<CustomError> handleForbiddenException(ForbiddenException e, HttpServletRequest request) {
        var status = HttpStatus.FORBIDDEN.value();
        return ResponseEntity.status(status)
        .body(new CustomError(Instant.now(), status, e.getMessage(), request.getRequestURI()));
    }
}
