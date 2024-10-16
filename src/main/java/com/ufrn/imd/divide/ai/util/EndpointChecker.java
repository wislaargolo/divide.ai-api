package com.ufrn.imd.divide.ai.util;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerExecutionChain;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

@Component
public class EndpointChecker {

    private final RequestMappingHandlerMapping requestMappingHandlerMapping;


    public EndpointChecker(RequestMappingHandlerMapping requestMappingHandlerMapping) {
        this.requestMappingHandlerMapping = requestMappingHandlerMapping;
    }

    public boolean isEndpointExist(HttpServletRequest request) {
        try {
            HandlerExecutionChain handlerExecutionChain = requestMappingHandlerMapping.getHandler(request);
            return handlerExecutionChain != null;
        } catch (Exception e) {
            return false;
        }
    }
}
