package com.ufrn.imd.divide.ai.exception;

import com.ufrn.imd.divide.ai.dto.response.ApiResponseDTO;
import com.ufrn.imd.divide.ai.dto.response.ErrorResponseDTO;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.time.ZonedDateTime;
import java.util.stream.Collectors;

@RestControllerAdvice
public class ControllerExceptionHandler {

    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    @ExceptionHandler({ResourceNotFoundException.class, NoResourceFoundException.class})
    public ApiResponseDTO<ErrorResponseDTO> handleResourceNotFoundException(
            Exception exception, WebRequest request) {

        ErrorResponseDTO errorResponse = new ErrorResponseDTO(
                ZonedDateTime.now(),
                HttpStatus.NOT_FOUND.value(),
                HttpStatus.NOT_FOUND.getReasonPhrase(),
                exception.getMessage(),
                request.getDescription(false));

        return new ApiResponseDTO<>(
                false,
                "Resource not found",
                null,
                errorResponse
        );
    }

    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    @ExceptionHandler(BusinessException.class)
    public ApiResponseDTO<ErrorResponseDTO> handleBusinessException(
            BusinessException exception, WebRequest request) {

        ErrorResponseDTO errorResponse = new ErrorResponseDTO(
                ZonedDateTime.now(),
                exception.getHttpStatusCode().value(),
                "Business error",
                exception.getMessage(),
                request.getDescription(false));

        return new ApiResponseDTO<>(
                false,
                "Business validation failed",
                null,
                errorResponse
        );
    }

    @ResponseStatus(value = HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(BadCredentialsException.class)
    public ApiResponseDTO<ErrorResponseDTO> handleBadCredentialsException(
            BadCredentialsException exception, WebRequest request) {

        ErrorResponseDTO errorResponse = new ErrorResponseDTO(
                ZonedDateTime.now(),
                HttpStatus.UNAUTHORIZED.value(),
                HttpStatus.UNAUTHORIZED.getReasonPhrase(),
                exception.getMessage(),
                request.getDescription(false));

        return new ApiResponseDTO<>(
                false,
                "Invalid credentials",
                null,
                errorResponse
        );
    }

    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ApiResponseDTO<ErrorResponseDTO> handleMethodArgumentNotValidException(
            MethodArgumentNotValidException exception, WebRequest request) {
        String errors = exception.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.joining(", "));

        ErrorResponseDTO errorResponse = new ErrorResponseDTO(
                ZonedDateTime.now(),
                HttpStatus.BAD_REQUEST.value(),
                HttpStatus.BAD_REQUEST.getReasonPhrase(),
                errors,
                request.getDescription(false));

        return new ApiResponseDTO<>(
                false,
                "Validation error",
                null,
                errorResponse
        );
    }

    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public ApiResponseDTO<ErrorResponseDTO> handleException(Exception exception, WebRequest request) {
        ErrorResponseDTO errorResponse = new ErrorResponseDTO(
                ZonedDateTime.now(),
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(),
                exception.getMessage(),
                request.getDescription(false));

        return new ApiResponseDTO<>(
                false,
                "Internal server error",
                null,
                errorResponse
        );
    }
}
