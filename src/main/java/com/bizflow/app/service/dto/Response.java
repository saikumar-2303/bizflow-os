package com.bizflow.app.service.dto;

import java.io.Serializable;
import org.springframework.http.HttpStatus;

public class Response implements Serializable {

    private static final long serialVersionUID = 1L;

    private Object success;

    public Response() {
        super();
    }

    public Response(Object success, Object error, HttpStatus status) {
        this.success = success;
        this.error = error;
        this.status = status;
    }

    private Object error;

    private HttpStatus status;

    public Object getSuccess() {
        return success;
    }

    public void setSuccess(Object success) {
        this.success = success;
    }

    public Object getError() {
        return error;
    }

    public void setError(Object error) {
        this.error = error;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public void setStatus(HttpStatus status) {
        this.status = status;
    }
}
