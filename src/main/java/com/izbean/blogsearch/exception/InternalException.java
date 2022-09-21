package com.izbean.blogsearch.exception;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@Getter
public abstract class InternalException extends RuntimeException {

    private static final long serialVersionUID = -1161325146656420133L;

    private final Map<String, String> validation = new HashMap<>();

    public InternalException(String message) {
        super(message);
    }

    public InternalException(String message, Throwable cause) {
        super(message, cause);
    }

    public void addValidation(String fieldName, String message) {
        validation.put(fieldName, message);
    }

    abstract public int getStatusCode();

}
