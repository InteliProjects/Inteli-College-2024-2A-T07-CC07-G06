package com.nsync.exception.custom;

import com.nsync.exception.AppException;
import jakarta.ws.rs.core.Response;

public class MissingCsvFieldException extends AppException {
    public MissingCsvFieldException(String message) {
        super(message, Response.Status.BAD_REQUEST.getStatusCode());
    }
}
