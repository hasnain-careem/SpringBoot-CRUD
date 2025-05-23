package org.example.taskmanager.exception;

import org.example.taskmanager.dto.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(TaskNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleTaskNotFound(TaskNotFoundException ex) {
        return new ResponseEntity<>(new ErrorResponse(
                HttpStatus.NOT_FOUND.value(),
                ex.getMessage()
        ), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(Exception ex) {
        return new ResponseEntity<>(new ErrorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "Internal server error"
        ), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
