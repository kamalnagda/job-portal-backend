package com.jobportal.exception;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MultipartException.class)
    public ResponseEntity<String> handleMultipartException(MultipartException ex) {
        System.out.println("	 MultipartException handler called");

        Throwable cause = ex.getCause();
        if (cause instanceof org.springframework.web.multipart.MaxUploadSizeExceededException) {
            return ResponseEntity
                    .status(HttpStatus.PAYLOAD_TOO_LARGE)
                    .body(" File too large. Max allowed is 1MB.");
        }

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(" File upload failed: " + ex.getMessage());
    }
    
    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<?> handleMaxSizeException(MaxUploadSizeExceededException ex) {
        System.out.println("✅ MultipartException handler called");
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body("❌ File too large! Maximum allowed size is 5MB.");
    }
    
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error ->
            errors.put(error.getField(), error.getDefaultMessage())
        );
        return ResponseEntity.badRequest().body(errors); // Sends the map to frontend
    }
}
