package org.pekka.geoanalyzer.exception;

import org.pekka.geoanalyzer.service.JobStateService;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.concurrent.TimeoutException;

@ControllerAdvice
public class ApiExceptionHandler {
    private static final Logger LOGGER = org.slf4j.LoggerFactory.getLogger(ApiExceptionHandler.class);

    private final JobStateService jobStateService;

    @Autowired
    public ApiExceptionHandler(JobStateService jobStateService) {
        this.jobStateService = jobStateService;
    }

    @ExceptionHandler(value = {JobAlreadyStartedException.class})
    public ResponseEntity<?> handleJobAlreadyStartedException(JobAlreadyStartedException e) {
        LOGGER.error("API exception occurred", e);
        return ResponseEntity.badRequest().body(e.getMessage());
    }

    @ExceptionHandler(value = {TimeoutException.class})
    public ResponseEntity<?> handleTimeoutException(TimeoutException e) {
        LOGGER.error("API exception occurred", e);
        return ResponseEntity.status(504).body(e.getMessage());
    }

}
