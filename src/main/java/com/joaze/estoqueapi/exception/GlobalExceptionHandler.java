package com.joaze.estoqueapi.exception;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ResponseError> handleGenericException(Exception exception, HttpServletRequest request){
        log.error("Unexpected error occurred", exception);
        ResponseError error = this.createResponseError(HttpStatus.INTERNAL_SERVER_ERROR, "Unexpected error occurred", request, null);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }

    @ExceptionHandler(InsufficientStockException.class)
    public ResponseEntity<ResponseError> handleInsufficientStockException(InsufficientStockException exception, HttpServletRequest request ){
        ResponseError error = this.createResponseError(HttpStatus.BAD_REQUEST, exception.getMessage(), request, null);
        return  ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ResponseError> handleResourceNotFoundException(ResourceNotFoundException exception, HttpServletRequest request){
        ResponseError error = this.createResponseError(HttpStatus.NOT_FOUND, exception.getMessage(), request, null);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ResponseError> handleBusinessException(BusinessException exception, HttpServletRequest request){
        ResponseError error = this.createResponseError(HttpStatus.UNPROCESSABLE_ENTITY, exception.getMessage(), request, null);
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(error);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ResponseError> handleMethodArgumentNotValidException(MethodArgumentNotValidException exceptions, HttpServletRequest request){
        List<FieldErrorResponse> errorsResponse = exceptions.getBindingResult().getFieldErrors().stream()
                .map(fieldError -> new FieldErrorResponse(fieldError.getField(), fieldError.getDefaultMessage())).toList();

        ResponseError errors = this.createResponseError(HttpStatus.BAD_REQUEST, exceptions.getMessage(), request, errorsResponse);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
    }

    private ResponseError createResponseError(HttpStatus http, String message, HttpServletRequest request, List<FieldErrorResponse> errors){
        return new ResponseError(
                LocalDateTime.now(),
                http.value(),
                http.getReasonPhrase(),
                message,
                request.getRequestURI(),
                errors
        );
    }
}
