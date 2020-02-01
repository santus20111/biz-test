package com.example.bizhome.web.errorhandler;

import org.springframework.boot.web.reactive.error.DefaultErrorAttributes;
import org.springframework.validation.FieldError;
import org.springframework.web.reactive.function.server.ServerRequest;

import java.util.*;

public class GlobalErrorAttributes extends DefaultErrorAttributes {

    @Override
    public Map<String, Object> getErrorAttributes(ServerRequest request,
                                                  boolean includeStackTrace) {
        Map<String, Object> map = new HashMap<>();

        List<Map<String, Object>> errors = new ArrayList<>();
        map.put("errors", errors);
        Map<String, Object> defaultResponse = super.getErrorAttributes(request, includeStackTrace);

        if(defaultResponse.containsKey("errors")) {
            for (FieldError fieldError : (Collection<FieldError>) defaultResponse.get("errors")) {
                Map<String, Object> error = new HashMap<>();
                error.put("field", fieldError.getField());
                error.put("message", fieldError.getDefaultMessage());
                errors.add(error);
            }
        } else {
            Map<String, Object> error = new HashMap<>();
            error.put("message", defaultResponse.get("message"));
            errors.add(error);
        }

        return map;
    }

}
