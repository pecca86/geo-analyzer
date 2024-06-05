package org.pekka.geoanalyzer.exception;

public class JobAlreadyStartedException extends ApiException {
    public JobAlreadyStartedException(String message) {
        super(message);
    }
}
