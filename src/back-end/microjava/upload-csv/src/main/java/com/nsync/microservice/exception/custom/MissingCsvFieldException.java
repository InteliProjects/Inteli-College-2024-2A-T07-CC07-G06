package com.nsync.microservice.exception.custom;

import com.nsync.microservice.exception.AppException;
import jakarta.ws.rs.core.Response;

public class MissingCsvFieldException extends AppException {
    public MissingCsvFieldException(String message) { super(message, Response.Status.BAD_REQUEST.getStatusCode()); }
}
