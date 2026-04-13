package com.joaze.estoqueapi.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalHandleException {

    @ExceptionHandler(InsufficientStockException.class)
    public ResponseEntity<ResponseError> insufficientStockException(InsufficientStockException exception, HttpServletRequest request ){
        ResponseError error = this.createResponseErro(HttpStatus.BAD_REQUEST.value(), exception.getMessage(), request);
        return  ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ResponseError> ResourceNotFoundException(ResourceNotFoundException exception, HttpServletRequest request){
        ResponseError error = this.createResponseErro(HttpStatus.NOT_FOUND.value(), exception.getMessage(), request);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ResponseError> businessException(BusinessException exception, HttpServletRequest request){
        ResponseError erro = this.createResponseErro(HttpStatus.INTERNAL_SERVER_ERROR.value(), exception.getMessage(), request);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(erro);
    }

    private ResponseError createResponseErro(Integer status , String message, HttpServletRequest request){
        return new ResponseError(
                LocalDateTime.now(),
                status,
                message,
                request.getRequestURI()
        );
    }
}
