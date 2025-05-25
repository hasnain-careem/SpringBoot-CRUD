package org.example.taskmanager.exception;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import org.example.taskmanager.dto.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(TaskNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleTaskNotFound(TaskNotFoundException ex) {
        return new ResponseEntity<>(new ErrorResponse(
                HttpStatus.NOT_FOUND.value(),
                ex.getMessage() != null ? ex.getMessage() : "Task not found"
        ), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        return new ResponseEntity<>(new ErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                "Validation error",
                errors
        ), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleHttpMessageNotReadable(HttpMessageNotReadableException ex) {
        String message = "Invalid request format";
        if (ex.getCause() instanceof InvalidFormatException) {
            InvalidFormatException ife = (InvalidFormatException) ex.getCause();
            if (ife.getTargetType().isEnum()) {
                String validValues = String.join(", ",
                        Arrays.stream(ife.getTargetType().getEnumConstants())
                                .map(Object::toString)
                                .toArray(String[]::new)
                );
                message = "Invalid value for enum " + ife.getTargetType().getSimpleName()
                        + ". Valid values are: " + validValues;
            } else {
                message = "Invalid format for field. Expected type: " + ife.getTargetType().getSimpleName();
            }
        }
        return new ResponseEntity<>(new ErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                message
        ), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(Exception ex) {
        String message = ex.getMessage();
        if (message == null || message.isBlank()) {
            message = "Internal server error";
        }

        return new ResponseEntity<>(new ErrorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                message
        ), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
