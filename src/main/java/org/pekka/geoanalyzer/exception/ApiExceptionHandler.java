package org.pekka.geoanalyzer.exception;

import org.slf4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ApiExceptionHandler {
    private static final Logger LOGGER = org.slf4j.LoggerFactory.getLogger(ApiExceptionHandler.class);

    @ExceptionHandler(value = {JobAlreadyStartedException.class})
    public ResponseEntity<?> handleJobAlreadyStartedException(JobAlreadyStartedException e) {
        LOGGER.error("API exception occurred", e);
        return ResponseEntity.badRequest().body(e.getMessage());
    }

    @ExceptionHandler(value = {JobFailedException.class})
    public ResponseEntity<?> handleJobFailedException(JobFailedException e) {
        LOGGER.error("API exception occurred", e);
        return ResponseEntity.badRequest().body(e.getMessage());
    }
}
