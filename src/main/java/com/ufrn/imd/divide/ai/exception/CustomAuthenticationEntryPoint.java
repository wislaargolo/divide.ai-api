package com.ufrn.imd.divide.ai.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ufrn.imd.divide.ai.dto.response.ApiResponseDTO;
import com.ufrn.imd.divide.ai.dto.response.ErrorResponseDTO;
import com.ufrn.imd.divide.ai.util.EndpointChecker;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.io.IOException;
import java.time.ZonedDateTime;

@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final ObjectMapper objectMapper;

    public CustomAuthenticationEntryPoint(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException authException) throws IOException, NoResourceFoundException {

        ErrorResponseDTO errorResponse = new ErrorResponseDTO(
                ZonedDateTime.now(),
                HttpServletResponse.SC_UNAUTHORIZED,
                "Unauthorized",
                "Access denied. Please authenticate to access this resource.",
                request.getRequestURI()
        );

        ApiResponseDTO<ErrorResponseDTO> apiResponse = new ApiResponseDTO<>(
                false,
                "Authentication failed",
                null,
                errorResponse
        );

        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");
        response.getWriter().write(objectMapper.writeValueAsString(apiResponse));
        response.getWriter().flush();
    }

}
