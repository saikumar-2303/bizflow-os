package com.bizflow.app.service;

import com.bizflow.app.service.dto.Response;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class HelperService {

    public HelperService() {}

    public Response getResponse(Object message, HttpStatus status) {
        return new Response(message, " ", status);
    }
}
