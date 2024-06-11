package dev.fernando.dscommerce.controllers.handlers;

import java.time.Instant;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import dev.fernando.dscommerce.dto.CustomError;
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
}
