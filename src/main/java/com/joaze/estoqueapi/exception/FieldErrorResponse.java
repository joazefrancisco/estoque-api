package com.joaze.estoqueapi.exception;

public record FieldErrorResponse(
        String field,
        String name
) {}
