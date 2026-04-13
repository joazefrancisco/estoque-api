package com.joaze.estoqueapi.exception;

public class InsufficientStockException extends RuntimeException {

    public InsufficientStockException(){
        super();
    }
    public InsufficientStockException(String message){
        super(message);
    }
}
