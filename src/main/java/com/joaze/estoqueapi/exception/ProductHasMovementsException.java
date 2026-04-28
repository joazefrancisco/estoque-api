package com.joaze.estoqueapi.exception;

public class ProductHasMovementsException extends Exception {

    public ProductHasMovementsException(){
        super();
    }
    public ProductHasMovementsException(String message){
        super(message);
    }
}
